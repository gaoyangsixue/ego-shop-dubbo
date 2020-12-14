package com.sxt.shop.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sxt.shop.domain.User;
import com.sxt.shop.param.LoginParam;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.annotations.ApiOperation;

@RestController
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private StringRedisTemplate redis;

	private static final String AUTH_CODE_PREFIX = "AUTH:CODE:"; // UUID

	/**
	 * 登录
	 * @param username
	 * 用户名
	 * @param password
	 * 密码
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginParam loginParam) {
		String token = null ;
		try {
			/**
			 * 校验验证码
			 */
			checkAuthCode(loginParam.getSessionUUID(),loginParam.getImageCode());
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginParam.getPrincipal(), loginParam.getCredentials());
			Subject subject = SecurityUtils.getSubject();
			subject.login(usernamePasswordToken);
			token = subject.getSession().getId().toString();
		} catch (AccountException e) {
			return ResponseEntity.badRequest().body("用户不存在或者过期");//400
		}catch (CredentialsException e) {
			return ResponseEntity.badRequest().body("用户名或密码错误");
		}catch (AuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(token); // statuscode：200 只有token
	}

	private void checkAuthCode(String sessionUUID, String imageCode) {
		if(StringUtils.hasText(sessionUUID)&&StringUtils.hasText(imageCode)) {
			String authCode = redis.opsForValue().get(AUTH_CODE_PREFIX+sessionUUID);
			if(!imageCode.equalsIgnoreCase(authCode)) {
				throw new AuthenticationException("验证码错误");
			}

		}else {
			throw new RuntimeException("验证码或uuid为null");
		}

	}

	@GetMapping("captcha.jpg")
	public void captcha(@RequestParam(required = true)String uuid,HttpServletResponse resp){
		log.info("获取验证码，uuid为{}",uuid);
		// 使用Hutool 来生成验证码
		LineCaptcha createLineCaptcha = CaptchaUtil.createLineCaptcha(150, 45, 4, 4);
		String code = createLineCaptcha.getCode();
		// 将验证码存储在redis里面，并且设置过期时间
		redis.opsForValue().set(AUTH_CODE_PREFIX+uuid, code,60,TimeUnit.SECONDS);
		log.info("验证码为{}",code);
		try {
			resp.getOutputStream().write(createLineCaptcha.getImageBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@ApiOperation("获取到当前的用户")
	@GetMapping("/user/info")
	public User getUser() {
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	@ApiOperation("访问主页")
	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@ApiOperation("admin 角色测试")
	@GetMapping("/admin")
	@RequiresRoles("admin")
	public String admin() {
		return "admin";
	}

	@ApiOperation("admin:add权限测试")
	@GetMapping("/admin/add")
	@RequiresPermissions("admin:add")
	public String adminAdd() {
		return "adminAdd";
	}

	@GetMapping("/root")
	@RequiresRoles("root")
	public String root() {
		return "root";
	}

	@GetMapping("/root/add")
	@RequiresPermissions("root:add")
	public String rootAdd() {
		return "rootAdd";
	}
}
