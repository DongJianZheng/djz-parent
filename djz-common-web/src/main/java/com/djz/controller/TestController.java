package com.djz.controller;


import com.alibaba.fastjson.JSONObject;
import com.djz.annotation.AccessLimit;
import com.djz.annotation.Pass;
import com.djz.annotation.ValidationParam;
import com.djz.response.ResultBean;
import com.djz.response.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *  测试接口
 * @author djz
 */
@RestController
public class TestController {


    @RequestMapping("/test")
    @Pass
    public ResultBean<String> login() {
        return ResultHandler.ok("");
    }



}
