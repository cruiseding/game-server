package com.jzy.game.engine.server;

/**
 * 客户端接口
 * @author CruiseDing
 * @QQ 359135103
 * 2017年8月29日 上午10:31:07
 */
public interface ITcpClientService<T extends BaseServerConfig> extends Runnable {

	/**
	 * 发送消息
	 * @author CruiseDing
	 * @QQ 359135103
	 * 2017年8月29日 上午10:28:04
	 * @param object
	 * @return
	 */
    boolean sendMsg(Object object);
	
	/**
	 * 检测服务器状态
	 * @author CruiseDing
	 * @QQ 359135103
	 * 2017年8月29日 上午10:43:07
	 */
    void checkStatus();
}
