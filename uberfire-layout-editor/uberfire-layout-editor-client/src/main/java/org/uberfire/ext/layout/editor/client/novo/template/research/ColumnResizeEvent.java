package org.uberfire.ext.layout.editor.client.novo.template.research;

public class ColumnResizeEvent {

    private int columnHashCodeBegin;
    private int columnHashCodeEnd;
    private Direction direction = Direction.LEFT;

    public ColumnResizeEvent( int columnHashCodeBegin,
                              int columnHashCodeEnd ) {

        this.columnHashCodeBegin = columnHashCodeBegin;
        this.columnHashCodeEnd = columnHashCodeEnd;
    }

    public ColumnResizeEvent left() {
        this.direction = Direction.LEFT;
        return this;
    }

    public ColumnResizeEvent right() {
        this.direction = Direction.RIGHT;
        return this;
    }

    public boolean isLeft(){
        return direction == Direction.LEFT;
    }

    private enum Direction {
        LEFT, RIGHT;
    }

    public int getColumnHashCodeBegin() {
        return columnHashCodeBegin;
    }

    public int getColumnHashCodeEnd() {
        return columnHashCodeEnd;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "ColumnResizeEvent{" +
                "columnHashCodeBegin=" + columnHashCodeBegin +
                ", columnHashCodeEnd=" + columnHashCodeEnd +
                ", direction=" + direction +
                '}';
    }

    public ColumnResizeEvent(){};
}
