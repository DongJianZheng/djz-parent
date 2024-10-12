package com.djz.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djz.auth.entity.SystemUser;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djz
 * @since 2022-10-21
 */
public interface IUserService extends IService<SystemUser> {


    Map<String,Object> checkMobileAndPasswd(JSONObject requestJson);

    public Map<String, Object> getLoginUserAndMenuInfo(SystemUser user);
}
