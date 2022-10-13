package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper
 *
 * @author fj
 * @date 2022/10/10 19:54
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
