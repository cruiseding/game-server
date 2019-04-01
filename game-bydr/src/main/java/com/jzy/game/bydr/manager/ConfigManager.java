package com.jzy.game.bydr.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jzy.game.bydr.AppBydr;
import com.jzy.game.bydr.config.GameConfig;
import com.jzy.game.engine.script.ScriptManager;
import com.jzy.game.engine.util.FileUtils;
import com.jzy.game.model.mongo.bydr.entity.CFish;
import com.jzy.game.model.script.IConfigScript;

/**
 * 配置管理
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年8月2日 下午3:36:16
 */
public class ConfigManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
	private static volatile ConfigManager configManager;
	private GameConfig gameConfig = new GameConfig();
	/**鱼配置信息*/
	private Map<Integer, CFish> fishMap=new ConcurrentHashMap<>();

	private ConfigManager() {
    }

	public static ConfigManager getInstance() {
		if (configManager == null) {
			synchronized (ConfigManager.class) {
				if (configManager == null) {
					configManager = new ConfigManager();
				}
			}
		}
		return configManager;
	}

	/**
	 * 加载配置表
	 * @author CruiseDing
	 * @QQ 359135103
	 * 2017年10月18日 下午2:48:45
	 */
	public void loadConfig() {
		gameConfig = FileUtils.getConfigXML(AppBydr.getConfigPath(), "gameConfig.xml", GameConfig.class);
		if (gameConfig == null) {
			throw new RuntimeException(String.format("游戏常量%s/gameConfig.xml 文件不存在", AppBydr.getConfigPath()));
		}
		ScriptManager.getInstance().getBaseScriptEntry().functionScripts(IConfigScript.class, (IConfigScript script)->script.reloadConfig(null));
	}

	public GameConfig getGameConfig() {
		return gameConfig;
	}
	
	public Map<Integer, CFish> getFishMap() {
		return fishMap;
	}

	public void setFishMap(Map<Integer, CFish> fishMap) {
		this.fishMap = fishMap;
	}
	
	/**
	 * 鱼配置信息
	 * @author CruiseDing
	 * @QQ 359135103
	 * 2017年10月18日 下午3:18:43
	 * @param configId
	 * @return
	 */
	public CFish getFish(int configId) {
		if(fishMap.containsKey(configId)) {
			return fishMap.get(configId);
		}
		LOGGER.warn("CFish配置错误:{}未配置",configId);
		return null;
	}
}
