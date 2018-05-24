package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${TIEM_CACHE_EXPIRE}")
	private Integer TIEM_CACHE_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//获取商品添加缓存，不影业务响逻辑，try-catch
		try {
			System.out.println("缓存获取商品信息");
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
				return tbItem;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			//结果添加到缓存
			try {
				System.out.println("缓存添加商品信息");
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE",JsonUtils.objectToJson(list.get(0)));
				//设置过期时间(1个小时)
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",TIEM_CACHE_EXPIRE);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象。
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//发送一个添加成功消息
		//发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		//返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result delItem(String params) {
		String [] ids = params.substring(4).split("%2C");
		int i=0;
		for(String id:ids) {
			TbItem tbItem = new TbItem();
			tbItem.setId(Long.valueOf(id));
			tbItem.setStatus((byte) 3);
			i = itemMapper.updateByPrimaryKeySelective(tbItem);
			//j = itemDescMapper.deleteByPrimaryKey(Long.parseLong(id.trim()));
		}
		if(i>0) {
			return E3Result.ok();
		}
		return E3Result.build(500,"ERR");
	}

	@Override
	public E3Result instockItem(String params) {
		//下架-2
		int i=0;
		String [] ids = params.substring(4).split("%2C");
		for(String id:ids) {
			TbItem tbItem = new TbItem();
			tbItem.setId(Long.valueOf(id));
			tbItem.setStatus((byte) 2);
			i = itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		if(i>0) {
			return E3Result.ok();
		}
		return E3Result.build(500,"ERR");
	}

	@Override
	public E3Result reshelfItem(String params) {
		//上架-1
		int i=0;
		String [] ids = params.substring(4).split("%2C");
		for(String id:ids) {
			TbItem tbItem = new TbItem();
			tbItem.setId(Long.valueOf(id));
			tbItem.setStatus((byte) 1);
			i = itemMapper.updateByPrimaryKeySelective(tbItem);
		}		
		if(i>0) {
			return E3Result.ok();
		}
		return E3Result.build(500,"ERR");
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//获取商品描述添加缓存，不影业务响逻辑，try-catch
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json,TbItemDesc.class);
				System.out.println("缓存获取商品描述信息");
				return tbItemDesc;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//结果添加到缓存
		try {
			System.out.println("缓存添加商品描述信息");
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC",JsonUtils.objectToJson(itemDesc));
			//设置过期时间(1个小时)
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",TIEM_CACHE_EXPIRE);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

	@Override
	public E3Result updateItem(TbItem item, String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return E3Result.ok();
	}
}
