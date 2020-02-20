package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.Member;
import com.itcast.service.MemberService;
import com.itcast.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            if (!validateCode.equals(redisValidateCode)){
                //验证码或手机号错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //验证码正确
            Member member=memberService.findMemberBytelephone(telephone);
            if (member==null){
                //注册用户,先注册手机号,其他信息后期可以完善
                member=new Member();
                member.setFileNumber(new SimpleDateFormat("yyyyMMdd").format(new Date())+telephone.substring(8));
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                member.setName(UUID.randomUUID().toString().substring(0,15));
                memberService.add(member);
            }
            //将手机号添加到cookie中,方便追踪用户,区分用户,实际生产中cookie应该是一个可以反解密的加密字符串
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//设置cookie路径,在访问/以下的路径,cookie都会被发送
            //存活时间一个月
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //将member对象转换成json字符串格式
            String json = JSON.toJSON(member).toString();
            //将会员信息存入redis中,不存入seesion是因为集群的原因,防止多个服务器发生session共享的问题
            jedisPool.getResource().setex(telephone,60*30,json);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
