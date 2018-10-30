package com.bfd.config;

import javax.sql.DataSource;

import com.bfd.filter.ValidateCodeFilter;
import com.bfd.properties.SecurityConstants;
import com.bfd.repository.UserRepository;
import com.bfd.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:安全模块整体配置
 */
@Component
public class FormAuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
            http
            //验证码校验
            .addFilterBefore(validateCodeFilter,UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                //登录页设置
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                //登录表单提交action
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                //登录成功处理
                .successHandler(myAuthenticationSuccessHandler)
                //登录失败处理
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                //记住我
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                //过期时间
                .tokenValiditySeconds(SecurityConstants.REMEMBER_ME_SECONDS)
                .userDetailsService(userDetailsService)
                .and()
            // session 配置
            //.sessionManagement()
                //.invalidSessionUrl(SecurityConstants.DEFAULT_SESSION_INVALID_URL).and()
                // 设置最大session数量
                //.maximumSessions(SecurityConstants.MAXIMUM_SESSIONS)
                // session超时处理策略
                //.expiredSessionStrategy(new MySessionStrategy())
                //.sessionRegistry(sessionRegistry)
                //.and()
                //.and()
            //退出设置
            .logout()
                //退出路径
                .logoutUrl(SecurityConstants.LOGOUT_URL)
                //退出成功处理
                .logoutSuccessHandler(logoutSuccessHandler)
                //删除cookie
                .deleteCookies("JSESSIONID")
                .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                //不需要鉴权的路径
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PAGE_URL,
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX,
                        SecurityConstants.DEFAULT_SESSION_INVALID_URL,
                        SecurityConstants.LOGOUT_URL,
                        SecurityConstants.DRM_URL).permitAll()
                .anyRequest()
                //权限控制
                //.access("@userRepository.hasPermission(request, authentication)")
                .authenticated()
                .and()
            .cors().and()
            .csrf().disable();
    }

    /**
     * 记住我功能的token存取器配置
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public UserRepository userRepository(){
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        userRepository.setDataSource(dataSource);
        return userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder()	{
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
