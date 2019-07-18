package com.yktj.myinterview.task;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Excel操作工具类
 */
public class ExcelUtils {

    static final short borderpx = 1;

    /**
     * 导出excel表格
     * @param head
     * @param body
     * @return
     */
    public static HSSFWorkbook expExcel(List<String> head, List<List<String>> body) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell= null;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle, borderpx);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.createFreezePane(0,1,0,1);

        for (int i = 0; i<head.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }

        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, borderpx);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < body.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> paramList = body.get(i);
            for (int p = 0; p < paramList.size(); p++) {
                cell = row.createCell(p);
                cell.setCellValue(paramList.get(p));
                cell.setCellStyle(cellStyle2);
            }
        }
        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    /**
     * 文件输出
     * @author LiuYang
     * @param workbook 填充好的workbook
     * @param fullname 生成的文件路径名称
     */
    public static boolean outFile(HSSFWorkbook workbook,String fullname) {
//        SimpleDateFormat fdate=new SimpleDateFormat("yyyyMMdd-HH点mm分");
//        path = filespath + path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        boolean isSuccess = true;
        OutputStream os=null;
        File file = null;
        try {
            file = new File(fullname);
            String filename = file.getName();
            os = new FileOutputStream(file);
//            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));
//            os= new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        try {
            os.flush();
            os.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 设置字体样式
     * @author LiuYang
     * @param workbook 工作簿
     * @param name 字体类型
     * @param height 字体大小
     * @return HSSFFont
     */
    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }

    /**
     * 设置单元格样式
     * @author LiuYang
     * @param cellStyle 工作簿
     * @param border border样式
     */
    private static void setBorderStyle(HSSFCellStyle cellStyle, short border) {
        cellStyle.setBorderBottom(border); // 下边框
        cellStyle.setBorderLeft(border);// 左边框
        cellStyle.setBorderTop(border);// 上边框
        cellStyle.setBorderRight(border);// 右边框
    }
}