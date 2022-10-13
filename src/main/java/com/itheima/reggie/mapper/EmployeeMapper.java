package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * EmployeeMapper
 *
 * @author fj
 * @date 2022/10/4 19:34
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
