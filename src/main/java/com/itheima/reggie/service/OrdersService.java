package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

import javax.servlet.http.HttpSession;

/**
 * OrderService
 *
 * @author fj
 * @date 2022/10/11 18:50
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders, HttpSession session);
}
