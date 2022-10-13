package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jnlp.ServiceManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SetmealServiceImpl
 *
 * @author fj
 * @date 2022/10/9 19:16
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional//开启事务
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        //获取套餐id
        Long id = setmealDto.getId();
        //获取套餐包含菜品列表信息
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        dishList=dishList.stream().map((item)->{
            //将套餐id赋值给每一个菜品
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        //保存套餐内的菜品信息到套餐菜品表
        setmealDishService.saveBatch(dishList);
    }
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        this.removeByIds(ids);//删除套餐表
        //删除套餐菜品表相关菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper);
    }

    @Override
    public SetmealDto getSetmealWithDish(Long id) {
        //根据id获取套餐信息 此时菜品信息还只是id
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询套餐内的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐信息
        this.updateById(setmealDto);
        //先清除套餐内的菜品信息 在将修改后端结果提交
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        //根据套餐id删除
        queryWrapper.eq(SetmealDish ::getSetmealId,setmealDto.getId());
        //执行删除
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        //为菜品列表添加对应的套餐id
        setmealDishList.stream().map(item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
}
