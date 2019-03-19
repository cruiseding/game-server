package com.jzy.game.model.redis.key;

/**
 * 大厅redis数据key枚举
 *
 * @author CruiseDing
 * @mail 359135103@qq.com
 */
public enum HallKey {
	/** 服务器启动时间 */
	GM_Hall_StartTime("GM_%d:Hall:starttime"),

	/** 角色基本信息 */
	Role_Map_Info("Role_%d:Map:info"),
	/**角色背包*/
	Role_Map_Packet("Role_%d:Map:packet"),
	;

	private final String key;

	HallKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
