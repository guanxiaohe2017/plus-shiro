

package com.mybatis.plus.utils;

import java.util.HashMap;


/**
 *
 * 描述: Map工具类
 *
 * @author 官萧何
 * @date 2020/8/17 11:34
 * @version V1.0
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
