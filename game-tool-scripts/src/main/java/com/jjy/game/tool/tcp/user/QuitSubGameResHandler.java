package com.jjy.game.tool.tcp.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzy.game.message.Mid.MID;
import com.jzy.game.message.hall.HallLoginMessage.QuitRequest;
import com.jzy.game.message.hall.HallLoginMessage.QuitSubGameResponse;
import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.HttpHandler;
import com.jzy.game.engine.handler.TcpHandler;

/**
 * 退出子游戏
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年7月27日 上午9:32:02
 */
@HandlerEntity(mid = MID.QuitSubGameRes_VALUE, msg = QuitSubGameResponse.class)
public class QuitSubGameResHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuitSubGameResHandler.class);

	public void run() {
		QuitSubGameResponse res = getMsg();
		LOGGER.info("退出子游戏：{}", res.getResult());

		// 发送退出游戏
		QuitRequest.Builder builder = QuitRequest.newBuilder();
		builder.setRid(0);
		getSession().write(builder.build());
	}

}
