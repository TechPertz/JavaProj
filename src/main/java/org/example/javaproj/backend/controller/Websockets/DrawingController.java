package org.example.javaproj.backend.controller.Websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.example.javaproj.backend.Constants;
import org.example.javaproj.backend.model.Board;
import org.example.javaproj.backend.model.DrawingMessage;
import org.example.javaproj.backend.model.DrawingPoint;
import org.example.javaproj.backend.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DrawingController extends TextWebSocketHandler {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final BoardService boardService;
    private final ObjectMapper objectMapper;
    private final Map<Long, Lock> boardLocks = new ConcurrentHashMap<>();
    private final Map<Long, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public DrawingController(BoardService boardService, ObjectMapper objectMapper) {
        this.boardService = boardService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long boardId = getBoardIdFromQueryParams(session);
        sessions.computeIfAbsent(boardId, id -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long boardId = getBoardIdFromQueryParams(session);
        Set<WebSocketSession> boardSessions = sessions.get(boardId);
        if (boardSessions != null) {
            boardSessions.remove(session);
            if (boardSessions.isEmpty()) {
                sessions.remove(boardId);
            }
        }
    }

    private void updateBoard(Board board, DrawingMessage drawingMessage) {
        for (DrawingPoint point : drawingMessage.getPoints()) {
            board.updatePixel(board, point.getX(), point.getY(), point.getPen());
        }
        boardService.updateMatrixBoard(board);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long boardId = 1L; // Using a constant Board ID for now
        Lock lock = boardLocks.computeIfAbsent(boardId, id -> new ReentrantLock());
        lock.lock();
        try {
            Board board = boardService.getMainBoard();
            if (board == null) {
                session.sendMessage(new TextMessage("Board does not exist"));
                return;
            }

            DrawingMessage drawingMessage = objectMapper.readValue(message.getPayload(), DrawingMessage.class);
            updateBoard(board, drawingMessage);
            broadcastUpdate(board.getId(), drawingMessage);
        } finally {
            lock.unlock();
        }
    }

    private void broadcastUpdate(Long boardId, DrawingMessage drawingMessage) throws IOException {
        Set<WebSocketSession> boardSessions = sessions.get(boardId);
        if (boardSessions != null) {
            LOGGER.info("Broadcasting drawing message to board {}", boardId);
            String message = objectMapper.writeValueAsString(drawingMessage);
            for (WebSocketSession session : boardSessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }

    private Long getBoardIdFromQueryParams(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build();
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();
        String paramValue = queryParams.getFirst(Constants.BOARD_ID_KEY);
        if (paramValue == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "boardId missing in Query Params"
            );
        }
        return Long.parseLong(paramValue);
    }




}