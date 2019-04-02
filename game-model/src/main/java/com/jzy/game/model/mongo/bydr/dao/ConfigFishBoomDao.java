/**工具生成，请遵循规范。
 @author CruiseDing
*/
package com.jzy.game.model.mongo.bydr.dao;

import java.util.List;
import org.mongodb.morphia.dao.BasicDAO;
import com.jzy.game.engine.mongo.AbstractMongoManager;
import com.jzy.game.model.mongo.bydr.entity.ConfigFishBoom;

public class ConfigFishBoomDao extends BasicDAO<ConfigFishBoom, Integer> {
	private static volatile ConfigFishBoomDao configFishBoomDao;

	public ConfigFishBoomDao(AbstractMongoManager mongoManager) {
		super(ConfigFishBoom.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),mongoManager.getMongoConfig().getDbName());
	}

	public static ConfigFishBoomDao getDB(AbstractMongoManager mongoManager) {
		if(configFishBoomDao == null) {
			synchronized (ConfigFishBoomDao.class){
				if(configFishBoomDao == null){
					configFishBoomDao = new ConfigFishBoomDao(mongoManager);
					}
				}
			}
		return configFishBoomDao;
	}

	public static List<ConfigFishBoom> getAll(){
		 return configFishBoomDao.createQuery().asList();
	}

}