package cn.e3mall.sso.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class SSOApp {
	@Test
	public void test() throws IOException {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("SSO服务已经启动。。。。");
		System.in.read();
		System.out.println("SSO服务已经关闭");
	}
}
