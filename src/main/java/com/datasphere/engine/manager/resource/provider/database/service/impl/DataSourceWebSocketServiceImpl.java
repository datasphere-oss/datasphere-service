package com.datasphere.engine.manager.resource.provider.database.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datasphere.engine.manager.resource.provider.database.exception.WebSocketException;
import com.datasphere.engine.manager.resource.provider.database.service.DataSourceTableMigrationService;
import com.datasphere.server.notifications.websocket.WebSocketManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DataSourceWebSocketServiceImpl{

	private static final Log logger = LogFactory.getLog(DataSourceWebSocketServiceImpl.class);
	
	@Inject
    DataSourceTableMigrationService migrationService;
	
	@Inject
	WebSocketManager webSocketManager;
	
	public void onTransmitting(String id
//							   UserContext userContext
	) throws WebSocketException {
		DataSourceTableMigrationService.Result result = migrationService.getResult(id);
		double persent = 0 ;
		if(result.getTotalNumber() != 0){
			 persent = ((double)result.getSuccessNumber()/(double)result.getTotalNumber());
		}
	
		DecimalFormat df = new DecimalFormat("0");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", id);
		message.put("type","1");
		message.put("percent", df.format(persent*100));
		try{
//			webSocketManager.sendMessage(WSPBuilder.schedule(userContext.getOmSysUser().getUserId(), message));
			logger.info("DS_DB : ["+id+"] websocket notice schedule ["+message.get("percent")+"]");
		} catch(Exception e){
			throw new WebSocketException(e);
		}
	}

	public void afterComplete(String id
//							  UserContext userContext
	) throws WebSocketException{
		DataSourceTableMigrationService.Result result = migrationService.getResult(id);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", id);
		message.put("type","2");
		message.put("state",result.getState());
		message.put("message",result.getMessage());
		message.put("size", result.getSize());
		try{
//			webSocketManager.sendMessage(WSPBuilder.buildDefault(userContext.getOmSysUser().getUserId(), "1", message));
			logger.info("DS_DB : ["+id+"] websocket send completed ["+result+"]");
		} catch(Exception e){
			throw new WebSocketException(e);
		}
	}
}