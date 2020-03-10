package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.pojo.dto.PermissionDto;
import com.wangyang.authorize.pojo.entity.Permission;
import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.authorize.pojo.entity.RolePermission;
import com.wangyang.authorize.repository.PermissionRepository;
import com.wangyang.authorize.repository.RolePermissionRepository;
import com.wangyang.authorize.repository.RoleRepository;
import com.wangyang.authorize.service.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "permission_cache")
@Slf4j
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    public List<Permission> findByUserId(int id) {
        return  permissionRepository.findByUserId(id);
    }

    @Override
    @Cacheable
    public List<PermissionDto> listAll() {
        log.info("start 查找permission的role");
        List<Permission> permissions = permissionRepository.findAll();
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        Map<Integer, Role> map = convertToMap(roles, Role::getId);
        Map<Integer,List<Role>> roleMap = new HashMap<>();
        rolePermissionList.forEach(rolePermission -> {
            roleMap.computeIfAbsent(rolePermission.getPermissionId()
                    ,roleId->new LinkedList<>())
                    .add(map.get(rolePermission.getRoleId()));
        });
        List<PermissionDto> permissionDtoList = permissions.stream().map(permission -> {
            PermissionDto permissionDto = new PermissionDto();
            BeanUtils.copyProperties(permission, permissionDto);
            permissionDto.setRoles(roleMap.get(permission.getId()));
            return permissionDto;
        }).collect(Collectors.toList());
        log.info("end 查找permission的role");
        return permissionDtoList;
    }

    @NonNull
    public static <ID, D> Map<ID, D> convertToMap(Collection<D> list, Function<D, ID> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<ID, D> resultMap = new HashMap<>();

        list.forEach(data -> resultMap.putIfAbsent(mappingFunction.apply(data), data));

        return resultMap;
    }
}
