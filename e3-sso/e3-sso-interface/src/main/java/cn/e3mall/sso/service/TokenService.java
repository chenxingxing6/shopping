package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * 根据token查询用户信息
 * <p>Title: TokenService</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public interface TokenService {

	E3Result getUserByToken(String token);
}
