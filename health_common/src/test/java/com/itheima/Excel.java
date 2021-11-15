package com.itheima;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;


import java.io.*;

/**
 * @Auther:liyang
 * @Date:2021/11/15 - 11 -15 -10:29
 * @Description:com.itheima
 * @Version:1.0
 */

public class Excel {
    @Test
    public void test1() throws IOException {
        //从本地计算机中读取excel表格的内容
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\23844\\Desktop\\day05.xlsx")));
        //拿是那张表
        XSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);
        //读的话，分为遍历行和列,行的索引是从零开始的
//         第一种自己遍历的方式
        int lastRowNum = sheet1.getLastRowNum();
        System.out.println(sheet1.getFirstRowNum());
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet1.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                String stringCellValue = row.getCell(j).getStringCellValue();
                System.out.print(stringCellValue + " ");
            }
            System.out.println();
        }
        ////遍历工作表获得行对象
      /*  //直接通过加强for的方式来遍历
        for (Row row : sheet1) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获得单元格中的值
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
*/
        xssfWorkbook.close();
    }

    //    写
    @Test
    public void Test2() throws IOException {
        //从内存中创建
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建表
        XSSFSheet sheet = xssfWorkbook.createSheet("传智播客");
        //创建行
        XSSFRow row = sheet.createRow(0);
        //创建列
//
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("01");
        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue(22);

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("02");
        row2.createCell(1).setCellValue("李四");
        row2.createCell(2).setCellValue(23);
//    向文件中写入数据
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/工作表.xlsx"));
        xssfWorkbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        xssfWorkbook.close();

    }

/*    private void createRow(String id, String name, String age) {
        row.createCell(1).setCellValue("编号");
        row.createCell(2).setCellValue("姓名");
        row.createCell(3).setCellValue("年龄");
    }*/
}
