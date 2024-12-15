package org.example.javaproj.backend.config;

import org.apache.logging.log4j.LogManager;
import org.example.javaproj.backend.controller.Websockets.DrawingController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final DrawingController drawingController;

    public WebSocketConfig(DrawingController drawingController) {
        LOGGER.info("Starting WebSocket Config");
        this.drawingController = drawingController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(drawingController, "/ws/draw")
                .setAllowedOrigins("*"); // Adjust allowed origins as per your security requirements
    }
}
