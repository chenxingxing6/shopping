package cn.e3mall.content.publish;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class App {
	@Test
	public void test() throws IOException {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("服务已经启动。。。。");
		System.in.read();
		System.out.println("服务已经关闭");
	}
}
