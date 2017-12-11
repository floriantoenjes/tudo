package com.floriantoenjes.tudo.config;

import com.floriantoenjes.tudo.auth.JWTAuthenticationFilter;
import com.floriantoenjes.tudo.auth.JWTLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                    .cors()
                .and()
                    .httpBasic() // Maybe remove this later if not needed anymore
                    .and()
                    .csrf()
                    .disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.setAllowCredentials(true);

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("OPTIONS");

        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
