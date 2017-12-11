package com.lzq.adream.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.lzq.adream.utils.forview.ModelResponseBodyConverter;
import com.lzq.adream.utils.forview.StringResponseBodyConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by lzq on 2017/6/22.
 */

public final class SetterExclusionStrategys extends Converter.Factory {

    public static SetterExclusionStrategys create() {
        return new SetterExclusionStrategys();
    }

    private SetterExclusionStrategys() {
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        String name = getClassName(type);
        Log.e("ConvertFactory","getClassName" + name);
        if (!name.equals(String.class.getName() + "<java.lang.String>")) {
            return new StringResponseBodyConverter();
        }else{
            TypeAdapter<?> adapter = new Gson().getAdapter(TypeToken.get(type));
            return new ModelResponseBodyConverter<>(adapter);
        }
    }

    private static final String TYPE_CLASS_NAME_PREFIX = "class ";
    private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";

    /**
     * 根据TYPE获取类型名称
     * @param type
     * @return
     */
    public static String getClassName(Type type) {
        if (type == null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
            className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
        } else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
            className = className.substring(TYPE_INTERFACE_NAME_PREFIX.length());
        }
        return className;
    }
}
