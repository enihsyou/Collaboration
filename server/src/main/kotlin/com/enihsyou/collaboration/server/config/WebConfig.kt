package com.enihsyou.collaboration.server.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        /*认证范围*/
        http
            .authorizeRequests()
            .anyRequest().permitAll()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
        /*关闭CSRF检测*/
        http
            .csrf().disable()
            .cors()
    }


    /**设置密码编码器*/
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
