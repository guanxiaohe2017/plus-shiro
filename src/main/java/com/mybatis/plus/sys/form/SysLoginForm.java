

package com.mybatis.plus.sys.form;

import lombok.Data;

/**
 *
 * 描述: 登录表单
 *
 * @author 官萧何
 * @date 2020/8/17 11:27
 * @version V1.0
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
