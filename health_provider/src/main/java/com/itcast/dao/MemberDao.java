package com.itcast.dao;

import com.github.pagehelper.Page;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.Member;

public interface MemberDao {
    Member findMemberBytelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);

    Integer findTodayNewMember(String today);

    Integer findTotalMember();

    /*Integer findThisWeekNewMember(String firstDayOfWeek);

    Integer findThisMonthNewMember(String firstDay4ThisMonth);*/

    Integer findNewMember(String firstDayOfWeek);

    Page<Member> findPage(String queryString);
}
