package com.wangyang.authorize.controller.api;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.authorize.pojo.dto.SpringUserDto;
import com.wangyang.service.service.IUserService;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.entity.User;
import com.wangyang.pojo.params.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    IUserService userService;

    @GetMapping
    public Page<User> pageBy(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return userService.pageBy(pageable);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User addUser(UserParam user, @RequestPart(value = "file",required = false) MultipartFile file){
        return userDetailService.addUser(user,file);
    };

    @PostMapping(value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User updateUser(@PathVariable("id") Integer id, UserParam user,@RequestPart(value = "file",required = false) MultipartFile file){
        return userDetailService.updateUser(id,user,file);
    };

    @GetMapping("/delete/{id}")
    public User delete(@PathVariable("id") Integer id){
        return userDetailService.delete(id);
    }

    @GetMapping("/find/{id}")
    public UserDto findById(@PathVariable("id") Integer id){
        return userDetailService.findById(id);
    }

    @GetMapping("/getCurrent")
    @ResponseBody
    public SpringUserDto getCurrentUser(){
        return userDetailService.getCurrentUser();
    }
}
