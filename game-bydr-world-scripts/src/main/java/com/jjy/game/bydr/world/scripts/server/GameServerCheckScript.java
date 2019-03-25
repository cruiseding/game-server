package com.jjy.game.bydr.world.scripts.server;

import com.jzy.game.bydr.manager.RoleManager;
import com.jzy.game.message.ServerMessage;
import com.jzy.game.bydr.world.server.BydrWorldServer;
import com.jzy.game.model.script.IGameServerCheckScript;
import com.jzy.game.engine.mina.config.MinaServerConfig;

/**
 * 服务器状态监测脚本
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年7月10日 下午4:36:25
 */
public class GameServerCheckScript implements IGameServerCheckScript {

	@Override
	public void buildServerInfo(ServerMessage.ServerInfo.Builder builder) {
		IGameServerCheckScript.super.buildServerInfo(builder);
		MinaServerConfig minaServerConfig = BydrWorldServer.getInstance().getGameHttpServer().getMinaServerConfig();
		builder.setHttpport(minaServerConfig.getHttpPort());
		builder.setIp(minaServerConfig.getIp());
		builder.setOnline(RoleManager.getInstance().getOnlineRoles().size());	//设置在线人数
	}

}
