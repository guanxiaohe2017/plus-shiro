

package com.mybatis.plus.sys.controller;

import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.form.SysLoginForm;
import com.mybatis.plus.sys.service.ICaptchaService;
import com.mybatis.plus.sys.service.IUserService;
import com.mybatis.plus.sys.service.IUserTokenService;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.constraint.BaseConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 *
 * 描述: 登录相关
 *
 * @author 官萧何
 * @date 2020/8/17 13:58
 * @version V1.0
 */
@RestController
@Api(tags = "用户登录相关操作")
public class LoginController extends AbstractController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserTokenService userTokenService;
	@Autowired
	private ICaptchaService captchaService;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	@ApiOperation("获得验证码")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = captchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@PostMapping("/login")
	@ApiOperation("登录")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
//		boolean captcha = captchaService.validate(form.getUuid(), form.getCaptcha());
//		if(!captcha){
//			return R.error("验证码不正确");
//		}

		//用户信息
		User user = userService.queryByUserName(form.getUsername());
		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}
		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}
		//生成token，并保存到数据库
		R r = userTokenService.createToken(user.getUserId());
		//如果用户未激活，更新并激活
		if(BaseConstants.Flag.NO.equals(user.getIsActived())){
			user.setIsActived(BaseConstants.Flag.YES);
			userService.updateById(user);
		}
		return r;
	}


	/**
	 * 退出
	 */
	@ApiOperation("登出")
	@PostMapping("/logout")
	public R logout() {
		userTokenService.logout(getUserId());
		return R.ok();
	}

}
