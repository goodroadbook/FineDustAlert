package com.goodroadbook.finedustsmartalert.apiserver;

import java.util.ArrayList;

public class HttpResData {

    private ArrayList<list> list;
    private String totalCount;
    private parm parm;

    public ArrayList<list> getLists() {
        return list;
    }

    public void setLists(ArrayList<list> list) {
        this.list = list;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public parm getParm() {
        return parm;
    }

    public void setParm(parm parm) {
        this.parm = parm;
    }
}
