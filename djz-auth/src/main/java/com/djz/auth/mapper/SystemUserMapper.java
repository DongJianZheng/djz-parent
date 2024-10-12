package com.djz.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djz.auth.entity.SystemUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {


    //等同于编写一个普通 list 查询，mybatis-plus 自动替你分页
    List<SystemUser> selectPageByConditionUser(Page<SystemUser> page, @Param("info") String info,
                                               @Param("status") Integer [] status, @Param("startTime")String startTime, @Param("endTime")String endTime);


}
