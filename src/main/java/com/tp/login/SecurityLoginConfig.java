package com.tp.login;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityLoginConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login","/error","/register")
                .not()
                .authenticated()
                .antMatchers("/js/**", "/css/**","/images/**", "/fonts/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .failureForwardUrl("/login-error")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login")
                .and()
                .logout()
                .clearAuthentication(true)
                .logoutSuccessUrl("/login");

    }
}
