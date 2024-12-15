package org.example.javaproj.frontend.network;

public interface Client {
    boolean connect();
    void sendMessage(String message);
    void close();
}
