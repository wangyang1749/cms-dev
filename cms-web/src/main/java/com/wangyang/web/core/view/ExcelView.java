package com.wangyang.web.core.view;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

public class ExcelView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, org.apache.poi.ss.usermodel.Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String filename = "students.xlsx";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "inline; filename=" + filename);

        //根据工作簿创建Excel表
        Sheet sheet = workbook.createSheet("sheet1");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("password");

        if (model == null) return;
//        List<User> list = (List<User>) model.get("list");
//
//        for (int i = 0; i < list.size(); i++) {
//            User student = list.get(i);
//
//            Row tempRow = sheet.createRow(i+1);
//            tempRow.createCell(0).setCellValue(student.getId());
//            tempRow.createCell(1).setCellValue(student.getUsername());
//            tempRow.createCell(2).setCellValue(student.getPassword());
//        }

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
