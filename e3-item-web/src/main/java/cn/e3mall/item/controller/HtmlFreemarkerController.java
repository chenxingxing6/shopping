package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlFreemarkerController {
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/gethtml")
	@ResponseBody
	public String getHtml() throws Exception{
		Configuration conf = freeMarkerConfigurer.getConfiguration();
		Template template = conf.getTemplate("hello.ftl");
		Map<String,String> data = new HashMap<>();
		data.put("hello","hello cxx");
		Writer out = new FileWriter(new File("F:\\hello.txt"));
		template.process(data,out);
		out.close();
		return "ok";
	}
}
