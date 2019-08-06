package com.jeerigger.activiti.utils;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/4/28 0028 上午 9:31
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.security.MessageDigest;

/**
 * @ClassName: MD5Util
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/4/28 0028 上午 9:31
 * @version: V1.0
 */
public class MD5Util {

    public static  void main(String[] args){
        String str = "李四同意20190506";
        String stringMD5 = getStringMD5(str);
        System.out.println(stringMD5);
    }

    /**
     * 对字符串进行MD5加密
     * @param s
     * @return
     */
    public final static String getStringMD5(String s) {
        byte[] strTemp = s.getBytes();
        return MD5Util.getByteArrayMD5(strTemp);
    }

    /**
     * 对byte数组进行MD5加密
     * @param source
     * @return
     */
    public  static String getByteArrayMD5(byte[] source) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                // (int)b双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
