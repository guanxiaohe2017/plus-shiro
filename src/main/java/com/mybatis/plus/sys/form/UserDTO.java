package com.mybatis.plus.sys.form;

import lombok.Data;

import java.util.Date;

/**
 *
 * 描述: 用户传输类
 *
 * @author 官萧何
 * @date 2020/8/17 11:27
 * @version V1.0
 */
@Data
public class UserDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

   /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;


    /**
     * 用户关联业务 ID
     */
    private Long dataId;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 对应业务字段 名称
     */
    private String data;

    /**
     * 省Id
     */
    private String  provinceId;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市Id
     */
    private String cityId;
    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区、县Id
     */
    private String areaId;
    /**
     * 区、县名称
     */
    private String areaName;

  /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;



}
