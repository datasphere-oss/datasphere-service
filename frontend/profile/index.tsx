import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"userprofile\">
    +             <div className=\"top-level-tabs\">
    +                 <div className=\"horizontal-flex row-fluid object-nav\">
    +                     <div className=\"flex oh object-breadcrumb\">
    +                         <a className=\"item\">
    +                             <i className=\"icon icon-user universe-background profile\" />
    +                             <span className=\"title ellipsis\"> {profile.user.displayName}</span>
    +                         </a>
    +                     </div>
    +                     {!appConfig.noLoginMode && appConfig.login == profile.user.login ? (
    +                         <div className=\"noflex\">
    +                             <NavLink className=\"{enabled: topNav.tab == 'settings'}\" to=\"profile.my.settings\">
    +                                 我的设置
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'achievements'}\" to=\"profile.my.achievements\">
    +                                 成就
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'exports'}\" to=\"profile.my.exports\">
    +                                 导出
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'stars'}\" to=\"profile.my.stars\">
    +                                 标星和查看
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'apikeys'}\" to=\"profile.my.apikeys\">
    +                                 API 键
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'credentials'}\" to=\"profile.my.credentials\">
    +                                 连接认证
    +                             </NavLink>
    +                             <NavLink className=\"{enabled: topNav.tab == 'account'}\" to=\"profile.my.account\">
    +                                 我的账户
    +                             </NavLink>
    + 
    +                             <div className=\"otherLinks\">
    +                                 <button
    +                                     className=\"btn btn-default\"
    +                                     onClick={() => {
    +                                         logout();
    +                                     }}
    +                                     style=\"float:right; margin-right:5px;\">
    +                                     <i className=\"icon-signout\">退出</i>
    +                                 </button>
    +                                 <i className=\"icon-signout\" />
    +                             </div>
    +                             <i className=\"icon-signout\" />
    +                         </div>
    +                     ) : null}
    +                     <i className=\"icon-signout\" />
    +                 </div>
    +                 <i className=\"icon-signout\" />
    +             </div>
    +             <i className=\"icon-signout\">
    +                 <div className=\"dss-page row-fluid\">
    +                     <div block-api-error=\"\">
    +                         {profile.user.login ? (
    +                             <div
    +                                 className=\"span2 offset0 nav-list-sidebar sidebar-admin\"
    +                                 tab-model=\"uiState.settingsPane\">
    +                                 {profile.user ? (
    +                                     <div className=\"avatar square shade\" style=\"width:80%; margin: 5px auto\">
    +                                         {isDSSAdmin() ||
    +                                         (!appConfig.noLoginMode && appConfig.login == profile.user.login) ? (
    +                                             <span>
    +                                                 <div className=\"avatar-overlay\">修改图片 </div>
    +                                                 {profile.user.login ? (
    +                                                     <totem
    +                                                         object-type=\"'USER'\"
    +                                                         object-id=\"profile.user.login\"
    +                                                         object-img-hash=\"profile.user.objectImgHash\"
    +                                                         size=\"256\"
    +                                                         editable=\"true\"
    +                                                     />
    +                                                 ) : null}
    +                                             </span>
    +                                         ) : null}
    +                                         {!isDSSAdmin() &&
    +                                         !(!appConfig.noLoginMode && appConfig.login == profile.user.login) ? (
    +                                             <span>
    +                                                 {profile.user.login ? (
    +                                                     <totem
    +                                                         object-type=\"'USER'\"
    +                                                         object-id=\"profile.user.login\"
    +                                                         object-img-hash=\"profile.user.objectImgHash\"
    +                                                         size=\"256\"
    +                                                         editable=\"false\"
    +                                                     />
    +                                                 ) : null}
    +                                             </span>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    +                                 {/* <div style=\"text-align: center; color: #666; margin-top: 15px;\">
    +                 Login: @{{profile.user.login}}
    +             </div> */}
    + 
    +                                 <div style=\"padding: 20px\">
    +                                     <div>
    +                                         <h2 className=\"page-subtitle dib\">登录: </h2>
    +                                         <span style=\"font-size: 13px; color: #666666; margin-left: 5px;\">
    +                                             @{profile.user.login}
    +                                         </span>
    +                                     </div>
    +                                     <div>
    +                                         <h2 className=\"page-subtitle dib\">Profile: </h2>
    +                                         <span style=\"font-size: 13px; color: #666666; margin-left: 5px;\">
    +                                             {profile.user.userProfile || '[None]'}
    +                                         </span>
    +                                     </div>
    +                                     <h2 className=\"page-subtitle\">群组:</h2>
    +                                     <ul className=\"groupList\">
    +                                         {profile.user.groups.map((group, index: number) => {
    +                                             return (
    +                                                 <li key={`item-${index}`}>
    +                                                     <i className=\"icon-group\" style=\"font-size: 0.8em;\">
    +                                                         {' '}
    +                                                         {group}
    +                                                     </i>
    +                                                 </li>
    +                                             );
    +                                         })}
    +                                         <i className=\"icon-group\" style=\"font-size: 0.8em;\" />
    +                                     </ul>
    +                                     <i className=\"icon-group\" style=\"font-size: 0.8em;\" />
    +                                 </div>
    +                                 <i className=\"icon-group\" style=\"font-size: 0.8em;\">
    +                                     <div style=\"padding: 0 20px; text-align: center;\">
    +                                         {hooks.isDirty && hooks.isDirty() ? (
    +                                             <div
    +                                                 className=\"btn-group save-button\"
    +                                                 style=\"display: inline-block; width: 100px; margin-top: 5px;\">
    +                                                 <button
    +                                                     className=\"btn btn-small btn-primary\"
    +                                                     style=\"width: 100%\"
    +                                                     onClick={() => {
    +                                                         hooks.save();
    +                                                     }}>
    +                                                     <i className=\"icon-save\" />
    +                                                     &nbsp;保存
    +                                                 </button>
    +                                             </div>
    +                                         ) : null}
    +                                         {hooks.isDirty && !hooks.isDirty() ? (
    +                                             <div
    +                                                 className=\"btn-group save-button\"
    +                                                 style=\"display: inline-block; width: 100px; margin-top: 5px;\">
    +                                                 <button
    +                                                     className=\"btn btn-small btn-primary\"
    +                                                     disabled={true}
    +                                                     style=\"width: 100%\">
    +                                                     <i className=\"icon-save\" />
    +                                                     &nbsp;已保存!
    +                                                 </button>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 </i>
    +                             </div>
    +                         ) : null}
    +                         <i className=\"icon-group\" style=\"font-size: 0.8em;\">
    +                             <div className=\"span10 h100 offset0\">
    +                                 {profile.user ? (
    +                                     <div ui-view=\"\" className=\"h100 oa\" style=\"margin-bottom: 48px;\" />
    +                                 ) : null}
    +                             </div>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-group\" style=\"font-size: 0.8em;\" />
    +                 </div>
    +             </i>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;