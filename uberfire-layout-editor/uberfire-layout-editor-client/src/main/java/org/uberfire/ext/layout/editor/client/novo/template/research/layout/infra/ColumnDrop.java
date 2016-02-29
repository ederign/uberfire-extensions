package org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra;

public class ColumnDrop {

    private final int hash;
    private final Orientation orientation;
    private String dndData;

    public ColumnDrop( int hash,
                       Orientation orientation, String dndData ) {
        this.hash = hash;
        this.orientation = orientation;
        this.dndData = dndData;
    }

    public enum Orientation {
        LEFT, RIGHT, UP, DOWN
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getHash() {
        return hash;
    }

    public String getDndData() {
        return dndData;
    }

    public boolean isASideDrop() {
        return getOrientation() == ColumnDrop.Orientation.LEFT ||
                getOrientation() == ColumnDrop.Orientation.RIGHT;
    }

    public boolean isALeftDrop() {
        return getOrientation() == ColumnDrop.Orientation.LEFT;
    }

    public boolean isADownDrop() {
        return getOrientation() == ColumnDrop.Orientation.DOWN;
    }
}