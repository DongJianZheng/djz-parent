package com.djz.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djz.auth.entity.Menu;
import com.djz.auth.entity.Role;
import com.djz.auth.entity.RoleToMenu;
import com.djz.auth.entity.UserToRole;
import com.djz.auth.mapper.RoleMapper;
import com.djz.auth.model.RoleModel;
import com.djz.auth.service.IMenuService;
import com.djz.auth.service.IRoleService;
import com.djz.auth.service.IRoleToMenuService;
import com.djz.auth.service.IUserToRoleService;
import com.djz.base.Constant;
import com.djz.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djz
 * @since 2022-10-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {



}
