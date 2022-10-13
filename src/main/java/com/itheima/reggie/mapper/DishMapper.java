package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * DishMapper
 *
 * @author fj
 * @date 2022/10/7 15:16
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
