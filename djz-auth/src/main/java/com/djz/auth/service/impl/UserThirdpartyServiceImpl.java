package com.djz.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.djz.auth.entity.UserThirdparty;
import com.djz.auth.mapper.UserThirdpartyMapper;
import com.djz.auth.service.IUserThirdpartyService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 第三方用户表 服务实现类
 * </p>
 *
 * @author djz
 * @since 2018-07-27
 */
@Service
public class UserThirdpartyServiceImpl extends ServiceImpl<UserThirdpartyMapper, UserThirdparty> implements IUserThirdpartyService {

}
