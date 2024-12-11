package com.ticketing.system.ticketing_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * This configuration enables the use of WebSockets and sets up an application message broker for handling
 * messaging between the frontend and backend in real-time.
 */
@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling backed by a message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for WebSocket communication.
     * This method specifies the destination prefixes for handling incoming and outgoing messages.
     *
     * @param config the MessageBrokerRegistry used to configure the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers the STOMP WebSocket endpoint that the frontend can connect to.
     * This method configures the WebSocket connection to be used by the frontend, with the allowed origins and
     * support for SockJS fallback in case WebSocket is not supported by the client.
     *
     * @param registry the StompEndpointRegistry used to register the WebSocket endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // Defines the endpoint the frontend will connect
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();  // Enable SockJS for clients that don't support WebSockets natively
    }
}
