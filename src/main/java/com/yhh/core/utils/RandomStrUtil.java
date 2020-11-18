package com.yhh.core.utils;

import java.util.Random;

/**  
 * <p>Description:
 * 生成随机字符串：
 * 10位密码，字符可能由大写字母、小写字母、数字和特殊符号组成
 * @author yhh  
 * @date 2020年4月26日  
 */
public class RandomStrUtil {

    private static char nextChar(Random rnd) {
        switch (rnd.nextInt(3)) {
            case 0:
                return (char) ('a' + rnd.nextInt(26));
            case 1:
                return (char) ('A' + rnd.nextInt(26));
            default:
                return (char) ('0' + rnd.nextInt(10));
        }
    }

    public static String randomPassword(int size) {
        char[] chars = new char[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            switch (i) {
                case 0:
                    chars[0] = (char) ('a' + rnd.nextInt(26));
                    break;
                case 1:
                    chars[1] = (char) ('A' + rnd.nextInt(26));
                    break;
                case 2:
                    chars[2] = (char) ('0' + rnd.nextInt(10));
                    break;
                default:
                    chars[i] = nextChar(rnd);
                    break;
            }
        }
        return new String(chars);
    }

}
