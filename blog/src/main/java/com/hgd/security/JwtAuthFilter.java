package com.hgd.security;

import com.hgd.http.AppInfo;
import com.hgd.http.HttpCode;
import com.hgd.util.JwtUtil;
import com.hgd.util.RedisUtil;
import com.hgd.util.WriteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }
        if(JwtUtil.isTokenExpired(token)){
            WriteUtil.write(response, HttpCode.RELOGIN);
            return;
        }
        String userId = JwtUtil.getSubjectFromToken(token);
        UserDetailsImpl userDetails = redisUtil.hGet(AppInfo.USER_REDIS_KEY,userId);
        if(userDetails==null){
            WriteUtil.write(response, HttpCode.RELOGIN);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userDetails, null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
