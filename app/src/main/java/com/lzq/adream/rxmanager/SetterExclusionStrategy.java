package com.lzq.adream.rxmanager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


/**
 * 过滤帮助类
 *
 * @author bamboo
 *
 */
public class SetterExclusionStrategy implements ExclusionStrategy {
    private String[] fields;

    public SetterExclusionStrategy(String[] fields) {
        this.fields = fields;

    }

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }
    /**
     * 过滤字段的方法
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (fields != null) {
            for (String name : fields) {
                if (f.getName().equals(name)) {
                    /** trues 代表此字段要过滤 */
                    return true;
                }
            }
        }
        return false;
    }
}
