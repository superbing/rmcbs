package com.bfd.session;

import com.bfd.properties.SecurityConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:session过期处理
 */
public class MySessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(SecurityConstants.MESSAGE_SESSION_EXPIRED);
    }
}
