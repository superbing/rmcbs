package com.bfd.controller;

import com.bfd.properties.SecurityConstants;
import com.bfd.utils.ImageCode;
import com.bfd.utils.Result;
import com.bfd.utils.ValidateCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:处理需要身份认证的请求
 */
@RestController
public class SecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    /**
     * 当需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Result<Object> requireAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是:" + targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, SecurityConstants.DEFAULT_LOGIN_PAGE_URL);
            }
        }

        return new Result<>(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.EXPECTATION_FAILED.getReasonPhrase(), "访问的服务需要身份认证，请引导用户到登录页");
    }

    /**
     * session失效的方法
     *
     * @return
     */
    @GetMapping(SecurityConstants.DEFAULT_SESSION_INVALID_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Result<Object> sessionInvalid() {
        return new Result<>(HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase(), "session失效");
    }

    /**
     * 创建验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX)
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(new ServletWebRequest(request));
        logger.info("验证码"+imageCode.getCode());
        logger.info("获取验证码sessionId"+request.getSession().getId());
        // 将图形验证码放入session中
        request.getSession().setAttribute(SecurityConstants.SESSION_KEY_IMAGE_CODE, imageCode.getCode());
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
}
