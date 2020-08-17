

package com.mybatis.plus.utils.validator.group;

import javax.validation.GroupSequence;

/**
 *
 * 描述: 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author 官萧何
 * @date 2020/8/17 11:32
 * @version V1.0
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
