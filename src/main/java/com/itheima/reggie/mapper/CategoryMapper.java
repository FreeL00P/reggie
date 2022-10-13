package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * CategoryMapper
 *
 * @author fj
 * @date 2022/10/7 14:07
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
