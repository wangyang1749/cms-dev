package com.wangyang.service.service;

import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IUserService {


    Page<User> pageBy(Pageable pageable);

    User add(User user);

    User findById(int id);


    UserDto findUserDaoById(int id);

    Optional<User> findOptionalBy(int userId);


    List<User> findAllById(Collection<Integer> ids);
}
