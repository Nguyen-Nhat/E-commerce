package org.ckxnhat.resource.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-27 10:07:08.837
 */

public class JwtUtil {
    public static Long getUserId() {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = auth.getToken();
        return (Long) jwt.getClaims().get("uid");
    }
}
