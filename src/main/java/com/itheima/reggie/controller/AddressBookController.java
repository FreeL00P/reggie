package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * AddressBookController
 *
 * @author fj
 * @date 2022/10/11 15:04
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 获取默认地址
     * @param session
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(HttpSession session){
        //获取当前用户 在通user_id查询默认地址
        User user = (User)session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user.getId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook address = addressBookService.getOne(queryWrapper);
        return R.success(address);
    }

    /**
     * 获取地址列表
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request){
        //获取当前用户 在通user_id查询默认地址
        User user = (User)request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user.getId());
        List<AddressBook> addressList = addressBookService.list(queryWrapper);
        return R.success(addressList);
    }

    @PutMapping("/default")
    public R<String> setDefaultAddress(@RequestBody AddressBook address,HttpSession session){
        User user = (User)session.getAttribute("user");
        //将原先的默认地址 设置为普通
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook defaultAddress = addressBookService.getOne(wrapper);
        defaultAddress.setIsDefault(0);
        addressBookService.updateById(defaultAddress);
        address.setUpdateUser(user.getId());
        address.setIsDefault(1);
        addressBookService.updateById(address);
        return R.success("修改默认地址成功");
    }

    /**
     * 添加收货地址
     * @param address
     * @param session
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody AddressBook address,HttpSession session){
        User user = (User)session.getAttribute("user");
        address.setUserId(user.getId());
        address.setUpdateUser(user.getId());
        addressBookService.save(address);
        return R.success("添加收货地址成功");
    }

    /**
     * 点击修改按钮 传回数据给前端
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> queryById(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook address,HttpSession session){
        User user = (User)session.getAttribute("user");
        address.setUpdateUser(user.getId());
        addressBookService.updateById(address);
        return R.success("更新收货地址成功");

    }
}
