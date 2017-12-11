package com.floriantoenjes.tudo.config;

import com.floriantoenjes.tudo.auth.JWTAuthenticationFilter;
import com.floriantoenjes.tudo.auth.JWTLoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static String API_BASEPATH = "/api/v1";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                .antMatchers(HttpMethod.POST, API_BASEPATH + "/login")
                .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .addFilterBefore(new JWTLoginFilter(API_BASEPATH + "/login", authenticationManager()),
                            UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new JWTAuthenticationFilter(),
                            UsernamePasswordAuthenticationFilter.class)
                    .headers()
                    .frameOptions()
                    .disable()
                .and()
                    .httpBasic()
                    .and()
                    .csrf()
                    .disable();
    }

}
