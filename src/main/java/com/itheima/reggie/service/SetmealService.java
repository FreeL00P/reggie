package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * SetmealService
 *
 * @author fj
 * @date 2022/10/9 19:14
 */
public interface SetmealService extends IService<Setmeal> {
    //新增套餐时一同把菜品保存到套餐菜品表
    void saveWithDish(SetmealDto setmealDto);
    //删除套餐时，一同把菜品表内关联的菜品删除
    void deleteWithDish(List<Long> ids);
    //修改套餐时 将套餐信息查询出来 显示在前端
    SetmealDto getSetmealWithDish(Long id);

    //更新套餐
    void updateWithDish(SetmealDto setmealDto);
}

