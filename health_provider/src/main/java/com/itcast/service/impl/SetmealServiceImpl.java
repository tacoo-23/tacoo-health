package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.constant.RedisConstant;
import com.itcast.dao.SetmealDao;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.Setmeal;
import com.itcast.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String path;

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) throws Exception {
        //往套餐表添加记录
        setmealDao.add(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //往套餐和检查组关联表中添加记录
        addSmIdAndCgId(setmeal.getId(),checkGroupIds);
        //生成静态页面
        makeMoblieGenerateHtml();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) throws Exception {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page=setmealDao.findPage(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());

        return pageResult;
    }

    @Override
    public List<Setmeal> findAll() throws Exception {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findSetmealById(Integer setmealId) throws Exception {
        return setmealDao.findSetmealById(setmealId);

    }

    @Override
    public List<Integer> findcheckGroupsBySid(Integer setmealId) throws Exception {
        return setmealDao.findcheckGroupsBySid(setmealId);
    }

    @Override
    public void editSetmeal(Setmeal setmeal, String oldImg) throws Exception {
        setmealDao.editSetMEAL(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,oldImg);
        //生成静态页面
        makeMoblieGenerateHtml();

    }

    public void addSmIdAndCgId(Integer sId,Integer[] ids){
        if(ids!=null){
            Map<String,Integer> map = new HashMap<String, Integer>();
            for (Integer id : ids) {
                map.put("setmealId",sId);
                map.put("checkGroupId",id);
                setmealDao.addSmIdAndCgId(map);
            }
        }
    }

    //生成静态页面
    public void makeMoblieGenerateHtml(){
        List<Setmeal> setmeals = setmealDao.findAll();
        //生成套餐列表静态页面
        makeMobileSetmealGenerateHtml(setmeals);

        //生成套餐详情静态页面
        makeMobileSetmealDetaileGenerateHtml(setmeals);

    }
    //生成套餐列表静态页面
    public void makeMobileSetmealGenerateHtml(List<Setmeal> setmeals){
        Map map = new HashMap();
        map.put("setmealList",setmeals);
        makeGenerateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }
    //生成套餐详情静态页面
    public void makeMobileSetmealDetaileGenerateHtml(List<Setmeal> setmeals){
        Map map = new HashMap();
        for (Setmeal setmeal : setmeals) {
            //详情页面 封装的setmeal必须要有多对多的关系
            map.put("setmeal",setmealDao.findSetmealById(setmeal.getId()));
            makeGenerateHtml("mobile_setmeal_detail.ftl","m_setmeal_detail_"+setmeal.getId()+".html",map);
        }

    }


    //用于生成静态页面
    public void makeGenerateHtml(String templateName,String htmlName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer writer =null;
        //加载模板文件
        try {
            Template template = configuration.getTemplate(templateName);
            writer=new FileWriter(new File(path+"/"+htmlName));
            template.process(map,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
