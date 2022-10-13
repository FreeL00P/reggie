package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

import javax.annotation.ManagedBean;

/**
 * AddressBookMapper
 *
 * @author fj
 * @date 2022/10/11 15:02
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
