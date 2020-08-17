

package com.mybatis.plus.sys.form;

import lombok.Data;

/**
 *
 * 描述: 密码表单
 *
 * @author 官萧何
 * @date 2020/8/17 11:26
 * @version V1.0
 */
@Data
public class PasswordForm {
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

    private String code;

    private String mobile;

}
