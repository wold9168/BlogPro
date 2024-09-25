package com.hgd.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static <T> T getAuthUser(){
        try {
            Object principal = getPrincipal();
            if(principal instanceof String){
                return null;
            }
            return (T) principal;
        }
        catch (Exception e){return null;}
    }
}
