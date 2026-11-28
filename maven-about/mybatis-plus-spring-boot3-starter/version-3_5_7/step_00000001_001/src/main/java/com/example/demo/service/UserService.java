package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserCreateDTO;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;

public interface UserService extends IService<User> {
    UserVO create(UserCreateDTO dto);
}
