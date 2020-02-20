package com.itcast.pojo;

import java.io.Serializable;
import java.util.List;

public class ReportSetmealData implements Serializable {

    private List<String> setmealNames;
    private List<SetmealData> setmealDatas;

    public List<String> getSetmealNames() {
        return setmealNames;
    }

    public void setSetmealNames(List<String> setmealNames) {
        this.setmealNames = setmealNames;
    }

    public List<SetmealData> getSetmealDatas() {
        return setmealDatas;
    }

    public void setSetmealDatas(List<SetmealData> setmealDatas) {
        this.setmealDatas = setmealDatas;
    }
}
