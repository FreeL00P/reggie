package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * DishService
 *
 * @author fj
 * @date 2022/10/7 15:17
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    //根据id查询对应的口味信息
    DishDto getByIdWithFlavor(Long id);
    //更新菜品信息
    void updateWithFlavor(DishDto dishDto);
}
