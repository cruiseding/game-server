package com.jjy.game.manage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jjy.game.manage.core.ext.DataStoreFactoryBean;
import com.jjy.game.manage.core.ext.MorphiaFactoryBean;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;

@Configuration
@ComponentScan("com.jjy.game.manage")
public class MongoConfig {
	
	@Autowired
	private MorphiaFactoryBean morphia;
	
	@Value("${mongo.db.databaseName}")
	private String dbName;
	
	@Value("${mongo.db.host}")
	private String uri;

	@Bean("mongoOptions")
	public MongoClientOptions mongoOptions() {
		MongoClientOptions.Builder builder = MongoClientOptions.builder();
		//与每个主机的连接数，默认为10
		builder.connectionsPerHost(10);
		//连接超时时间(毫秒)，默认为10000
		builder.connectTimeout(10000);
		//是否创建一个finalize方法，以便在客户端没有关闭DBCursor的实例时，清理掉它。默认为true
		builder.cursorFinalizerEnabled(true);
		//线程等待连接可用的最大时间(毫秒)，默认为120000
		builder.maxWaitTime(120000);
		//可等待线程倍数，默认为5.例如connectionsPerHost最大允许10个连接，则10*5=50个线程可以等待，更多的线程将直接抛异常
		builder.threadsAllowedToBlockForConnectionMultiplier(5);		
		//socket读写时超时时间(毫秒)，默认为0，不超时
		builder.socketTimeout(0);
		//对应全局的WriteConcern.SAFE，默认为false,w，默认为0,wtimeout，默认为0,
		//WriteConcern.FSYNC_SAFE，如果为真，每次写入要等待写入磁盘，默认为false,
		//WriteConcern.JOURNAL_SAFE，如果为真，每次写入要等待日志文件写入磁盘，默认为false
		builder.writeConcern(WriteConcern.ACKNOWLEDGED);		
		return builder.build();
	}
	
	@Bean("morphia")
	public MorphiaFactoryBean morphia() {
		MorphiaFactoryBean mfb = new MorphiaFactoryBean();
		String[] mapPackages = {"com.jjy.game.manage"};
		mfb.setMapPackages(mapPackages);
		return mfb;
	}
	
	@Bean
	public DataStoreFactoryBean datastore() {
		DataStoreFactoryBean dsb = new DataStoreFactoryBean();
		try {
			dsb.setMorphia(morphia.getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		dsb.setDbName(dbName);
		dsb.setUri(uri);
		dsb.setToEnsureIndexes(true);
		dsb.setToEnsureCaps(true);
		return dsb;
	}
}
