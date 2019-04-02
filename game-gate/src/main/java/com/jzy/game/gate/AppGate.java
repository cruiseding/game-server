package com.jzy.game.gate;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzy.game.engine.redis.jedis.JedisClusterConfig;
import com.jzy.game.engine.redis.jedis.JedisManager;
import com.jzy.game.engine.script.ScriptManager;
import com.jzy.game.engine.util.FileUtils;
import com.jzy.game.gate.manager.MongoManager;
import com.jzy.game.gate.server.GateServer;

/**
 * 启动类
 *
 */
public final class AppGate {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppGate.class);

	private static String configPath;
	
	private static JedisManager redisManager;
	
	private static GateServer gateServer;

	public static void main(String[] args) {
		initConfigPath();

		// redis
		JedisClusterConfig jedisClusterConfig = FileUtils.getConfigXML(configPath, "jedisClusterConfig.xml", JedisClusterConfig.class);
		if (jedisClusterConfig == null) {
			LOGGER.error("redis配置{}未找到", configPath);
			System.exit(1);
		}
		redisManager = new JedisManager(jedisClusterConfig);

		// 创建mongodb连接
		MongoManager.getInstance().createConnect(configPath);

		// 加载脚本
		ScriptManager.getInstance().init(null);

		// 通信服务
		gateServer = new GateServer();
		
		new Thread(gateServer).start();
	}
	
	private static void initConfigPath() {
		File file = new File(System.getProperty("user.dir"));
		if ("target".equals(file.getName())) {
			configPath = file.getPath() + File.separatorChar + "config";
		} else {
			configPath = file.getPath() + File.separatorChar + "target" + File.separatorChar + "config";
		}
		LOGGER.info("配置路径为：" + configPath);
	}

	public static String getConfigPath() {
		return configPath;
	}

	public static GateServer getHallServer() {
		return gateServer;
	}
	
	public static JedisManager getJedisManager() {
		return redisManager;
	}

}
