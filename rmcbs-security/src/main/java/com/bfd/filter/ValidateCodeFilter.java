package com.bfd.filter;

import com.bfd.exception.ValidateCodeException;
import com.bfd.properties.SecurityConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:图形验证码过滤器
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getRequestURI().indexOf(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)>-1 &&
                StringUtils.equalsIgnoreCase(request.getMethod(),RequestMethod.POST.toString())) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型");
            try {
                validate(new ServletWebRequest(request));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) {
        HttpSession session = request.getRequest().getSession();
        System.out.println("验证sessionId:"+session.getId());
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        String codeInSession = (String)session.getAttribute(SecurityConstants.SESSION_KEY_IMAGE_CODE);

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession, codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        session.removeAttribute(SecurityConstants.SESSION_KEY_IMAGE_CODE);

    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
