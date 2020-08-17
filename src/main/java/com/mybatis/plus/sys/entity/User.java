package com.mybatis.plus.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {

private static final long serialVersionUID=1L;

    public static final Integer STATUS_YES = 1;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

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
     * 数据ID，具体业务id
     */
    private Long dataId;

    /**
     * 名称, 具体业务名称（真实姓名）
     */
    private String dataName;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 是否激活，0：未激活，1：激活
     */
    private Integer isActived;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 微信公众号openId
     */
    private String openId;

    /**
     * 角色ID列表
     */
    @TableField(exist=false)
    private List<Long> roleIdList;


}
