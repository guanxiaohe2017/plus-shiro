package com.mybatis.plus.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 描述: 日期提取工具
 *
 * @author 官萧何
 * @date 2020/8/17 11:36
 * @version V1.0
 */
public class TimeWords {
    private static String regex_date;
    private static String regex_time;
    private static final String L = "(";
    private static final String R = ")";
    private static final String O = "|";

    static {
        createRegexDate();
        createRegexTime();
    }

    /***
     *  截取字符串里的日期
     * @param input
     * @return
     */
    private static String[] get(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String s[] = new String[10000];
        int len = 0;
        while (matcher.find()) {
            s[len] = matcher.group();
            len++;
        }
        return Arrays.copyOf(s, len);
    }


    private static void createRegexTime() {
        String nh = "((1[0-9])|(2[0-4])|((0|)[0-9]))";
        String duan = "(上午|下午|晚上|凌晨|早上|夜晚|am|pm|)";
        String ch = "(((二|)(十|)[零,一,二,三,四,五,六,七,八,九,十]))";
        String ce = "(点|:)";
        String cme = "(分|)";
        String t1 = "((一刻)|(半)|([0-5][0-9]))" + cme;
        String t2 = duan + orRegex(nh, ch) + ce + orRegex(t1, "");
        regex_time = t2;
    }


    private static void createRegexDate() {
        final String C09 = "([0,1,2,3,4,5,6,7,8,9,十,〇,一,二,三,四,五,六,七,八,九])";
        final String C19 = "([1,2,3,4,5,6,7,8,9,一,二,三,四,五,六,七,八,九])";
        final String C0 = "[0,十]";
        final String C1 = "[1,一]";
        final String C01 = "[0,十,1,一]";
        final String C02 = "[0,1,2,十,一,二]";
        final String C3 = "[3,三]";
        final String C12 = "[1,2,一,二]";
        final String C17 = "[1,2,3,4,5,6,7,一,二,三,四,五,六,日]";
        final String Y = C02 + C09 + C09 + C09;
        final String M = orRegex(C09, C1 + C02, orRegex(C0, "") + C19);
//        final String D = orRegex(C09, C12 + C09, C3 + C01, orRegex(C0, "") + C19, C19 + orRegex(C0, "") + C19);
        final String D = orRegex(C19 + orRegex(C0, "") + C19,orRegex(C0, "") + C19 , C3 + C01, C12 + C09, C09);
        final String W = "((周|下周|本周)(" + C17 + "|末))|((下周星期|下礼拜|礼拜|下星期|星期)(" + C17 + "|天))";
        final String M_END1 = "月";
        final String D_END1 = "(日|号)";
        final String Y_END1 = "年";
        final String E = "(-|\\.)";
        String year = orRegex(Y + Y_END1, "");
        String year_month = year + M + M_END1;
        String day = D + D_END1;
        String day_week = orRegex(day, "") + W;
        String month_day = orRegex(orRegex(M + M_END1, "") + day, M + M_END1 + D);
        String year_month_day = year_month + day;
        regex_date = orRegex(year_month_day, month_day, day_week, year_month);
        regex_date = orRegex(regex_date, Y + E + M + E + D, Y + E + M, M + E + D);
    }


    public static Map<String, String> words(String input) {
        String value = "t 1024";
        Map<String, String> map = new HashMap<>();
        put(map, get(regex_date, input), value);
        put(map, get(regex_time, input), value);
        return map;
    }

    /**
     * @author: 官昌洪
     * @Description: 获得标准时间字符串
     * @Date: 2020/4/28 15:14
     * @Param:
     * @return:
     */
    public static String obtainDateStr(String input) {
        Map<String, String> words = TimeWords.words(input);
        Set<String> strings = words.keySet();
        for (String string : strings) {
            if (string.contains("年") || string.contains("-") || string.contains(".")) {
                String s = MyUtil.handleChineseDate(string);
                return s;
            }
        }

        return null;
    }

    private static void put(Map<String, String> map, String keys[], String value) {
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], value);
        }
    }

    private static String orRegex(String... args) {
        String regex = "";
        for (int i = 0; i < args.length; i++) {
            regex = regex + O + L + args[i] + R;
        }
        return L + regex.substring(1) + R;
    }


    public static void main(String[] args) throws IOException {
        testConvertZwrq();
    }

    private static void testConvertZwrq() {
        String[] list = new String[]{"二〇一九年十一月","二○○九年四月三十日", "○九年四月三十日", "二○○九年十二月三十一日", "二零零九年十二月三十一日"};
        for (String s : list) {
            Date d = convertCnDate(s);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(d));
        }
    }

    public static Date convertCnDate(String cprq) {
        int yearPos = cprq.indexOf("年");
        int monthPos = cprq.indexOf("月");
        String cnYear = cprq.substring(0, yearPos);
        String cnMonth = cprq.substring(yearPos + 1, monthPos);

        String cnDay = null;
        try {
            cnDay = cprq.substring(monthPos + 1, cprq.length() - 1);
        } catch (Exception e) {
            cnDay = "一";
        }

        String year = ConvertCnYear(cnYear);
        String month = ConvertCnDateNumber(cnMonth);

        String day = ConvertCnDateNumber(cnDay);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return c.getTime();
    }

    private static String ConvertCnYear(String cnYear) {
        if (cnYear.length() == 2)
            return "20" + ConvertCnNumberChar(cnYear);
        else
            return ConvertCnNumberChar(cnYear);
    }

    private static String ConvertCnDateNumber(String cnNumber) {
        if (cnNumber.length() == 1) {
            if (cnNumber.equals("十"))
                return "10";
            else
                return ConvertCnNumberChar(cnNumber);
        } else if (cnNumber.length() == 2) {
            if (cnNumber.startsWith("十")) {
                return "1" + ConvertCnNumberChar(cnNumber.substring(1, 2));
            } else if (cnNumber.endsWith("十")) {
                return ConvertCnNumberChar(cnNumber.substring(0, 1)) + "0";
            } else {
                return ConvertCnNumberChar(cnNumber);
            }
        } else if (cnNumber.length() == 3) {
            return ConvertCnNumberChar(cnNumber.substring(0, 1) + cnNumber.substring(2, 3));
        }
        return null;
    }

    private static String ConvertCnNumberChar(String cnNumberStr) {
        String ALL_CN_NUMBER = "〇○零一二三四五六七八九";
        String ALL_NUMBER = "000123456789";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cnNumberStr.length(); i++) {
            char c = cnNumberStr.charAt(i);
            int index = ALL_CN_NUMBER.indexOf(c);
            if (index != -1) {
                buf.append(ALL_NUMBER.charAt(index));
            } else {
                buf.append(cnNumberStr.charAt(i));
            }
        }
        return buf.toString();
    }

}
