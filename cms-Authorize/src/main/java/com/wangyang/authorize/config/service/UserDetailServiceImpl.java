package com.wangyang.authorize.config.service;


import com.wangyang.authorize.pojo.dto.SpringUserDto;
import com.wangyang.authorize.utils.SecurityUtils;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.service.repository.UserRepository;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.IAttachmentService;
import com.wangyang.service.service.IRoleService;
import com.wangyang.service.service.IUserService;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Attachment;
import com.wangyang.pojo.entity.Role;
import com.wangyang.pojo.entity.User;
import com.wangyang.pojo.params.UserParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;


    @Autowired
    private IRoleService roleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IArticleService articleService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IAttachmentService attachmentService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin")){
            return new org.springframework.security.core.userdetails.User("admin","123456",
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        return findByUsername(username);
    }

    public User addUser(UserParam userParam, MultipartFile file){
        User user = new User();
        BeanUtils.copyProperties(userParam,user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(file!=null){
            Attachment attachment = attachmentService.upload(file,  user.getUsername());
            user.setAvatar(attachment.getPath());
        }
        User saveUser = userService.add(user);

        return saveUser;
    }

    public User updateUser(int id,UserParam userParam,MultipartFile file){
        User user = userService.findById(id);
        BeanUtils.copyProperties(userParam,user,"password","avatar");
        if(userParam.getPassword()!=null||!"".equals(userParam.getPassword())){
            user.setPassword(passwordEncoder.encode(userParam.getPassword()));
        }

        if(file!=null){
            Attachment attachment = attachmentService.upload(file, user.getUsername());
            user.setAvatar(attachment.getPath());
        }
        return userRepository.save(user);
    }


    public SpringUserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null){
            throw  new UsernameNotFoundException("用户名不存在!!");
        }
        SpringUserDto springUserDto = new SpringUserDto();
        BeanUtils.copyProperties(user, springUserDto);
        List<Role> roles = roleService.findByUserId(user.getId());
        if(CollectionUtils.isEmpty(roles)){
            return null;
        }
        springUserDto.setRoles(roles);
        return springUserDto;
    }


    public User delete(int id){
        User user = userService.findById(id);
        if(user.getUsername()=="admin"){
            throw  new ObjectException("管理员不能删除");
        }
        List<Article> articleList = articleService.listByUserId(id);
        if(articleList.size()!=0){
            throw  new ObjectException("用户"+user.getUsername()+"有文章"+articleList.size()+"篇不能删除！");
        }
        userRepository.delete(user);
        return user;
    }

    public UserDto findById(int id){
       return userService.findUserDaoById(id);
    }

    public SpringUserDto getCurrentUser() {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        return findByUsername(username.get());
    }

    public Optional<String> getCurrentUserName() {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        return username;
    }
}
