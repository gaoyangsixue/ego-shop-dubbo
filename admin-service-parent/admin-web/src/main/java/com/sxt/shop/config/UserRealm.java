package com.sxt.shop.config;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.sxt.shop.domain.User;
import com.sxt.shop.entity.SysUser;
import com.sxt.shop.service.SysUserService;


@Configuration
public class UserRealm extends AuthorizingRealm{

	@Autowired
	private SysUserService userService;
	/**
	 * 
	 * 授权 执行n次
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		List<String> roles = user.getRoles();
		List<String> permissions = user.getPermissions();
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addRoles(roles);
		simpleAuthorizationInfo.addStringPermissions(permissions);
		return simpleAuthorizationInfo;
	}

	/**
	 * 登录 执行一次
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = token.getPrincipal().toString();
		// 使用用户名查询用户
		SysUser sysUser = userService.findUserByUsername(username);
		if(sysUser==null) { // 若用户不存在，应该return null ,而不是把null 也放在SimpleAuthenticationInfo 里面
			return null ;
		}
		// 查询该用户的权限信息
		// 返回AuthenticationInfo 对象
		return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), ByteSource.Util.bytes("whsxt".getBytes()),username);
	}
  
	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456", ByteSource.Util.bytes("whsxt".getBytes()), 2);
		String hex = md5Hash.toHex();
		System.out.println(hex);
	}

}
