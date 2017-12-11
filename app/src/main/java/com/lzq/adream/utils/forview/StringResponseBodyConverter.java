package com.lzq.adream.utils.forview;


import com.lzq.adream.base.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by lzq on 2017/6/22.
 */

public class StringResponseBodyConverter implements Converter<ResponseBody, BaseResponse<String>> {
    @Override
    public BaseResponse<String> convert(ResponseBody value) throws IOException {

        String str = value.string();
        BaseResponse<String> response = null;
        try {
            JSONObject json = new JSONObject(str);
            response = new BaseResponse<>();
//            response.setCode(json.optString("code"));
//            response.setValue(json.optString("value"));
//            response.setMessage(json.optString("message"));
            response.setNewslist(json.optString("newslist"));

            response.setReturnStatus(true);
            //response.setHeWeather5( json.optString("HeWeather"));
            response.setSubjects(json.optString("subjects"));

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            value.close();
        }
        return response;
    }
}
