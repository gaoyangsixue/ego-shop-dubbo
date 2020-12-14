package com.sxt.shop.service.impl;

import com.sxt.shop.entity.SysUser;
import com.sxt.shop.mapper.SysUserMapper;
import com.sxt.shop.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author WHSXT-LTD
 * @since 2019-09-20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Autowired
	private SysUserMapper mapper;

	private static Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Override
	public SysUser findUserByUsername(String username) {
		if(!StringUtils.hasText(username)) {
			throw new  RuntimeException("用户名不能为null");
		}
		log.info("查询用户，用户名为：{}",username);
		
		List<SysUser> users = mapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
		if(users.isEmpty()) {
			return  null ;
		}
		if(users.size()>1) {
			log.error("用户名：{},存在多个用户",username);
			throw new RuntimeException("用户名不唯一，可能是数据库迁移错误");
		}
		return users.get(0);
	}

}
