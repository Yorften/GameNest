package com.gamenest.config.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors-origin}")
    private String corsOrigin;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker to carry messages back to the client
        // on destinations prefixed with "/topic". Clients will subscribe to these.
        registry.enableSimpleBroker("/topic");

        // Designates the "/app" prefix for messages that are bound for methods
        // annotated with @MessageMapping (if you were handling incoming messages from
        // clients).
        // We don't strictly need this for server-to-client pushes only, but it's
        // standard practice.
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register "/ws" as the STOMP endpoint. Clients will connect to this URL.
        // withSockJS() enables SockJS fallback options so that alternate transports
        // may be used if WebSocket is not available. Good for browser compatibility.
        registry.addEndpoint("/ws").withSockJS();

        registry.addEndpoint("/ws").setAllowedOrigins(corsOrigin).withSockJS();
    }
}
