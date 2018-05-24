package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	
	
	/**
	 * 商品编辑功能
	 */
	@RequestMapping(value="/rest/item/update", method=RequestMethod.POST)
	@ResponseBody
	public E3Result editItem(TbItem item, String desc) {
		E3Result result = itemService.updateItem(item, desc);
		return result;
	}
	
	@RequestMapping(value="/rest/item/query/item/desc/{id}", method=RequestMethod.GET)
	@ResponseBody
	public E3Result editItem1(@PathVariable Long id) {
		TbItemDesc tbItemDesc = itemService.getItemDescById(id);
		return E3Result.ok(tbItemDesc);
	}
	
	@RequestMapping(value="/rest/item/param/item/query/{id}", method=RequestMethod.GET)
	@ResponseBody
	public E3Result editItem2(@PathVariable Long id) {
		TbItem tbItem = itemService.getItemById(id);
		return E3Result.ok(tbItem);
	}
	
	
	/**
	 * 商品删除功能
	 */
	@RequestMapping(value="/rest/item/delete", method=RequestMethod.POST)
	@ResponseBody
	public E3Result delItem(@RequestBody String params) {
		E3Result result = itemService.delItem(params);
		return result;
	}
	
	/**
	 * 商品上架功能
	 */
	@RequestMapping(value="/rest/item/reshelf", method=RequestMethod.POST)
	@ResponseBody
	public E3Result reshelfItem(@RequestBody String params) {
		E3Result result = itemService.reshelfItem(params);
		return result;
	}
	
	/**
	 * 商品下架功能
	 */
	@RequestMapping(value="/rest/item/instock", method=RequestMethod.POST)
	@ResponseBody
	public E3Result instockItem(@RequestBody String params) {
		E3Result result = itemService.instockItem(params);
		return result;
	}
}
