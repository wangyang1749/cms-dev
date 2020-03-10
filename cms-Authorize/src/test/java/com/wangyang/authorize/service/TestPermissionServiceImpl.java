package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.dto.PermissionDto;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestPermissionServiceImpl extends AbstractServiceTest {

    @Test
    public void test(){
        List<PermissionDto> list = permissionService.listAll();
        list.forEach(i->{
            System.out.println(i.getEnName());
        });
    }
}
