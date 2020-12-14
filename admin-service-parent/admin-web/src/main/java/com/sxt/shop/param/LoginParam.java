package com.sxt.shop.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginParam {
	
	@ApiModelProperty("用户密码")
	private String credentials;
	@ApiModelProperty("用户名称")
	private String principal;
	@ApiModelProperty("uuid")
	private String sessionUUID;
	@ApiModelProperty("验证码")
	private String imageCode;
	
}
