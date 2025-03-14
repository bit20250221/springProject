package org.spring.attraction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.spring.attraction.ENUM.UserType.attraction;
import static org.spring.attraction.ENUM.UserType.manager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/index","/user/login","/user/save", "/js/**", "/css/**", "/img/**").permitAll()
                        //.requestMatchers("/attraction").hasAnyRole(attraction.name(),manager.name())
                        .requestMatchers("/admin").hasAuthority(manager.name())
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/user/login") // 접근을 제한한 폼이 login 폼으로 바뀜

                        .loginProcessingUrl("/user/login")     // 로그인 데이터를 form 태그의 경로로 보냄
                        .usernameParameter("userLoginId")     // 시큐리티 기본 파라미터는 username
                        .passwordParameter("pass")            // 시큐리티 기본 파라미터는 userPassword
                        .defaultSuccessUrl("/attraction/list", true)
                        .permitAll()
                );

        http
                .logout((auth) -> auth.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
