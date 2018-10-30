package com.bfd.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.bfd.bean.ApiBean;
import com.bfd.bean.AuthInfoBean;
import com.bfd.bean.CompanyBean;
import com.bfd.dao.mapper.AuthMapper;
import com.bfd.enums.StatusEnum;
import com.bfd.service.AuthService;
import com.bfd.config.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: bing.shen
 * @date: 2018/8/13 15:29
 * @Description:
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Value("${auth.secret}")
    private String secret;

    private int calendarField = Calendar.HOUR;

    @Value("${auth.expired-interval}")
    private int calendarInterval;

    private final String preKey = "auth_";


    @Override
    public String getToken(String clientId, String secret){
        CompanyBean companyBean = authMapper.getClient(clientId, secret);
        if(companyBean==null){
            throw new RuntimeException("clientId或secret错误");
        }
        if(StatusEnum.CLOSE.getKey().equals(companyBean.getStatus())){
            throw new RuntimeException("单位状态已关闭");
        }
        Date startTime = companyBean.getStartTime();
        Date endTime = companyBean.getEndTime();
        if(startTime==null || endTime==null){
            throw new RuntimeException("单位商务信息未设置");
        }
        if(!validateTime(new Date(), startTime, endTime)){
            throw new RuntimeException("单位商务信息已到期");
        }
        return createToken(companyBean);
    }

    @Override
    public AuthInfoBean getAuthInfo(String token){
        AuthInfoBean authInfoBean = new AuthInfoBean();
        authInfoBean.setCode(HttpStatus.OK.value());
        Integer errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        //token验证
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            authInfoBean.setCode(HttpStatus.UNAUTHORIZED.value());
            authInfoBean.setMessage("token过期");
        } catch (Exception e) {
            e.printStackTrace();
            authInfoBean.setCode(errorCode);
            authInfoBean.setMessage("token错误");
            return authInfoBean;
        }
        //token解析
        Map<String, Claim> claims = JWT.decode(token).getClaims();
        Claim companyBeanClaim = claims.get("companyBean");
        if (null == companyBeanClaim) {
            authInfoBean.setCode(errorCode);
            authInfoBean.setMessage("token校验失败");
            return authInfoBean;
        }
        CompanyBean companyBean = JSON.parseObject(companyBeanClaim.asString(), CompanyBean.class);
        Long businessId = companyBean.getBusinessId();
        authInfoBean.setCompanyBean(companyBean);
        //读取缓存权限信息
        Object object = RedisUtil.get(preKey.concat(companyBean.getBusinessId().toString()));
        JSONObject jsonObject;
        if(object!=null){
            jsonObject = JSONObject.parseObject((String)object);
        }else{
            jsonObject = this.cacheAuth(businessId);
        }
        authInfoBean.setAuthenticatedObject(jsonObject);
        return authInfoBean;
    }

    @Override
    public ApiBean getApiInfoByUrl(String url, String companyCode) {
        return authMapper.getApiInfoByUrl(url, companyCode);
    }

    /**
     * JWT生成Token.<br/>
     *
     * JWT构成: header, payload, signature
     *
     * @param companyBean
     */
    private String createToken(CompanyBean companyBean){
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create().withHeader(map)
                // payload
                .withClaim("iss", "Service")
                .withClaim("aud", "APP")
                .withClaim("companyBean", JSON.toJSONString(companyBean))
                // sign time
                .withIssuedAt(iatDate)
                // expire time
                .withExpiresAt(expiresDate)
                // signature
                .sign(Algorithm.HMAC256(secret));
        return token;
    }

    private boolean validateTime(Date nowTime, Date startTime, Date endTime){
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    private JSONObject cacheAuth(Long businessId){
        JSONObject jsonObject = null;
        List<ApiBean> list = authMapper.queryAuth(businessId);
        if(!CollectionUtils.isEmpty(list)){
            jsonObject = new JSONObject();
            if(!CollectionUtils.isEmpty(list)){
                for(ApiBean api : list){
                    jsonObject.put(api.getUrl(), api);
                }
            }
            RedisUtil.set(preKey.concat(businessId.toString()), jsonObject.toJSONString());
        }
        return jsonObject;
    }
}
