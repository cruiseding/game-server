package com.jzy.game.engine.mina.handler;

import java.util.concurrent.Executor;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.IHandler;
import com.jzy.game.engine.handler.TcpHandler;
import com.jzy.game.engine.script.ScriptManager;
import com.jzy.game.engine.server.BaseServerConfig;
import com.jzy.game.engine.server.Service;
import com.jzy.game.engine.util.MsgUtils;

/**
 * 默认消息处理器
 * <p>
 * 消息头+消息内容 <br>
 * 消息头可能有消息长度、消息ID、用户ID
 * </p>
 * 
 * @author CruiseDing
 * @date 2017-03-30 QQ:359135103
 */
public abstract class DefaultProtocolHandler implements IoHandler {

	protected static final Logger log = LoggerFactory.getLogger(DefaultProtocolHandler.class);
	
	protected final int messageHeaderLength; // 消息头长度

	/**
	 * 
	 * @param messageHeaderLength
	 *            消息头长度
	 */
	public DefaultProtocolHandler(int messageHeaderLength) {
		this.messageHeaderLength = messageHeaderLength;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// log.warn("已创建连接{}", session);
	}

	@Override
	public void sessionOpened(IoSession session) {
		log.warn("已打开连接{}", session);
	}

	@Override
	public void messageSent(IoSession ioSession, Object message) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) {
		log.warn("连接{}已关闭sessionClosed", session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) {
		log.warn("连接{}处于空闲{}", session, idleStatus);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) {
		log.error("连接{}异常：{}", session, throwable);
		session.closeNow();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		log.warn("连接{}inputClosed已关闭", session);
		session.closeNow();
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		byte[] bytes = (byte[]) obj;
		try {
			if (bytes.length < messageHeaderLength) {
				log.error("messageReceived:消息长度{}小于等于消息头长度{}", bytes.length, messageHeaderLength);
				return;
			}
			int offset = messageHeaderLength > 4 ? 8 : 0;
			int msgID = MsgUtils.getMessageID(bytes, offset); // 消息ID
			ScriptManager scriptManager = ScriptManager.getInstance();
			if (scriptManager.tcpMsgIsRegister(msgID)) {
				Class<? extends IHandler> handlerClass = scriptManager.getTcpHandler(msgID);
				HandlerEntity handlerEntity = scriptManager.getTcpHandlerEntity(msgID);
				if (handlerClass != null) {
					Message message = MsgUtils.buildMessage(handlerEntity.msg(), bytes, messageHeaderLength,
							bytes.length - messageHeaderLength);
					TcpHandler handler = (TcpHandler) handlerClass.newInstance();
					if (handler != null) {
						if (offset > 0) { // 偏移量大于0，又发玩家ID
							long rid = MsgUtils.getMessageRID(bytes, 0);
							handler.setRid(rid);
						}
						messageHandler(session, handlerEntity, message, handler);
						return;
					}
				}
			}
			forward(session, msgID, bytes);
		} catch (Exception e) {
			log.error("messageReceived", e);
			int msgid = MsgUtils.getMessageID(bytes, 0);
			log.warn("尝试按0移位处理,id：{}", msgid);
		}
	}

	/**
	 * 消息处理
	 *
	 * @param session
	 * @param messageBean
	 * @param message
	 * @param handler
	 * @param msgID
	 * @param bytes
	 */
	protected void messageHandler(IoSession session, HandlerEntity handlerEntity, Message message, TcpHandler handler) {
		handler.setMessage(message);
		handler.setSession(session);
		handler.setCreateTime(System.currentTimeMillis());
		Executor executor = getService().getExecutor(handlerEntity.threadType());
		if (executor == null) {
			// log.warn("处理器{}没有分配线程", handler.getClass().getName());
			handler.run();
			return;
		}
		executor.execute(handler);
	}

	/**
	 * 转发消息
	 *
	 * @param session
	 * @param msgID
	 * @param bytes
	 */
	protected abstract void forward(IoSession session, int msgID, byte[] bytes);

	public abstract Service<? extends BaseServerConfig> getService();
}
