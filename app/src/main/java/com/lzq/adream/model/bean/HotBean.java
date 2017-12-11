package com.lzq.adream.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class HotBean {
    /**
     * response : searchrecommend
     * searchKeywords : ["帽子","时尚女裙","时尚秋装","韩版外套","情女装","女鞋","韩版棉袄","韩版秋装","女士外套"]
     */

    public String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<String> getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(List<String> searchKeywords) {
        this.searchKeywords = searchKeywords;
    }


    public List<String> searchKeywords;
}
