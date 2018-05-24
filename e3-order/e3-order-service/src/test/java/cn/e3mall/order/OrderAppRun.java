package cn.e3mall.order;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderAppRun {
	@Test
	public void testRun() throws Exception{
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("订单服务开启....");
		System.in.read();
	}
}
