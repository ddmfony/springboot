package com.yktj.myinterview.controller;


import com.yktj.myinterview.entities.Tlog;
import com.yktj.myinterview.entities.Tloglist;
import com.yktj.myinterview.mapper.TlogMapper;
import com.yktj.myinterview.task.ExportExcel;
import com.yktj.myinterview.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class TlogController {

    @Autowired(required = false)
    TlogMapper tlogMapper;

    @Autowired
    private StringRedisTemplate redisTemplate; // Redis操作工具类
    @Autowired
    ExportExcel exportExcel; // 生成Excel文件工具类

    /**
     * 查询所有Redis记录返回列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        Set<String> keys = redisTemplate.keys("*");
        // 按日期大小排序
        SortedSet ssKeys=new TreeSet();
        for (String key : keys){
            ssKeys.add(key);
        }

        Collection<Tloglist> list = new ArrayList<Tloglist>();
        for (Object key : ((TreeSet) ssKeys).descendingSet()) { // 倒序
            String json = redisTemplate.opsForValue().get(key.toString());
            Tloglist tlog = JsonUtils.jsonToPoJo(json, Tloglist.class);
            tlog.setKey(key.toString());
            list.add(tlog);
        }

        //放在请求域中
        model.addAttribute("tlogs", list);
        // 只要把HTML页面放在classpath:/templates/，thymeleaf就能自动渲染
        return "list";
    }

    /**
     * 根据key值，读取redis中保存的对应文件所在服务器文件夹路径，提供下载
     *
     * @param response
     * @param key
     */
    @ResponseBody
    @GetMapping(value = "/tlog/{key}")
    public void download(HttpServletResponse response, @PathVariable("key") String key) {
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
//            System.out.println(key);
            String json = redisTemplate.opsForValue().get(key);
            Tloglist tlog = JsonUtils.jsonToPoJo(json, Tloglist.class);
            String filepath = tlog.getFilepath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);

            // 避免文件名中文乱码
            filename = new String(filename.getBytes("utf-8"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            os = response.getOutputStream();

            File file = new File(filepath);
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * // 测试用批量导出Excel文件，参数count表示“今天以前的天数”
     *
     * @param count
     * @return
     */
    @ResponseBody
    @GetMapping("/batch/{count}")
    public String batch(@PathVariable("count") Integer count) {
        exportExcel.batchExport(count);
        return "Done";
    }

//    @GetMapping("/tlog/{id}")
//    public Tlog getTlog(@PathVariable("id") Integer id){
//        return tlogMapper.getTlogById(id);
//    }

    /**
     * 测试用
     *
     * @param key
     * @return
     */
    @ResponseBody
    @GetMapping("/tlogs/{key}")
    public List<Tlog> getTlogByKey(@PathVariable("key") String key) {
        return tlogMapper.getTlogByKey(key);
    }

}
