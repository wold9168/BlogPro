package com.hgd.security;

import com.hgd.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PrePostServiceImpl {
    public boolean prePost(String p){
        UserDetailsImpl authUser = SecurityUtil.getAuthUser();
        List<String> permList = authUser.getPermList();

        return permList.contains(p);
    }
}
