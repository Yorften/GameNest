package com.gamenest.config.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.gamenest.config.jwt.JwtTokenUtil;
import com.gamenest.exception.InvalidDataException;
import com.gamenest.service.implementation.CustomUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsServiceImpl userDetailsService;

    @Value("${cors-origin}")
    private String corsOrigin;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins(corsOrigin).withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Only process STOMP CONNECT command
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    log.debug("STOMP CONNECT attempt with Authorization header: {}",
                            (authorizationHeader != null ? "Present" : "Missing"));

                    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                        log.warn("STOMP CONNECT failed: Missing or invalid Authorization header.");
                        throw new AuthenticationCredentialsNotFoundException(
                                "Missing or invalid Authorization header for STOMP connection");
                    }

                    String token = authorizationHeader.substring(7);

                    try {
                        if (jwtTokenUtil.validateJwtToken(token)) {
                            String username = jwtTokenUtil.getUsernameFromJWT(token);

                            if (username != null) {
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                                accessor.setUser(authentication);

                                log.info("STOMP CONNECT authenticated successfully for user: {}", username);
                                return message;
                            }
                        }

                        log.warn("STOMP CONNECT failed: JWT validation failed or username extraction failed.");
                        throw new BadCredentialsException("Invalid JWT token for STOMP connection");

                    } catch (Exception e) {
                        log.error("Invalid JWT token: {}", e.getMessage());
                        throw new InvalidDataException("Invalid JWT token");
                    }
                }

                return message;

            }
        });
    }
}
