package com.jzy.game.gate.jedis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class JedisClusterTest {
	
	@Test
	public void testJedisCluster() throws Exception {
		//创建一连接，JedisCluster对象,在系统中是单例存在
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("47.105.165.12", 7000));
		nodes.add(new HostAndPort("47.105.165.12", 7001));
		nodes.add(new HostAndPort("47.105.165.12", 7002));
		nodes.add(new HostAndPort("47.105.165.12", 7003));
		nodes.add(new HostAndPort("47.105.165.12", 7004));
		nodes.add(new HostAndPort("47.105.165.12", 7005));
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(500);
		poolConfig.setMaxIdle(5);
		poolConfig.setMaxWaitMillis(60000);
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		poolConfig.setMinEvictableIdleTimeMillis(1800000);
		poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestWhileIdle(false);
		poolConfig.setTestOnReturn(false);
		JedisCluster cluster = new JedisCluster(nodes, 2000, 2000, 6, "@E$R@%YT", poolConfig);
		//执行JedisCluster对象中的方法，方法和redis一一对应。
//		cluster.set("cluster-test", "my jedis cluster test");
//		cluster.del("cluster-test");
		String result = cluster.get("cluster-test");
		System.out.println(result);
		//程序结束时需要关闭JedisCluster对象
		cluster.close();
	}
	
//	@Test
	public void testJedis() throws Exception {
		Jedis jedis = new Jedis("47.105.165.12");
		jedis.auth("@E$R@%YT");
	}


}
