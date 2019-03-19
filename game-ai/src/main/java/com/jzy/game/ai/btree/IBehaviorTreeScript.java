package com.jzy.game.ai.btree;

import com.jzy.game.engine.script.IScript;
import com.jzy.game.engine.struct.Person;

/**
 * 行为树脚本
 * @author CruiseDing
 * @QQ 359135103
 * 2017年11月
 */
public interface IBehaviorTreeScript extends IScript{
	
	/**
	 * 为对象添加行为树
	 * @author CruiseDing
	 * @QQ 359135103
	 * @param person 
	 */
	default void addBehaviorTree(Person person) {
		
	}
}
