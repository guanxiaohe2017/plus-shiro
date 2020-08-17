package com.mybatis.plus.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 描述: 自定义工具类
 *
 * @author 官萧何
 * @date 2020/8/17 11:34
 * @version V1.0
 */
@Slf4j
@Component
public class MyUtil {

    private static Log logger = LogFactory.getLog(MyUtil.class);

    public static String handleChineseDate(String year) {
        try {
            if (StringUtils.isNotEmpty(year)) {
                if (year.contains("年")) {
                    if (year.contains("一") || year.contains("〇") || year.contains("0")) {
                        Date date = TimeWords.convertCnDate(year);
                        return DateUtil.format(date, "yyyy-MM-dd");
                    }
                    if (!year.contains("日")) {
                        if ("月".equals(year.substring(year.length() - 1))) {
                            DateTime parse = DateUtil.parse(year, "yyyy年MM月");
                            year = DateUtil.format(parse, "yyyy-MM-dd");
                        } else {
                            DateTime parse = DateUtil.parse(year, "yyyy年MM月dd");
                            year = DateUtil.format(parse, "yyyy-MM-dd");
                        }
                    } else {
                        DateTime parse = DateUtil.parse(year, "yyyy年MM月dd日");
                        year = DateUtil.format(parse, "yyyy-MM-dd");
                    }
                } else if (year.contains("-")) {
                    year = year;
                } else if (year.contains("/")) {
                    DateTime parse = DateUtil.parse(year, "yyyy/MM/dd");
                    year = DateUtil.format(parse, "yyyy-MM-dd");
                } else if (year.contains(".")) {
                    DateTime parse = DateUtil.parse(year, "yyyy.MM.dd");
                    year = DateUtil.format(parse, "yyyy-MM-dd");
                }

                String[] split = year.split("-");
                if (split[1].length() < 2) {
                    year = split[0] + "-" + "0" + split[1] + "-" + split[2];
                }

                String[] split1 = year.split("-");
                if (split1[2].length() < 2) {
                    year = split1[0] + "-" + split1[1] + "-" + "0" + split1[2];
                }
            }
        } catch (Exception e) {
            Console.log(year+" 日期转换出错，设置日期为空====");
            year = "";
        }
        return year;
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean stringHasEndWith(String s) {
        return s.endsWith(".rar") || s.endsWith(".doc")
                || s.endsWith(".xls")
                || s.endsWith(".docx")
                || s.endsWith(".pdf")
                || s.endsWith(".tif")
                || s.endsWith(".html")
                || s.endsWith(".7z");
    }

    public static Date parseDateStr(String dateStr){
        DateTime dateTime = null;
        try {
            dateTime = DateUtil.parseDate(dateStr);
        } catch (Exception e) {
            Console.log("日期转换出错，设置日期为空====");
        }
        return dateTime;
    }



    public static String handleStrBlank(String str){
        str = str.replace(" ", "");
        str = str.replace("　", "");
        return str;
    }


    public static void removeEsItem(String type, String id) {
        String s = HttpUtil.get("http://wx.csbwbd.com/BwbdType/deleteByIdAndType?id=" + id + "&type=" + type);
        log.info("同步删除检索库项：类型：" + type + " id:" + id);
        log.info("处理结果" + s);
    }

    public static void removeEsItems(String type,List<Long> deleteIds) {
        for (Long deleteId : deleteIds) {
            removeEsItem(type, deleteId.toString());
        }
    }

    public static String dateConvertString(Date date){
        if (null == date) {
            return null;
        }
       return DateUtil.format(date, "yyyy-MM-dd");
    }

    public static Date stringConvertDate(String dateStr){
        if (StringUtils.isNotEmpty(dateStr)) {
            String s = MyUtil.handleChineseDate(dateStr);
            return DateUtil.parse(s);
        }
        return null;
    }
}
