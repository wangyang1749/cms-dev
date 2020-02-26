package com.wangyang.cms.core.view;

import com.wangyang.cms.pojo.entity.User;
import com.wangyang.cms.utils.PdfUtil;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractPdfView {


    @Override
    protected void buildPdfDocument(Map<String, Object> model, com.lowagie.text.Document document, com.lowagie.text.pdf.PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = new Date().getTime()+"_quotation.pdf"; // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","filename=" + new String(fileName.getBytes(), "iso8859-1"));
        List<User> products = (List<User>) model.get("list");
        PdfUtil pdfUtil = new PdfUtil();
        pdfUtil.createPDF(document, writer, products);
    }
}
