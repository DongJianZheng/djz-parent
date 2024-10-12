package com.djz.auth.shiro;

import com.alibaba.fastjson.JSONObject;
import com.djz.auth.annotation.Pass;
import com.djz.base.Constant;
import com.djz.utils.ComUtil;
import com.fasterxml.jackson.core.filter.TokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.reflections.Reflections;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author djz
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);

        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static Realm getRealm() {
        return new MyRealm();
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(getRealm());
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 访问401和404页面不通过我们的Filter
        //通过http://127.0.0.1:9527/druid/index.html 访问 liugh/liugh
        filterRuleMap.put("/druid/**", "anon");
        filterRuleMap.put("/error/exthrow", "anon");
        //放行webSocket
        filterRuleMap.put("/websocket/*", "anon");
        //放行swagger
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/swagger-resources", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/webjars/springfox-swagger-ui/**", "anon");
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
        filterRuleMap.putAll(getUrlAndMethodSet());
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }
    @Autowired
    Environment ev ;

    public Map<String, String> getUrlAndMethodSet(){
        String scanPackage = ev.getProperty("scanPackage");
        String contextPath = ev.getProperty("server.servlet.context-path");
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Controller.class);
        classesList.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        for (Class<?> clazz :classesList) {
            String baseUrl = "";
            String[] classUrl ={};
            if(!ComUtil.isEmpty(clazz.getAnnotation(RequestMapping.class))){
                classUrl=clazz.getAnnotation(RequestMapping.class).value();
            }
            Method[] methods = clazz.getMethods();
            for (Method method:methods) {
                if(method.isAnnotationPresent(Pass.class)){
                    String [] methodUrl = null;
                    StringBuilder sb  =new StringBuilder();
                    if(!ComUtil.isEmpty(method.getAnnotation(PostMapping.class))){
                        methodUrl=method.getAnnotation(PostMapping.class).value();
                        if(ComUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(PostMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!ComUtil.isEmpty(method.getAnnotation(GetMapping.class))){
                        methodUrl=method.getAnnotation(GetMapping.class).value();
                        if(ComUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(GetMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!ComUtil.isEmpty(method.getAnnotation(DeleteMapping.class))){
                        methodUrl=method.getAnnotation(DeleteMapping.class).value();
                        if(ComUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(DeleteMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!ComUtil.isEmpty(method.getAnnotation(PutMapping.class))){
                        methodUrl=method.getAnnotation(PutMapping.class).value();
                        if(ComUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(PutMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else {
                        methodUrl=method.getAnnotation(RequestMapping.class).value();
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }
                    if(!ComUtil.isEmpty(baseUrl)){
                        filterRuleMap.put(baseUrl,"anon");
                    }
                }
            }
        }
        Constant.METHOD_URL_SET.addAll(filterRuleMap.keySet());
        log.info("@Pass:"+ JSONObject.toJSONString(filterRuleMap.keySet()));
        return filterRuleMap;
    }

    private String  getRequestUrl(String[] classUrl, String[] methodUrl, StringBuilder sb,String contextPath) {
        if(!ComUtil.isEmpty(contextPath)){
            sb.append(contextPath);
        }
        if(!ComUtil.isEmpty(classUrl)){
            for (String url:classUrl) {
                sb.append(url+"/");
            }
        }
        for (String url:methodUrl) {
            sb.append(url);
        }
        if(sb.toString().endsWith("/")){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString().replaceAll("/+", "/");
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }



}
