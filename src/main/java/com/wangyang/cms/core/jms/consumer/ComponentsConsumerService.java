package com.wangyang.cms.core.jms.consumer;

import com.wangyang.cms.config.CmsConfig;
import com.wangyang.cms.core.jms.producer.DestinationConst;
import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.repository.TemplatePageRepository;
import com.wangyang.cms.utils.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
public class ComponentsConsumerService {

    @Autowired
    TemplatePageRepository templatePageRepository;


    @JmsListener(destination = DestinationConst.ARTICLE_SHOW_LATEST_STRING)
    public void commonTemplate(String option){
        List<Components> templatePages = templatePageRepository.findAll((Specification<Components>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.like(root.get("event"), "%"+option+"%"),
                        criteriaBuilder.isTrue(root.get("status"))
                ).getRestriction());

        templatePages.forEach(templatePage -> {
                    Object data = getData(templatePage.getDataName());
                    if(data!=null){
                        TemplateUtil.convertHtmlAndSave(data,templatePage);
                    }
                });
    }

    public Object getData(String name){
        try {
            String[] names = name.split("\\.");
            String className = names[0];
            String methodName = names[1];
            Object bean = CmsConfig.getBean(className);
            Method method = bean.getClass().getMethod(methodName);
            return method.invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
