package com.sxt.shop.service;

import com.sxt.shop.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author WHSXT-LTD
 * @since 2019-09-20
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	SysUser findUserByUsername(String username);

}
