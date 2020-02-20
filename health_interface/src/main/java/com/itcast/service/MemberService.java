package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.Member;

public interface MemberService {
    Member findMemberBytelephone(String telephone);

    void add(Member telephone);

    PageResult findPage(QueryPageBean pageBean);
}
