package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CategoryController
 * 分类管理
 * @author fj
 * @date 2022/10/7 14:03
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page={} pageSize={}",page,pageSize);
        Page pageInfo =new Page<>(page, pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        //添加排序条件
        queryWrapper.lambda().orderByAsc(Category ::getSort);
        //执行service层方法
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids){
        log.info("删除id={} 的信息",ids);
        categoryService.removeById(ids);
        return R.success("删除分类信息成功");
    }

    @PutMapping
    public R<String> uppdate(@RequestBody Category category, HttpServletRequest request){
//        //从session中获取登陆用户信息
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(empId);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    @PostMapping
    public R<String> save(@RequestBody Category category, HttpServletRequest request) {
        log.info("正在添加分类-->{}",category.toString());
//        //从session中获取登陆用户信息
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        category.setCreateTime(LocalDateTime.now());
//        category.setCreateUser(empId);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(empId);
        categoryService.save(category);
        return R.success("添加分类信息成功");
    }

    /**
     * 根据条件查询分页数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category ::getSort);
        queryWrapper.orderByDesc(Category ::getUpdateTime );
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
