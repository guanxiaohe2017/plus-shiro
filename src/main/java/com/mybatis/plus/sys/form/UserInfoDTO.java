package com.mybatis.plus.sys.form;

import lombok.Data;

/**
 *
 * 描述: 用户信息传输类
 *
 * @author 官萧何
 * @date 2020/8/17 11:28
 * @version V1.0
 */
@Data
public class UserInfoDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;


}
