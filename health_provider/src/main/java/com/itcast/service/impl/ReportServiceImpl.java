package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.dao.MemberDao;
import com.itcast.dao.OrderDao;
import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.ReportBusinessData;
import com.itcast.pojo.ReportSetmealData;
import com.itcast.pojo.SetmealData;
import com.itcast.service.ReportService;
import com.itcast.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Integer findMemberCountByMonth(String month) {
        return memberDao.findMemberCountByMonth(month);
    }

    @Override
    public ReportSetmealData getSetmealReport() throws Exception {
        List<SetmealData> setmealDataList=orderDao.getSetmealData();
        List<String> setmealNames = new ArrayList<String>();
        if (setmealDataList!=null&&setmealDataList.size()>0){
            for (SetmealData setmealData : setmealDataList) {
                setmealNames.add(setmealData.getName());
            }
        }
        ReportSetmealData reportSetmealData = new ReportSetmealData();
        reportSetmealData.setSetmealDatas(setmealDataList);
        reportSetmealData.setSetmealNames(setmealNames);

        return reportSetmealData;
    }

    @Override
    public ReportBusinessData getBusinessReport() throws Exception {
        ReportBusinessData reportBusinessData = new ReportBusinessData();
        String today = DateUtils.parseDate2String(new Date());

        Integer todayNewMember = memberDao.findTodayNewMember(today);
        Integer totalMember = memberDao.findTotalMember();
        String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(new Date()));
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());


        Integer thisWeekNewMember = memberDao.findNewMember(firstDayOfWeek);
        Integer thisMonthNewMember = memberDao.findNewMember(firstDay4ThisMonth);
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
        Integer thisWeekOrderNumber = orderDao.findOrderNumberAfterDate(firstDayOfWeek);
        Integer thisWeekVisitsNumber = orderDao.findVisitsNumberAfterDate(firstDayOfWeek);
        Integer thisMonthOrderNumber = orderDao.findOrderNumberAfterDate(firstDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsNumberAfterDate(firstDay4ThisMonth);
        List<HotSetmeal> hotSetmeals = orderDao.findHotSetmeals();

        reportBusinessData.setReportDate(today);
        reportBusinessData.setTodayNewMember(todayNewMember);
        reportBusinessData.setTotalMember(totalMember);
        reportBusinessData.setThisWeekNewMember(thisWeekNewMember);
        reportBusinessData.setThisMonthNewMember(thisMonthNewMember);
        reportBusinessData.setTodayOrderNumber(todayOrderNumber);
        reportBusinessData.setTodayVisitsNumber(todayVisitsNumber);
        reportBusinessData.setThisWeekOrderNumber(thisWeekOrderNumber);
        reportBusinessData.setThisWeekVisitsNumber(thisWeekVisitsNumber);
        reportBusinessData.setThisMonthOrderNumber(thisMonthOrderNumber);
        reportBusinessData.setThisMonthVisitsNumber(thisMonthVisitsNumber);
        reportBusinessData.setHotSetmeals(hotSetmeals);

        return reportBusinessData;
    }
}
