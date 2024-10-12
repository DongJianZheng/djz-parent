package com.djz.auth.service.impl;


import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djz.auth.entity.InfoToUser;
import com.djz.auth.entity.Menu;
import com.djz.auth.entity.SystemUser;
import com.djz.auth.entity.UserThirdparty;
import com.djz.auth.entity.UserToRole;
import com.djz.auth.mapper.SystemUserMapper;
import com.djz.auth.model.ThirdPartyUser;
import com.djz.auth.service.IInfoToUserService;
import com.djz.auth.service.IMenuService;
import com.djz.auth.service.IUserService;
import com.djz.auth.service.IUserThirdpartyService;
import com.djz.auth.service.IUserToRoleService;
import com.djz.base.Constant;
import com.djz.exception.BusinessException;
import com.djz.utils.ComUtil;
import com.djz.utils.JWTUtil;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class UserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements IUserService {

    @Resource
    private IInfoToUserService infoToUserService;


    @Resource
    private IUserToRoleService userToRoleService;

    @Resource
    private IMenuService menuService;


    @Override
    public Map<String, Object> checkMobileAndPasswd(JSONObject requestJson) {
        //由于 @ValidationParam注解已经验证过mobile和passWord参数，所以可以直接get使用没毛病。
        String identity = requestJson.getString("identity");
        LambdaQueryWrapper<InfoToUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(InfoToUser::getIdentityInfo, identity);
        InfoToUser infoToUser = infoToUserService.getOne(lambdaQueryWrapper);

        if(ComUtil.isEmpty(infoToUser)){
            throw new BusinessException("信息为空！");
        }
        LambdaQueryWrapper<SystemUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(SystemUser::getUserNo, infoToUser.getUserNo());
        userQueryWrapper.eq(SystemUser::getStatus, 1);

        SystemUser user = this.getOne(userQueryWrapper);
        if (ComUtil.isEmpty(user) || !BCrypt.checkpw(requestJson.getString("password"), user.getPassword())) {
            throw new BusinessException("用户不存在");
        }
        return this.getLoginUserAndMenuInfo(user);
    }
    @Override
    public Map<String, Object> getLoginUserAndMenuInfo(SystemUser user) {
        Map<String, Object> result = new HashMap<>();
        UserToRole userToRole = userToRoleService.selectByUserNo(user.getUserNo());
        user.setToken(JWTUtil.sign(user.getUserNo(), user.getPassword()));
        result.put("user",user);
        List<Menu> buttonList = new ArrayList<Menu>();
        //根据角色主键查询启用的菜单权限
        List<Menu> menuList = menuService.findMenuByRoleCode(userToRole.getRoleCode());
        List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if(buttonMenu.getMenuType() == Constant.TYPE_BUTTON){
                buttonList.add(buttonMenu);
            }
        }
        result.put("menuList",retMenuList);
        result.put("buttonList",buttonList);
        return result;
    }
}
