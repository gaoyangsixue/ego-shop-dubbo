package com.sxt.shop.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sxt.shop.entity.SysUser;
import com.sxt.shop.mapper.SysUserMapper;
import com.sxt.shop.service.SysUserService;

/**
 * sysUser 的测试
 * @author WHSXT-LTD
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserTest {

	@Autowired
	private SysUserMapper sysUserMapp;

	@Autowired
	private SysUserService sysUserService;

	@Test
	public void testDBMapper() {
		SysUser user = sysUserMapp.selectById(1L);
		System.out.println(user);
	}

	@Test
	public void testService() {
		SysUser byId = sysUserService.getById(1L);
		System.out.println(byId);
	}

}
