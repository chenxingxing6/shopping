package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	/**
	 * jedis
	 */
	@Ignore
    public void test() {
		// 第一步：创建一个Jedis对象。需要指定服务端的ip及端口。
    	Jedis jedis = new Jedis("115.159.126.205",6379);
    	// 第二步：使用Jedis对象操作数据库，每个redis命令对应一个方法。
    	jedis.set("myname","chenstart");
    	String myName = jedis.get("myname");
    	System.out.println(myName);
    	jedis.close();
    }
	
	/**
	 * jedis连接池
	 */
	@Ignore
    public void testJedisPool() {
		JedisPool jedispool = new JedisPool("115.159.126.205",6379);
		Jedis jedis = jedispool.getResource();
		jedis.set("myname","chenstart");
    	String myName = jedis.get("myname");
    	System.out.println(myName);
    	jedis.close();
    	jedispool.close();
    }
	
	@Ignore
	public void jedisClusterTest() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("115.159.126.205",7001));
		nodes.add(new HostAndPort("115.159.126.205",7002));
		nodes.add(new HostAndPort("115.159.126.205",7003));
		nodes.add(new HostAndPort("115.159.126.205",7004));
		nodes.add(new HostAndPort("115.159.126.205",7005));
		nodes.add(new HostAndPort("115.159.126.205",7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("hhh","hello world");
		System.out.println(jedisCluster.get("hhh"));
		jedisCluster.close();
	}
	
	
	@Test
    public void testJedisClient() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = app.getBean(JedisClient.class);
		jedisClient.set("mytest","hello world");
		String str = jedisClient.get("mytest");
		System.out.println(str);
	}
	
	
	
}
