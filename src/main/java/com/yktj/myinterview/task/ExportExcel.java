package com.yktj.myinterview.task;

import com.yktj.myinterview.entities.Tlog;
import com.yktj.myinterview.mapper.TlogMapper;
import com.yktj.myinterview.util.JsonUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling
public class ExportExcel {

    @Value("${excelfilespath}")
    private String filespath;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    TlogMapper tlogMapper;

    @Scheduled(cron = "0 0 4 * * ?")//每天临晨4点触发 将日志操作表前一天的日志信息生成excel保存并提供下载。
//    @Scheduled(cron = "0/3 * * * * ? ")//3s执行一次，测试用
    public void myTask() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currTime = Calendar.getInstance();
        currTime.set(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE) - 1);
        // 得到前一天的日期key，格式为"yyyy-MM-dd"
        String previousDay = format.format(new Date(currTime.getTimeInMillis()));
//        System.out.println(previousDay);
        exportByKey(previousDay);
    }

    // 测试用批量导出Excel文件，key/value保存在redis。
    public void batchExport(int count) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currTime = Calendar.getInstance();
        currTime.set(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE) - count - 1);
        // 今天以前count天的所有日志记录，导出Excel保存服务器
        for (int i = 0; i < count; i++) {
            currTime.set(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE) + 1);
            String previousDay = format.format(new Date(currTime.getTimeInMillis()));
            System.out.println((i + 1) + "----------------" + previousDay);
            exportByKey(previousDay);
        }
    }

    // 执行生成Excel文件保存在服务器，并将key/value保存在redis。
    public void exportByKey(String key) {
        List<Tlog> lists = tlogMapper.getTlogByKey(key);
        // 读取配置文件中的文件路径,日期key合成文件名
        String fullname = filespath + key + "_log.xls";
        genExcel(lists, fullname);
        System.out.println("日志记录条数：" + lists.size() + ",文件路径：" + fullname);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("recordcount", lists.size());
        map.put("filepath", fullname);
        // 日期yyyy-MM-dd作为key，总数量和下载地址作为value保存在redis。
        redisTemplate.opsForValue().set(key, JsonUtils.objectToJson(map));
    }

    public void genExcel(List<Tlog> lis, String fullname) {
        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("序号");
        head.add("操作时间");
        head.add("日志类型");
        head.add("日志内容");
        head.add("日志状态");
        head.add("标记");
        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (Tlog tlog : lis) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(String.valueOf(tlog.getId()));
            bodyValue.add(tlog.getOperationTime().toString());
            bodyValue.add(tlog.getType());
            bodyValue.add(tlog.getContent());
            bodyValue.add(tlog.getLogStatus());
            bodyValue.add(tlog.getFlag());
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        HSSFWorkbook excel = ExcelUtils.expExcel(head, body);
        ExcelUtils.outFile(excel, fullname);
    }
}
