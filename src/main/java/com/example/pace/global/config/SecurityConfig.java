package com.example.pace.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] allowUris = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error",
            "/api/v1/auth/**" // 인증 관련해서는 jwt 토큰 인증 없이도 요청을 보낼 수 있어야 함
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers(allowUris).permitAll() // 특정 url 패턴에 대한 접근 권한을 설정
                        .anyRequest().authenticated())// 그 외 모든 요청에 대해 인증을 요구
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .csrf(AbstractHttpConfigurer::disable) // csrf 끄기
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/logout?logout") // 로그아웃 성공시 임시 리다이렉트 url
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
