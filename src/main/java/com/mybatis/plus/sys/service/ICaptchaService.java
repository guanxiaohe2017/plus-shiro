package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.Captcha;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.image.BufferedImage;

/**
 * <p>
 * 系统验证码 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface ICaptchaService extends IService<Captcha> {
    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
