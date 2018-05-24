package cn.e3mall.search;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class SearchApp {
	@Test
	public void test() throws IOException {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("service服务已经启动。。。。");
		System.in.read();
		System.out.println("服务已经关闭");
	}
}
