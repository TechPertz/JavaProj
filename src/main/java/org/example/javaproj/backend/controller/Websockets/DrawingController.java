package org.example.javaproj.backend.controller.Websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.example.javaproj.backend.model.Board;
import org.example.javaproj.backend.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DrawingController extends TextWebSocketHandler {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final BoardService boardService;
    private final ObjectMapper objectMapper;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public DrawingController(BoardService boardService, ObjectMapper objectMapper) {
        this.boardService = boardService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long boardId = getBoardIdFromSession(session);
        sessions.put(boardId, session);
    }

    @Override
//    TODO: Add lock
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long boardId = getBoardIdFromSession(session);
        DrawingMessage drawingMessage = objectMapper.readValue(message.getPayload(), DrawingMessage.class);

        Board board = boardService.getMainBoard();
        if (board == null) {
            session.sendMessage(new TextMessage("Board does not exist"));
            return;
        }
        updateBoard(board, drawingMessage);
        broadcastUpdate(boardId, drawingMessage);
    }

    private void updateBoard(Board board, DrawingMessage drawingMessage) {
        for (DrawingPoint point : drawingMessage.getPoints()) {
            board.updatePixel(board, point.getX(), point.getY(), point.getPen());
        }
        boardService.updateMatrixBoard(board);
    }

    private void broadcastUpdate(Long boardId, DrawingMessage drawingMessage) throws IOException {
        WebSocketSession session = sessions.get(boardId);
        if (session != null && session.isOpen()) {
            drawingMessage.setType("UPDATE");
            String message = objectMapper.writeValueAsString(drawingMessage);
            session.sendMessage(new TextMessage(message));
        }
    }

    private Long getBoardIdFromSession(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();
        String paramValue = queryParams.getFirst("boardId");
        if (paramValue == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "boardId missing in Query Params"
            );
        }
        return Long.parseLong(paramValue);
    }

    public static class DrawingMessage {
        private DrawingPoint[] points;
        private String type;

        public DrawingPoint[] getPoints() {
            return points;
        }

        public void setPoints(DrawingPoint[] points) {
            this.points = points;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class DrawingPoint {
        private int x;
        private int y;
        private int pen;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getPen() {
            return pen;
        }

        public void setPen(int pen) {
            this.pen = pen;
        }
    }
}
