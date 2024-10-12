package com.djz.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.djz.auth.entity.UserToRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djz
 * @since 2022-10-21
 */
public interface IUserToRoleService extends IService<UserToRole> {

    /**
     * 根据用户ID查询人员角色
     * @param userNo 用户ID
     * @return  结果
     */
    UserToRole selectByUserNo(String  userNo);

}
