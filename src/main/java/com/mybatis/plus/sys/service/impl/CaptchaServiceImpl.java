package com.mybatis.plus.sys.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.sys.entity.Captcha;
import com.mybatis.plus.sys.mapper.CaptchaMapper;
import com.mybatis.plus.sys.service.ICaptchaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * <p>
 * 系统验证码 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha> implements ICaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
//        String code = producer.createText();
        String code = String.valueOf(RandomUtil.randomInt(1000, 9999));


        Captcha captchaEntity = new Captcha();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.saveOrUpdate(captchaEntity);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        Captcha captchaEntity = this.getOne(new QueryWrapper<Captcha>().eq("uuid", uuid));
        if (captchaEntity == null) {
            return false;
        }

        //删除验证码
        this.removeById(uuid);

        if (captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()) {
            return true;
        }

        return false;
    }
}
