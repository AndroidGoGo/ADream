package com.lzq.adream.utils;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by YI on 2017/2/22.
 */

public class CommonInterfaceReqUtils {
//    private static final String AND = "&";
//    private static final String EQUAL = "=";
//    private static final String QUESTION = "?";
//    private static final void mergeKeyValue(StringBuilder sb, String key, String value, boolean... formats) throws Exception {
//        if (sb == null || key == null || value == null) return;
//        boolean format = true;
//        if (formats != null && formats.length > 0) {
//            format = formats[0];
//        }
//        if (format) {
//            sb.append(key).append(EQUAL).append(URLEncoder.encode(value, "UTF-8"));
//        } else {
//            sb.append(key).append(EQUAL).append(value);
//        }
//    }

//    //地址栏参数拼接
//    public static final String requestUrlMerge(String url, JSONObject params) {
//        if (params == null) return url;
//        try {
//            StringBuilder sb = new StringBuilder(url);
//            List<String> keys = new ArrayList<>(params.keySet());
//            for (int i = 0; i < keys.size(); i++) {
//                String key = keys.get(i);
//                if (params.getString(key) != null) {
//                    sb.append(i == 0 ? QUESTION : AND);
//                    mergeKeyValue(sb, key, params.getString(key));
//                }
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static final String requestParametersMerge(JsonObject params) {
//        if (params == null) return null;
//        try {
//            StringBuilder sb = new StringBuilder();
//            int i=0;
//            Iterator<Map.Entry<String,JsonElement>> iterator=params.entrySet().iterator();
//            while (iterator.hasNext()){
//                Map.Entry<String,JsonElement> entry =iterator.next();
//                JsonElement jsonElement = entry.getValue();
//                String key = entry.getKey();
//                if (i > 0) {
//                    sb.append(AND);
//                }
//                mergeKeyValue(sb, key, jsonElement.toString(),false);
//                i++;
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private static final String sign(String secretKey, JsonObject params) {
//        if (params == null) return null;
//        StringBuilder sb = new StringBuilder();
//        Iterator<Map.Entry<String,JsonElement>> iterator=params.entrySet().iterator();
//        List<String> keys = new ArrayList<>();
//        while (iterator.hasNext()) {
//            Map.Entry<String,JsonElement> entry =iterator.next();
//            String key = entry.getKey();
//            keys.add(key);
//        }
//        Collections.sort(keys);
//
//        for(int i=0;i<keys.size();i++){
//            String key = keys.get(i);
//            sb.append(key);
//            String value=params.get(key).getAsString();
//            Log.e("加密","value----"+value);
////            if(value.startsWith("\"")&&value.endsWith("\"")){
////                value=value.substring(1,value.length()-1);
////            }
//            sb.append(value);
//        }
////        while (iterator.hasNext()) {
////            Map.Entry<String,JsonElement> entry =iterator.next();
////            JsonElement jsonElement = entry.getValue();
////            String key = entry.getKey();
////            sb.append(key);
////            String value=jsonElement.toString();
////            sb.append(value);
////        }
//        Log.e("加密","加密字符串----"+secretKey + sb.toString());
//        return MD5(secretKey + sb.toString()).toUpperCase();
//    }

    public static final String MD5(String str) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(str.getBytes("UTF-8"));
            BigInteger bigInt = new BigInteger(1, m.digest());
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static final String initRequestParameters(JsonObject params) {
//        if(params==null)return null;
//        params.addProperty("signtime",System.currentTimeMillis() / 1000);
//        params.addProperty("appId","eb86f42f6504bfefe069e85a204c9734");
//        String sign=sign("261ad12f08f13811298e2b50f803deab",params);
//        Log.e("加密","加密后-----"+sign);
//        params.addProperty("signature",sign);
////        return requestParametersMerge(params);
//        return params.toString();
//    }

    public static final String initRequestParameters(JSONObject params) throws JSONException {
        if (params == null) return null;
        params = getValueEncoded(params);
      /*  params.put("appId", "eb86f42f6504bfefe069e85a204c9734");
        params.put("signtime", System.currentTimeMillis() / 1000 + "");*/

        params.put("key","2313c234bbe749dfafe4420509555e38");
     //  String sign = sign("261ad12f08f13811298e2b50f803deab", params);

      //  params.put("signature", sign);
//        return requestParametersMerge(params);
        Log.e("加密", "加密后-----" + params.toString());
        return params.toString();
    }

    public static final Map<String,String> initRequestParameters(String request) throws JSONException {
        if(TextUtils.isEmpty(request)) return null;
        Map<String,String> map=new ArrayMap<>();

        JSONObject params=new JSONObject(request);
        Iterator<String> s = params.keys();
        while (s.hasNext()) {
            String key =s.next();
            map.put(key,params.getString(key));
        }

        Log.e("加密", "加密后-----" + map.toString());
        return map;
    }

    private static final String sign(String secretKey, Map<String,String> params) throws JSONException {
        if (params == null) return null;
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>();
        for(String key:params.keySet()){
            keys.add(key);
        }
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            sb.append(key);
            sb.append(params.get(key));
        }
        String signString = secretKey + sb.toString();
        Log.e("加密", "签名字段-----" + signString);
        return MD5(signString).toUpperCase();
    }


    //由于okhttp header 中的 value 不支持 null, \n 和 中文这样的特殊字符,所以这里
    //会首先替换 \n ,然后使用 okhttp 的校验方式,校验不通过的话,就返回 encode 后的字符串
    private static JSONObject getValueEncoded(JSONObject params) throws JSONException{
        Iterator<String> s = params.keys();
        while (s.hasNext()) {
            String key = s.next();
            String value = params.optString(key);
            String newValue = value.replace("\n", "");
            StringBuilder sb = new StringBuilder();
            try {
                if (newValue.startsWith("{") || newValue.startsWith("[")) {
                    sb.append(URLEncoder.encode(String.valueOf(newValue), "UTF-8"));
                } else {
                    for (int i = 0, length = newValue.length(); i < length; i++) {
                        char c = newValue.charAt(i);
                        if (c <= '\u001f' || c >= '\u007f') {
                            sb.append(URLEncoder.encode(String.valueOf(c), "UTF-8"));
                        } else {
                            sb.append(c);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(key,sb.toString());
        }
        return params;
    }

    private static final String sign(String secretKey, JSONObject params) throws JSONException {
        if (params == null) return null;
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>();
        Iterator<String> s = params.keys();
        while (s.hasNext()) {
            keys.add(s.next());
        }
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            sb.append(key);
            sb.append(params.getString(key));
        }
        String signString = secretKey + sb.toString();
        Log.e("加密", "签名字段-----" + signString);
        return MD5(signString).toUpperCase();
    }


}
