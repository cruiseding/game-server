package com.jzy.game.hall.script;

import java.util.function.Consumer;
import com.jzy.game.engine.script.IScript;
import com.jzy.game.model.struct.User;

/**
 * 用户接口
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年7月7日 下午4:16:57
 */
public interface IUserScript extends IScript {

	/**
	 * 创建用户
	 * 
	 * @param userConsumer
	 * @return
	 */
    public default User createUser(Consumer<User> userConsumer) {
		return null;
	}
}
