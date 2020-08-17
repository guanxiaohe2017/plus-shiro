
package com.mybatis.plus.sys.controller;

import com.mybatis.plus.sys.entity.User;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 描述: controller基类
 *
 * @author 官萧何
 * @date 2020/8/17 11:26
 * @version V1.0
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected User getUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
}
