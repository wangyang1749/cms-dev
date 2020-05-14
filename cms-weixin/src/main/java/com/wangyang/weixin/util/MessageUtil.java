package com.wangyang.weixin.util;

import com.thoughtworks.xstream.XStream;
import com.wangyang.weixin.pojo.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    public static Map<String,String> xmlToMap(HttpServletRequest request){
        InputStream ins =null;
        try {
            Map<String,String> map = new HashMap<>();
            SAXReader reader = new SAXReader();
            ins = request.getInputStream();
            Document doc = reader.read(ins);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();

            for (Element element : elements){
                map.put(element.getName(),element.getText());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            if(ins!=null){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public static  String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());
        return  xStream.toXML(textMessage);

    }
}
