package com.bfd.properties;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:默认参数设置
 */
public interface SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code/image";
    /**
     * 当请求需要身份认证时，默认跳转的url
     */
    String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";
    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
    /**
     * 默认登录页面
     */
    String DEFAULT_LOGIN_PAGE_URL = "/login.html";
    /**
     * session失效默认的跳转地址
     */
    String DEFAULT_SESSION_INVALID_URL = "/session/invalid";

    String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";

    int REMEMBER_ME_SECONDS = 3600;

    /**
     * 当session数量达到最大时，阻止后来的用户登录
     */
    int MAXIMUM_SESSIONS = 1;

    String MESSAGE_SESSION_EXPIRED = "session失效";

    String LOGOUT_URL = "/signOut";

    String DRM_URL = "/drm/**";

    String ADMIN_ACCOUNT = "admin";
}
