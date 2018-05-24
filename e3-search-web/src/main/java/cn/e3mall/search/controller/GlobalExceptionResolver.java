package cn.e3mall.search.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 全局异常处理
 * @author cxx
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	public static final Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//1.打印控制台
		ex.printStackTrace();
		//2.写日志
		logger.error("系统发生异常",ex);
		//3.发邮件，发短信
		//使用jmail工具包
		//4.给用户展示友好界面
		ModelAndView modelandView = new ModelAndView();
		modelandView.setViewName("error/exception");
		return modelandView;
	}

}
