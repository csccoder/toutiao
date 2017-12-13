package com.nowcoder.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);

    //未勾选自动登录时，票据默认过期时间
    private static final int LOGING_TICKET_DEFAULT_EXPIRED_TIME=1000*3600*12;//12 hour
    //勾选自动登录时，票据过期时间
    private static final int LOGING_TICKET_REMEMBER_EXPIRED_TIME=1000*3600*24*3;//3 day

    //系统允许上传的图片格式
    private static final String[] IMAGE_FILE_EXTD={"jpg","jpeg","bmp","png"};
    //图片本地保存根目录
    private static final String SEPARATER=File.separator;
    public static final String IMAGE_FILE_STORAGE_DIR=SEPARATER+"home"+ SEPARATER+"chenny"+SEPARATER+"files"+SEPARATER+"uploadImage"+SEPARATER; //linux
    //项目域名//
    public static final String TOUTIAO_DOMAIN="http://localhost:8080/";

    public static boolean isFileAllowed(String fileExt) {
        for(String ext:IMAGE_FILE_EXTD){
            if(ext.equals(fileExt)){
                return true;
            }
        }

        return false;
    }

    public static int getLoginTicketExpired(int remeber){
        return remeber==0?LOGING_TICKET_DEFAULT_EXPIRED_TIME:LOGING_TICKET_REMEMBER_EXPIRED_TIME;
    }

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return JSONObject.toJSONString(json);
    }


    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return JSONObject.toJSONString(json);
    }


    public static String getJSONString(int code, Map<String, ? extends Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return JSONObject.toJSONString(json);
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }


}
