package com.example.mungtage.config;

import com.example.mungtage.config.oauth.CustomOAuth2UserService;
import com.example.mungtage.config.oauth.JwtAuthFilter;
import com.example.mungtage.config.oauth.OAuth2SuccessHandler;
import com.example.mungtage.config.oauth.TokenService;
import com.example.mungtage.domain.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Override
//    protected void (HttpSecurity http) throws Exception {
//        http.httpBasic().disable()
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
//                .csrf().disable().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth2/authorization/**","/api/v1/oauth/**","/api/v1/match/auto").permitAll()
//                .anyRequest().authenticated();
//
//        http.formLogin().disable()
//                .oauth2Login()
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService)
//                .and()
//                .successHandler(oAuth2SuccessHandler);
//
//        http.addFilterBefore(new JwtAuthFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);
//    }
@Bean
public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic().disable()
            .csrf().disable()
            .cors(cors -> cors
                    .configurationSource(corsConfigurationSource())
            )
            .authorizeRequests(
                    authorizeRequests -> authorizeRequests
                            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .antMatchers("/oauth2/authorization/**","/api/v1/oauth/**","/api/v1/match/auto").permitAll()
                            .anyRequest()
                            .authenticated() // 최소자격 : 로그인
            )
            .sessionManagement(sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin().disable()
            .oauth2Login(oauth -> oauth.userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                    .successHandler(oAuth2SuccessHandler))
            .addFilterBefore(
                    new JwtAuthFilter(tokenService, userRepository),
                    UsernamePasswordAuthenticationFilter.class
            )
            .logout().disable();

    return http.build();
}
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*", "https://mungtage.site"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
