package com.jzy.game.gate.tcp.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.TcpHandler;
import com.jzy.game.engine.util.TimeUtils;
import com.jzy.game.message.Mid.MID;
import com.jzy.game.message.system.SystemMessage.HeartRequest;
import com.jzy.game.message.system.SystemMessage.HeartResponse;

/**
 * 心跳
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年9月1日 下午3:39:03
 */
@HandlerEntity(mid = MID.HeartReq_VALUE, msg = HeartRequest.class)
public class HeartHandler extends TcpHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);

	@Override
	public void run() {
		HeartResponse.Builder builder = HeartResponse.newBuilder();
		builder.setServerTime(TimeUtils.currentTimeMillis());
		session.write(builder.build());
		// LOGGER.info("{}心跳",MsgUtil.getIp(session));
	}

}
