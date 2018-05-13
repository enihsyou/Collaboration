package com.enihsyou.collaboration.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

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
        /*设置CORS*/
        http
            .cors()
            .configurationSource {
                CorsConfiguration().applyPermitDefaultValues().apply {
                    addAllowedMethod(HttpMethod.OPTIONS)
                    addAllowedMethod(HttpMethod.PUT)
                    addAllowedMethod(HttpMethod.DELETE)
                    addAllowedMethod(HttpMethod.PATCH)

                    addAllowedOrigin("localhost:8080")

                    allowCredentials = true
                }
            }
        http
            .sessionManagement()
            .maximumSessions(1)
    }

    /**设置密码编码器*/
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config
            /*指定发送给特定用户的前缀是*/
            .setUserDestinationPrefix("/user")
            /*在Websocket中发往这里的请求会被处理*/
            .setApplicationDestinationPrefixes("/app")
            /*发往这里的消息会被广播到所有订阅者那里*/
            .enableSimpleBroker("/topic")
            .setHeartbeatValue(longArrayOf(10000, 10000))
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            /*WebSocket注册接口，从这里建立连接，使用HTTP 101转为WebSocket继续*/
            .addEndpoint("websocket")
            .setAllowedOrigins("*")
    }
}
