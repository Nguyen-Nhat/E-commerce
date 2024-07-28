package org.ckxnhat.bff.controller;

import org.ckxnhat.bff.viewmodel.response.SuccessApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 08:54:08.957
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private ReactiveRedisSessionRepository redisSessionRepository;

    @GetMapping("/login")
    public ResponseEntity<?> login(){
        SuccessApiResponse successApiResponse = new SuccessApiResponse();
        successApiResponse.setStatus("200");
        successApiResponse.setData("Login successful");
        return ResponseEntity.ok(successApiResponse);
    }
    @GetMapping("/logout")
    public Mono<ResponseEntity<?>> logout(ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap(webSession -> {
                    String sessionId = webSession.getId();
                    return redisSessionRepository.deleteById(sessionId)
                            .then(webSession.invalidate())
                            .then(Mono.fromCallable(() -> {
                                ResponseCookie deleteSessionCookie = ResponseCookie.from("SESSION", "")
                                        .path("/")
                                        .maxAge(0)
                                        .httpOnly(true)
                                        .build();

                                ResponseCookie deleteJSessionIdCookie = ResponseCookie.from("JSESSIONID", "")
                                        .path("/")
                                        .maxAge(0)
                                        .httpOnly(true)
                                        .build();

                                SuccessApiResponse successApiResponse = new SuccessApiResponse();
                                successApiResponse.setStatus("200");
                                successApiResponse.setData("Logout successful");

                                return ResponseEntity.ok()
                                        .header("Set-Cookie", deleteSessionCookie.toString())
                                        .header("Set-Cookie", deleteJSessionIdCookie.toString())
                                        .body(successApiResponse);
                            }));
                });
    }
}
