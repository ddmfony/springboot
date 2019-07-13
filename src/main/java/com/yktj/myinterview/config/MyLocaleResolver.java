package com.yktj.myinterview.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 可以在连接上携带区域信息
 */
public class MyLocaleResolver implements LocaleResolver {
    
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lan = request.getParameter("l");
//        System.out.println("lan:" + lan + "；request:" + request.getParameter("l") + "；session:" + request.getSession().getAttribute("lan") );
        if (StringUtils.isEmpty(lan)){
            lan = (String)request.getSession().getAttribute("lan");
        }else{
            request.getSession().setAttribute("lan", lan);
        }
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(lan)){
            String[] split = lan.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
