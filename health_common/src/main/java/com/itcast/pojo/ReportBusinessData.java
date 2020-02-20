package com.itcast.pojo;

import java.io.Serializable;
import java.util.List;

public class ReportBusinessData implements Serializable {
    private String reportDate;
    private Integer todayNewMember;
    private Integer totalMember;
    private Integer thisWeekNewMember;
    private Integer thisMonthNewMember;
    private Integer todayOrderNumber;
    private Integer todayVisitsNumber;
    private Integer thisWeekOrderNumber;
    private Integer thisWeekVisitsNumber;
    private Integer thisMonthOrderNumber;
    private Integer thisMonthVisitsNumber;
    private List<HotSetmeal> hotSetmeals;


    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getTodayNewMember() {
        return todayNewMember;
    }

    public void setTodayNewMember(Integer todayNewMember) {
        this.todayNewMember = todayNewMember;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Integer getThisWeekNewMember() {
        return thisWeekNewMember;
    }

    public void setThisWeekNewMember(Integer thisWeekNewMember) {
        this.thisWeekNewMember = thisWeekNewMember;
    }

    public Integer getThisMonthNewMember() {
        return thisMonthNewMember;
    }

    public void setThisMonthNewMember(Integer thisMonthNewMember) {
        this.thisMonthNewMember = thisMonthNewMember;
    }

    public Integer getTodayOrderNumber() {
        return todayOrderNumber;
    }

    public void setTodayOrderNumber(Integer todayOrderNumber) {
        this.todayOrderNumber = todayOrderNumber;
    }

    public Integer getTodayVisitsNumber() {
        return todayVisitsNumber;
    }

    public void setTodayVisitsNumber(Integer todayVisitsNumber) {
        this.todayVisitsNumber = todayVisitsNumber;
    }

    public Integer getThisWeekOrderNumber() {
        return thisWeekOrderNumber;
    }

    public void setThisWeekOrderNumber(Integer thisWeekOrderNumber) {
        this.thisWeekOrderNumber = thisWeekOrderNumber;
    }

    public Integer getThisWeekVisitsNumber() {
        return thisWeekVisitsNumber;
    }

    public void setThisWeekVisitsNumber(Integer thisWeekVisitsNumber) {
        this.thisWeekVisitsNumber = thisWeekVisitsNumber;
    }

    public Integer getThisMonthOrderNumber() {
        return thisMonthOrderNumber;
    }

    public void setThisMonthOrderNumber(Integer thisMonthOrderNumber) {
        this.thisMonthOrderNumber = thisMonthOrderNumber;
    }

    public Integer getThisMonthVisitsNumber() {
        return thisMonthVisitsNumber;
    }

    public void setThisMonthVisitsNumber(Integer thisMonthVisitsNumber) {
        this.thisMonthVisitsNumber = thisMonthVisitsNumber;
    }

    public List<HotSetmeal> getHotSetmeals() {
        return hotSetmeals;
    }

    public void setHotSetmeals(List<HotSetmeal> hotSetmeals) {
        this.hotSetmeals = hotSetmeals;
    }
}
