/**工具生成，请遵循规范<br>
 @auther CruiseDing
 */
package com.jzy.game.hall.tcp.login;

import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.TcpHandler;
import com.jzy.game.message.Mid.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jzy.game.message.hall.HallLoginMessage.QuitSubGameRequest;


@HandlerEntity(mid=MID.QuitSubGameReq_VALUE,msg=QuitSubGameRequest.class)
public class QuitSubGameHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuitSubGameHandler.class);

	@Override
	public void run() {
		QuitSubGameRequest request = getMsg();
	}
}