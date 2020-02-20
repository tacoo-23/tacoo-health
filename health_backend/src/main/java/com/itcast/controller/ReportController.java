package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.ReportBusinessData;
import com.itcast.pojo.ReportSetmealData;
import com.itcast.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;

    private ReportBusinessData reportBusinessData;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //获取过去12个月的月份
        Map map = new HashMap();
        List<String> list = new ArrayList<>();
        //获取的日历时间就是当前时间,若要设置日历则调用setTime方法
        try {
            Calendar calendar = Calendar.getInstance();
            //给日历设置时间
//        calendar.setTime(new Date());
            calendar.add(Calendar.MONTH,-13);
            for (int i = 0; i <12; i++) {
                calendar.add(Calendar.MONTH,+1);
                Date date = calendar.getTime();
                String monthStr = new SimpleDateFormat("yyyy.MM").format(date);
                list.add(monthStr);
            }
            map.put("months",list);
            //查询过去12个月每个月的会员总数
            List<Integer> countList = new ArrayList<>();
            for (String month : list) {
                Integer count = reportService.findMemberCountByMonth(month+".31");
                countList.add(count);
            }
            map.put("memberCount",countList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            ReportSetmealData reportSetmealData=reportService.getSetmealReport();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,reportSetmealData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
    }
    @RequestMapping("/getBusinessReport")
    public Result getBusinessReport(){
        try {
            reportBusinessData=reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportBusinessData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
    //读取模板填充好运营数据响应给客户端
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletResponse response, HttpServletRequest request){
        //该方法需要servlet-api 3.1.0的jar包才可以用,本版本使用的是2.5
//        request.getServletContext()

        //获取模板文件在服务器的项目下的绝对路径,File.separator可以识别操作系统自动生成需要的分隔符
        String path = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
        OutputStream out = null;
        XSSFWorkbook workbook=null;
        try {
            //获取运营表的数据
//            ReportBusinessData businessReport = reportService.getBusinessReport();

            //基于模板获取一个工作簿对象
            workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
           //获取第一个sheet表对象
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow dateRow = sheet.getRow(2);
            dateRow.getCell(5).setCellValue(reportBusinessData.getReportDate());
            XSSFRow memberRow1 = sheet.getRow(4);
            memberRow1.getCell(5).setCellValue(reportBusinessData.getTodayNewMember());
            memberRow1.getCell(7).setCellValue(reportBusinessData.getTotalMember());

            XSSFRow memberRow2 = sheet.getRow(5);
            memberRow2.getCell(5).setCellValue(reportBusinessData.getThisWeekNewMember());
            memberRow2.getCell(7).setCellValue(reportBusinessData.getThisMonthNewMember());

            XSSFRow orderRow1 = sheet.getRow(7);
            orderRow1.getCell(5).setCellValue(reportBusinessData.getTodayOrderNumber());
            orderRow1.getCell(7).setCellValue(reportBusinessData.getTodayVisitsNumber());
            XSSFRow orderRow2 = sheet.getRow(8);
            orderRow2.getCell(5).setCellValue(reportBusinessData.getThisWeekOrderNumber());
            orderRow2.getCell(7).setCellValue(reportBusinessData.getThisWeekVisitsNumber());
            XSSFRow orderRow3 = sheet.getRow(9);
            orderRow3.getCell(5).setCellValue(reportBusinessData.getThisMonthOrderNumber());
            orderRow3.getCell(7).setCellValue(reportBusinessData.getThisMonthVisitsNumber());
            int index = 12;
            List<HotSetmeal> hotSetmeals = reportBusinessData.getHotSetmeals();
            for (HotSetmeal hotSetmeal : hotSetmeals) {
                XSSFRow setmealRow = sheet.getRow(index++);
                setmealRow.getCell(4).setCellValue(hotSetmeal.getName());
                setmealRow.getCell(5).setCellValue(hotSetmeal.getSetmeal_count());
                setmealRow.getCell(6).setCellValue(hotSetmeal.getProportion().doubleValue());
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition","attachment;filename=report.xlsx");
            out = response.getOutputStream();
            //将文件对象写出,使用workbook中的write方法,将输出流对象传递过去即可
            workbook.write(out);
            out.flush();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }finally {
            try {
                if (out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (workbook!=null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    @RequestMapping("/exportBusinessReport4PDF")
    public void exportBusinessReport4PDF(HttpServletResponse response,HttpServletRequest request){
        OutputStream out = null;

        try {
            String jrxmlPath=request.getSession().getServletContext().getRealPath("template")+File.separator+"health_business3.jrxml";
            String jasperPath=request.getSession().getServletContext().getRealPath("template")+File.separator+"health_business3.jasper";
            //编译模板,编译成后缀为.jasper的二进制文件
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
            List<HotSetmeal> hotSetmeals = reportBusinessData.getHotSetmeals();
           /* List<Map> list = new ArrayList<>();
            for (HotSetmeal hotSetmeal : hotSetmeals) {
                Map<String, Object> map = transBean2Map(hotSetmeal);
                list.add(map);
            }*/

            //填充数据--使用JavaBean数据源模式填充,需要将javaBean对象作为参数构造JRBeanCollectionDataSource对象传递过去
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, transBean2Map(reportBusinessData), new JRBeanCollectionDataSource(hotSetmeals));
            response.setContentType("application/pdf");
            response.setHeader("content-disposition","attachment;fileName=report.pdf");
            out=response.getOutputStream();
            //通过Jasperreports封装的JasperExportManager类来写到客户端
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class") && !key.equals("pageNo") && !key.equals("pageSize")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }
}
