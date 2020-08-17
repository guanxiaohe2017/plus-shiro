

package com.mybatis.plus.utils.validator;

import com.mybatis.plus.config.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * 描述: 数据校验
 *
 * @author 官萧何
 * @date 2020/8/17 11:33
 * @version V1.0
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}
