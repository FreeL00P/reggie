package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * OrdersMapper
 *
 * @author fj
 * @date 2022/10/11 18:48
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
