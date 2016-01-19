package org.uberfire.ext.layout.editor.client.novo.template.research;

public class RowDrop {


    private final int rowHashCode;
    private final Orientation orientation;

    public RowDrop( int rowHashCode, Orientation orientation ) {

        this.rowHashCode = rowHashCode;
        this.orientation = orientation;
    }

    public int getRowHashCode() {
        return rowHashCode;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public enum Orientation {
        BEFORE, AFTER
    }

}
