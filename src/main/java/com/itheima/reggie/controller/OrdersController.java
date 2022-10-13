package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import com.mchange.lang.LongUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrdersController
 *
 * @author fj
 * @date 2022/10/11 18:55
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/page")
    public R<Page<Orders>> page(Integer page, Integer pageSize,Long number,String startDate,String endDate){
        log.info("订单分页查询第 {} 页 {} 条 订单id{}",page,pageSize,number);
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //构造条件查询构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件 如果name 为空 则不添加
        if (number != null) queryWrapper.like(Orders::getNumber,number);
        //下单时间范围
        //查询beginTime 大于等于这个时间
        if (startDate != null){
            queryWrapper.ge(Orders::getOrderTime, startDate);
        }

        //查询endTime 小于等于这个时间
        if (endDate != null){
            queryWrapper.le(Orders::getOrderTime, endDate);
        }

        if(startDate != null&&endDate != null) queryWrapper.between(Orders::getCheckoutTime,startDate,endDate);
        //执行查询 底层会将查询到的数据封装到pageInfo
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session){
        log.info("订单数据：{}",orders);
        ordersService.submit(orders,session);
        return R.success("下单成功");
    }

    /**
     * 查询历史订单
     * 不知道啥玩意了 能跑就行
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> historyOrders(String page, String pageSize, HttpSession session){
        //获取用户 用户id
        User user = (User) session.getAttribute("user");
        Long userId = user.getId();
        Page<Orders> pageInfo=new Page<>();
        Page<OrdersDto> ordersDtoPage=new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId);
        ordersService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,ordersDtoPage);
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList=null;
        ordersDtoList= records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long id = item.getId();
            Orders order= ordersService.getById(id);
            if (order != null){
                LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderDetail::getOrderId,order.getId());
                List<OrderDetail> list = orderDetailService.list(wrapper);
                ordersDto.setOrderDetails(list);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        Long ordersId = orders.getId();
        Integer status = orders.getStatus();
        Orders newOrder = ordersService.getById(ordersId);
        newOrder.setStatus(status);
        ordersService.updateById(newOrder);
        return R.success("更新订单状态成功");
    }
}
