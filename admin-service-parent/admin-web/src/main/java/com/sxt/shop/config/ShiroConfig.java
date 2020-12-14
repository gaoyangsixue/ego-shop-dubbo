package com.sxt.shop.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;


/**
 * shiro的配置类
 * @author WHSXT-LTD
 *
 */
@Configuration
public class ShiroConfig {

	@Value("${shiro.redis}") // localhost:6379 在${:} 代表三元表达式
	private String redisHost;
	//1 SecurityManager
	@Bean
	public DefaultWebSecurityManager defaultWebSecurityManager(UserRealm realm,TokenSessionManager sessionManager,HashedCredentialsMatcher credentialsMatcher) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		// realm 设置密码验证器
		realm.setCredentialsMatcher(credentialsMatcher);
		// 设置realm
		defaultWebSecurityManager.setRealm(realm);
		// 设置sessionManager
		defaultWebSecurityManager.setSessionManager(sessionManager);
		return defaultWebSecurityManager;
	}

	/**
	 * 自定义Token的获取
	 * @return
	 */
	@Bean
	public TokenSessionManager tokenSessionManager(RedisSessionDAO sessionDAO) {
		TokenSessionManager tokenSessionManager = new TokenSessionManager();
		tokenSessionManager.setSessionDAO(sessionDAO);
		return tokenSessionManager;
	}

	/**
	 * 把登录信息放在Redis 来，在分布式环境里面，可以共享session
	 * 将shior的验证信息放在Redis
	 * 1 需要依赖第三方的jar
	 *  <!-- https://mvnrepository.com/artifact/org.crazycake/shiro-redis -->
		<dependency>
    		<groupId>org.crazycake</groupId>
    		<artifactId>shiro-redis</artifactId>
    		<version>3.2.3</version>
		</dependency>

	 * 2 新建RedisSessionDAO
	 * @return
	 */
	@Bean
	public RedisSessionDAO sessionDAO(RedisManager redisManager) {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager);
		return redisSessionDAO;
	}

	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(5);
		jedisPoolConfig.setMaxIdle(3);
		jedisPoolConfig.setMinIdle(2);
		redisManager.setJedisPoolConfig(jedisPoolConfig);
		//		127.0.0.1:6379
		redisManager.setHost(redisHost);
		return redisManager;
	}

	/**
	 * 放行和拦截
	 */
	@Bean
	public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
		defaultShiroFilterChainDefinition.addPathDefinition("/login", "anon");
//		defaultShiroFilterChainDefinition.addPathDefinition("/**", "authc");
//		测试时不需要强制登录
		defaultShiroFilterChainDefinition.addPathDefinition("/**", "anon");
		return defaultShiroFilterChainDefinition;
	}

	/**
	 * 注解的权限验证
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor  authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 密码匹配器
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher("MD5");
		hashedCredentialsMatcher.setHashIterations(2);
		return hashedCredentialsMatcher;
	}
}
