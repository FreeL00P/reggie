package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SetmealController
 *
 * @author fj
 * @date 2022/10/9 19:18
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name) {
        log.info("page={} pageSize={} name={}",page,pageSize,name);
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构造条件查询构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件 如果name 为空 则不添加
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        //执行查询 底层会将查询到的数据封装到pageInfo
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝 对相同属性进行拷贝 前者拷贝到后者
        BeanUtils.copyProperties(pageInfo,setmealDtoPage);

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            //将 setmealDto拷贝到item
            BeanUtils.copyProperties(item, setmealDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                //获取分类id对应的套餐名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * 添加
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("添加套餐 {}",setmealDto.toString());
        //setmealService.save(setmealDto);
        setmealService.saveWithDish(setmealDto);
        //套餐内包含的菜品信息存储到了另一张表,需要把菜品信息
        //把套餐id拷贝到
        return R.success("添加套餐成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除套餐的id列表 {}",ids.toString());
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 获取套餐下的菜品列表
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        log.info("获取套餐下的所有菜品以及口味信息 {}",setmeal);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //根据套餐id查询菜品信息
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());

        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        //得到套餐下的所有菜品信息
        List<Setmeal> setmealList= setmealService.list(queryWrapper);

        return R.success(setmealList);
    }

    /**
     * 点击修改查询信息 返回给前端
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> queryById(@PathVariable("id") Long id) {
        log.info("正在修改id= {} 的套餐信息",id);
        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);
        return  R.success(setmealDto);
    }

    /**
     * 点击保存 将修改后的套餐信息更新
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("更新套餐信息 {}",setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("更新套信息成功");
    }
}
