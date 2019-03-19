package com.jzy.game.gate.script;

import org.apache.mina.core.session.IoSession;
import com.jzy.game.engine.script.IScript;
import com.jzy.game.model.constant.Reason;

/**
 * 用户接口
 * @author CruiseDing
 * @QQ 359135103
 * 2017年7月26日 下午4:42:39
 */
public interface IUserScript extends IScript {
	
	/**
	 * 用户退出处理
	 * @author CruiseDing
	 * @QQ 359135103
	 * 2017年7月26日 下午4:47:34
	 * @param session 游戏客户端会话
	 * @param reason 原因
	 */
	default void quit(IoSession session,Reason reason){
		
	}
	
}
