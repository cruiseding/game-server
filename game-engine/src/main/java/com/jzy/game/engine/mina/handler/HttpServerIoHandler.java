package com.jzy.game.engine.mina.handler;

import java.util.concurrent.Executor;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import org.slf4j.LoggerFactory;

import com.jzy.game.engine.handler.HandlerEntity;
import com.jzy.game.engine.handler.IHandler;
import com.jzy.game.engine.mina.config.MinaServerConfig;
import com.jzy.game.engine.script.ScriptManager;
import com.jzy.game.engine.server.Service;
import com.jzy.game.engine.util.MsgUtils;
import com.jzy.game.engine.util.StringUtils;

import org.slf4j.Logger;
import org.apache.mina.http.HttpRequestImpl;

/**
 * http消息处理器
 *
 * @author CruiseDing
 * @date 2017-03-31 QQ:359135103
 */
public abstract class HttpServerIoHandler implements IoHandler {

	private static final Logger LOG = LoggerFactory.getLogger(HttpServerIoHandler.class);

	@Override
	public void sessionOpened(IoSession session) {

	}

	@Override
	public void messageReceived(IoSession ioSession, Object message) {
		if (!(message instanceof HttpRequestImpl)) {
			LOG.warn("HttpServerIoHandler:" + message.getClass().getName());
			return;
		}
		long begin = System.currentTimeMillis();

		HttpRequestImpl httpRequest = (HttpRequestImpl) message;
		Class<? extends IHandler> handlerClass = ScriptManager.getInstance()
				.getHttpHandler(httpRequest.getRequestPath());
		ScriptManager scriptManager = ScriptManager.getInstance();
		HandlerEntity handlerEntity = scriptManager.getHttpHandlerEntity(httpRequest.getRequestPath());
		if (handlerClass == null) {
			handlerClass = scriptManager.getHttpHandler(StringUtils.EMPTY);
			handlerEntity = scriptManager.getHttpHandlerEntity(StringUtils.EMPTY);
		}
		if (handlerClass == null) {
			LOG.error("Http 容器 未能找到 content = {} 的 httpMessageBean tostring: {}", 
					  httpRequest.getRequestPath(),
                      ioSession.getRemoteAddress());
			return;
		}

		try {
			IHandler handler = handlerClass.newInstance();
			handler.setMessage(httpRequest);
			handler.setSession(ioSession);
			handler.setCreateTime(System.currentTimeMillis());

			Executor executor = getSevice().getExecutor(handlerEntity.threadType());
			if (executor != null) {
				executor.execute(handler);
			} else {
				handler.run();
//				LOG.error("{}指定的线程{}未开启", handlerClass.getName(), handlerEntity.thread().toString());
			}

		} catch (InstantiationException | IllegalAccessException ex) {
			LOG.error("messageReceived build message error !!! ", ex);
		}

		long cost = System.currentTimeMillis() - begin;
		if (cost > 30L) {
			LOG.error(String.format("\t messageReceived %s msgID[%s] builder[%s] cost %d ms",
					Thread.currentThread().toString(), httpRequest.getRequestPath(), httpRequest.toString(), cost));
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		session.closeOnFlush();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.closeOnFlush();
		LOG.debug("exceptionCaught", cause);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		LOG.debug("http请求建立 " + session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOG.debug("http请求断开 " + session);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (!session.isClosing()) {
			MsgUtils.close(session, "http isClosing");
		}
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		LOG.error("http inputClosed " + session);
		MsgUtils.close(session, "http inputClosed");
	}

	/**
	 * 对应的服务
	 * 
	 * @return
	 */
	protected abstract Service<MinaServerConfig> getSevice();
}
