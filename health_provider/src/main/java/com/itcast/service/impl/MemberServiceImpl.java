package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.dao.MemberDao;
import com.itcast.dao.UserDao;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.Member;
import com.itcast.service.MemberService;
import com.itcast.utils.DateUtils;
import com.itcast.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private UserDao userDao;
    @Override
    public Member findMemberBytelephone(String telephone) {
        return memberDao.findMemberBytelephone(telephone);
    }
    //注册用户
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        Page<Member> members=memberDao.findPage(pageBean.getQueryString());

        PageResult pageResult = new PageResult(members.getTotal(),members.getResult());

        return pageResult;
    }
}
