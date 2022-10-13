package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * UserController
 *
 * @author fj
 * @date 2022/10/10 20:01
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone =(String) map.get("phone");
        log.info("front用户登录 {}",phone);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(queryWrapper);
        session.setAttribute("user",user);
        if (user != null){
            return R.success(user);
        }
        return R.error("用户不存在");
    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        log.info("退出登录");
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
    
}
