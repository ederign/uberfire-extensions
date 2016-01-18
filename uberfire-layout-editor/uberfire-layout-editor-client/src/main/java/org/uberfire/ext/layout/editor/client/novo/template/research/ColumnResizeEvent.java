package org.uberfire.ext.layout.editor.client.novo.template.research;


public class ColumnResizeEvent {

    private int columnHashCode;
    private Direction direction = Direction.LEFT;

    public ColumnResizeEvent( int columnHashCode ) {

        this.columnHashCode = columnHashCode;
    }

    public int getColumnHashCode() {
        return columnHashCode;
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


    public Direction getDirection() {
        return direction;
    }


    public ColumnResizeEvent(){};
}
