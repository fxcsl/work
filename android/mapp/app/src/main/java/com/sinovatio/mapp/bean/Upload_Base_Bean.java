package com.sinovatio.mapp.bean;

import java.io.Serializable;
import java.util.List;

public class Upload_Base_Bean<T> implements Serializable {

    public List<T> getJsonDate() {
        return jsonData;
    }

    public void setJsonDate(List<T> jsonDate) {
        this.jsonData = jsonDate;
    }

    private List<T> jsonData;
}
