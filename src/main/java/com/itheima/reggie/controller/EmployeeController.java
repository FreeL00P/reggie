package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * EmployeeController
 *
 * @author fj
 * @date 2022/10/4 19:41
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping ("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("进入到login");
        //将页面提交的password进行MD5加密处理
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交用户名username查询用户,查询不到返回失败结果
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp==null){
            return R.error("登陆失败");
        }
        //密码比对，如果不一致返回失败结果
        if (!emp.getPassword().equals(password)) {//该password为前端发送的password
            return R.error("密码错误");
        }
        //查看原工账户状态，如果已禁用，返回已禁用结果 0-->禁用 1-->可用
        if (emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //登陆成功,将员工id保存到session，返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //session中清楚登陆用户
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //添加员工信息
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        System.out.println("employee = " + employee.toString());
        //为添加的员工账号给一个初始密码 用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        //添加创建时间信息
//        employee.setCreateTime(LocalDateTime.now());
//        //添加更新时间信息
//        employee.setUpdateTime(LocalDateTime.now());
//        //因为在session中获取的信息 默认为object类型 需要进行强转 这里获取到的是empID
//        Long empId=(Long) request.getSession().getAttribute("employee");
//        //添加的创建者信息
//        employee.setCreateUser(empId);
//        //更新用户
//        employee.setUpdateUser(empId);
        //调用service保存到数据库
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页-模糊查询
     * @param page 页数
     * @param pageSize 每页显示条数
     * @param name 模糊查询条件
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){
        log.info("page={} pageSize={} name={}",page,pageSize,name);
        //构造分页构造器
        Page<Employee> pageInfo = new Page(page, pageSize);
        //构造条件查询构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件 如果name 为空 则不添加
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //执行查询 底层会将查询到的数据封装到pageInfo
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
//        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        log.info("查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
    
}
