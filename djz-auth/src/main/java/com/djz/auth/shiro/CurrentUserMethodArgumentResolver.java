package com.djz.auth.shiro;


import com.djz.auth.annotation.CurUser;
import com.djz.auth.entity.SystemUser;
import com.djz.exception.BusinessException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *  增加方法注入，将含有 @CurrentUser 注解的方法参数注入当前登录用户
 * @author djz
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SystemUser.class)
                && parameter.hasParameterAnnotation(CurUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SystemUser user = (SystemUser) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
        if (user == null) {
            throw new BusinessException("获取用户信息失败");
        }
        return user;
    }
}
