package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * ShoppingCartService
 *
 * @author fj
 * @date 2022/10/10 20:42
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
