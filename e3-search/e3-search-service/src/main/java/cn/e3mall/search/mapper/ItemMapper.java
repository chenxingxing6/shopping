package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	//根据id获取商品信息（id为消息队列刚传过来的）
	SearchItem getItemById(long itemId);
	
}
