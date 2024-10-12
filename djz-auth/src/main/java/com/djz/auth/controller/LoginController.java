package com.djz.auth.controller;


import com.alibaba.fastjson.JSONObject;
import com.djz.annotation.AccessLimit;
import com.djz.annotation.ValidationParam;
import com.djz.auth.annotation.Pass;
import com.djz.auth.service.IUserService;
import com.djz.response.ResultBean;
import com.djz.response.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *  登录接口
 * @author djz
 */
@RestController
public class LoginController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @Pass
    //5秒产生一个令牌,放入容量为0.3的令牌桶
    @AccessLimit(perSecond=0.3,timeOut = 5000)
    public ResultBean<Map<String, Object>> login(
            @ValidationParam("identity,password")@RequestBody JSONObject requestJson) {
        return ResultHandler.ok(userService.checkMobileAndPasswd(requestJson));
    }



}
