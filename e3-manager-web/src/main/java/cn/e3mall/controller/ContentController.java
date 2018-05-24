package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理Controller
 * <p>Title: ContentController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;

	@RequestMapping(value="/content/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content) {
		//调用服务把内容数据保存到数据库
		E3Result e3Result = contentService.addContent(content);
		return e3Result;
	}
	
	/**
	 * 根据id查询某分类下的内容
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = contentService.getContentList(categoryId,page, rows);
		return result;
	}
	
	/**
	 * 删除功能
	 */
	@RequestMapping(value="/content/delete", method=RequestMethod.POST)
	@ResponseBody
	public E3Result delContent(@RequestBody String params) {
		E3Result result = contentService.delContent(params);
		return result;
	}
	
	/**
	 * 更新功能
	 */
	@RequestMapping(value="/rest/content/edit", method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContent(TbContent content) {
		E3Result result = contentService.updateContent(content);
		return result;
	}
}
