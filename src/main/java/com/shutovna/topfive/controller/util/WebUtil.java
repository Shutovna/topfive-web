package com.shutovna.topfive.controller.util;

import jakarta.servlet.http.HttpServletRequest;
import org.thymeleaf.util.StringUtils;

public class WebUtil {
    public static String getPreviousPageByRequest(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return StringUtils.isEmpty(referer) ? "/" : referer;
    }
}
