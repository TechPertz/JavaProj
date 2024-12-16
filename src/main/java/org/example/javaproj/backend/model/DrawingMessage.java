package org.example.javaproj.backend.model;

public class DrawingMessage {
    private DrawingPoint[] points;
    private String type;

    public DrawingPoint[] getPoints() {
        return points;
    }

    public void setPoints(DrawingPoint[] points) {
        this.points = points;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
