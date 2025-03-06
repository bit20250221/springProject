package org.spring.attraction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.spring.attraction.ENUM.UserType.manager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/index","/login","/register", "/loginProc","/registerProc").permitAll()
                        //.requestMatchers("/경로 추가 필요").hasAnyRole("attraction","manager")
                        .requestMatchers("/admin").hasRole(manager.name())
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login") // 접근을 제한한 폼이 login 폼으로 바뀜
                        .loginProcessingUrl("/loginProc")           // 로그인 데이터를 form 태그의 경로로 보냄
                        .defaultSuccessUrl("/main")
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
