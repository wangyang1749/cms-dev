package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.repository.UserRepository;
import com.wangyang.authorize.service.IRoleService;
import com.wangyang.authorize.service.IUserService;
import com.wangyang.authorize.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    IRoleService roleService;

    static  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null){
            throw  new UsernameNotFoundException("用户名不存在!!");
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        List<Role> roles = roleService.findByUserId(user.getId());
        if(CollectionUtils.isEmpty(roles)){
            return null;
        }
        userDto.setRoles(roles);
        return  userDto;
    }


    @Override
    public User add(User user){
        User saveUser = userRepository.save(user);
        return saveUser;
    }
    @Override
    public User findById(int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return  null;
    }

    @Override
    public UserDto getCurrentUser() {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        return findByUsername(username.get());
    }

    @Override
    public List<User> findAllById(Collection<Integer> ids){
        return userRepository.findAllById(ids);
    }
}
