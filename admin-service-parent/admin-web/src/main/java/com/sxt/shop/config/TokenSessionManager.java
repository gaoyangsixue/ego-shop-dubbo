package com.sxt.shop.config;

import java.io.Serializable;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

/**
 * 修改系统使用我们自己定义头里面的Token ，而不是使用JessionID
 * @author WHSXT-LTD
 *
 */
public class TokenSessionManager extends DefaultWebSessionManager{

	private static final String TOKEN_HEADER_NAME = "Authorization";
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 需要从前端的头里面取Token
		String token = WebUtils.toHttp(request).getHeader(TOKEN_HEADER_NAME);
		if(!StringUtils.hasText(token)) {
			token = UUID.randomUUID().toString();
		}
		return token;
	}
}
