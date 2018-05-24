package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

public interface OrderService {
	//创建订单
	E3Result createOrder(OrderInfo orderInfo);
}
