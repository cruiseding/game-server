/**工具生成，请遵循规范<br>
 @auther CruiseDing
 */
package com.jzy.game.bydr.tcp.room;

import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.TcpHandler;
import com.jzy.game.message.Mid.MID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jzy.game.message.bydr.BydrRoomMessage.QuitRoomRequest;


@HandlerEntity(mid=MID.QuitRoomReq_VALUE,msg=QuitRoomRequest.class)
public class QuitRoomHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuitRoomHandler.class);

	@Override
	public void run() {
		QuitRoomRequest request = getMsg();
	}
}