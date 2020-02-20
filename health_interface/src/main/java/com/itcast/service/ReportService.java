package com.itcast.service;

import com.itcast.pojo.ReportBusinessData;
import com.itcast.pojo.ReportSetmealData;

public interface ReportService {
    Integer findMemberCountByMonth(String month) throws Exception;

    ReportSetmealData getSetmealReport() throws Exception;

    ReportBusinessData getBusinessReport() throws Exception;

}
