package com.wangyang.pojo.dto;

import org.springframework.beans.BeanUtils;

public interface InputConverter<DOMAIN> {
    default DOMAIN convertTo() {
        try {
            Class<DOMAIN> domainClass = (Class<DOMAIN>) ReflectionUtils.getParameterizedType(InputConverter.class, this.getClass())
                    .getActualTypeArguments()[0];
            DOMAIN targetInstance = domainClass.newInstance();

            BeanUtils.copyProperties(this,domainClass);
            return targetInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
