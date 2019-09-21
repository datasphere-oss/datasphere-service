/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.server.common.saml;

import com.datasphere.server.domain.user.UserRepository;
import com.datasphere.server.domain.user.UserService;
import com.datasphere.server.domain.user.group.Group;
import com.datasphere.server.domain.user.group.GroupMember;
import com.datasphere.server.domain.user.group.GroupService;
import com.datasphere.server.domain.user.role.RoleService;
import com.datasphere.server.domain.user.role.RoleSet;
import com.datasphere.server.domain.user.role.RoleSetRepository;
import com.datasphere.server.domain.user.role.RoleSetService;
import com.datasphere.server.domain.workspace.Workspace;
import com.datasphere.server.domain.workspace.WorkspaceRepository;
import com.datasphere.server.util.PolarisUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * this class is autowired to the SamlProvider, so it tries to get the user's details from the token using this 
 * class, o/w it returns null.
 * @author Ohad
 *
 */
@Component
@Transactional
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService{

  private static final Logger LOGGER = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	GroupService groupService;

	@Autowired
	WorkspaceRepository workspaceRepository;

	@Autowired
	RoleSetRepository roleSetRepository;

	@Autowired
	RoleSetService roleSetService;

	@Autowired
  EntityManager entityManager;

	@Autowired
  SAMLProperties samlProperties;

	@Override
	public UserDetails loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException{
		String nameID = credential.getNameID().getValue();
    LOGGER.debug("loadUserBySAML from {}, for : {}", credential.getRemoteEntityID(), nameID);

		//New user registration
    UserDetails metatronUser = userRepository.findByUsername(nameID);
		if(metatronUser == null) {
      LOGGER.debug("{} is not metatron user.", nameID);
			metatronUser = createMetatronUser(credential, getUserMapper(credential));
		} else {
      LOGGER.debug("{} is metatron user.", nameID);
    }

    ((com.datasphere.server.domain.user.User) metatronUser).setRoleService(roleService);

    // Preload credentials
    metatronUser.getAuthorities();

		return metatronUser;
	}

  @Transactional
	public com.datasphere.server.domain.user.User createMetatronUser(SAMLCredential credential, SAMLUserMapper samlUserMapper){
    LOGGER.debug("create metatron user for {}", credential.getNameID().getValue());

	  com.datasphere.server.domain.user.User metatronUser;

		if(samlUserMapper == null){
      metatronUser = new com.datasphere.server.domain.user.User();
    } else {
		  metatronUser = samlUserMapper.createUser(credential);
    }

		//UserName uses NameID
		String nameID = credential.getNameID().getValue();
		metatronUser.setUsername(nameID);
		metatronUser.setUserOrigin(credential.getRemoteEntityID());

    for(Attribute attr : credential.getAttributes()){
      Map<String, String> attrMap = new HashMap<>();
      attrMap.put("name", attr.getName());
      attrMap.put("friendlyName", attr.getFriendlyName());
      attrMap.put("value", credential.getAttributeAsString(attr.getName()));
			LOGGER.debug("name : {}, value : {}, friendlyName : {}", attrMap.get("name"), attrMap.get("value"), attrMap.get("friendlyName"));
    }

		if (StringUtils.isBlank(metatronUser.getFullName())) {
			metatronUser.setFullName(metatronUser.getUsername());
		}

		// mail If you do not perform a transfer and do not specify a password, the system generates a password
		metatronUser.setPassword(PolarisUtils.createTemporaryPassword(8));

		//Basic is deactivated
		metatronUser.setStatus(com.datasphere.server.domain.user.User.Status.ACTIVATED);

		// Group Specify default group if no information
		Group defaultGroup = groupService.getDefaultGroup();
		if (defaultGroup == null) {
			LOGGER.warn("Default group not found.");
		} else {
      Session session = entityManager.unwrap(org.hibernate.Session.class);
			defaultGroup.addGroupMember(new GroupMember(metatronUser.getUsername(), metatronUser.getFullName()));
		}

    userRepository.save(metatronUser);

		// Create Workspace (if no workspace is registered)
		RoleSet roleSet = roleSetService.getDefaultRoleSet();

		Workspace workspace = new Workspace();
		workspace.setPublicType(Workspace.PublicType.PRIVATE);
		workspace.setName(metatronUser.getFullName() + " Workspace");
		workspace.setOwnerId(metatronUser.getUsername());
		workspace.addRoleSet(roleSet);

		if(StringUtils.isNotEmpty(metatronUser.getWorkspaceType())
						&& Workspace.workspaceTypes.contains(metatronUser.getWorkspaceType())) {
			workspace.setType(metatronUser.getWorkspaceType());
		} else {
			workspace.setType(Workspace.workspaceTypes.get(0)); // "DEFAULT" setting
		}

		workspaceRepository.saveAndFlush(workspace);

		return metatronUser;
	}

	private SAMLUserMapper getUserMapper(SAMLCredential samlCredential) throws AuthenticationException{
    LOGGER.debug("search user mapper for remoteEntity {}", samlCredential.getRemoteEntityID());

    //need add property userMapperClass
		//ex) polaris.saml.userMapperClass=com.datasphere.server.common.saml.SAMLBhartiUserMapper
		String userMapperClassName = samlProperties.userMapperClass;
		LOGGER.debug("found userMapperClassName : {}", userMapperClassName);
		
		if(StringUtils.isNotEmpty(userMapperClassName)){
			try{
				Class<?> clazz = Class.forName(userMapperClassName);
				Constructor<?> ctor = clazz.getConstructor();
				return (SAMLUserMapper) ctor.newInstance();
			} catch (Exception e){
			}
		}
		return null;
  }

  private String getAttributeValue(XMLObject attributeValue){
    String returnValue;

    if(attributeValue == null){
      returnValue = null;
    } else if(attributeValue instanceof XSString){
      returnValue = getStringAttributeValue((XSString) attributeValue);
    } else if(attributeValue instanceof XSAnyImpl){
      returnValue = getAnyAttributeValue((XSAnyImpl) attributeValue);
    } else {
      returnValue = attributeValue.toString();
    }
    return returnValue;
  }

  private String getStringAttributeValue(XSString attributeValue){
    return attributeValue.getValue();
  }

  private String getAnyAttributeValue(XSAnyImpl attributeValue){
    return attributeValue.getTextContent();
  }


}
