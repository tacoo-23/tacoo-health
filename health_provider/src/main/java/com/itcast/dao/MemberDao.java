package com.itcast.dao;

import com.itcast.pojo.Member;

public interface MemberDao {
    Member findMemberByIdCard(String idCard);

    void add(Member member);
}
