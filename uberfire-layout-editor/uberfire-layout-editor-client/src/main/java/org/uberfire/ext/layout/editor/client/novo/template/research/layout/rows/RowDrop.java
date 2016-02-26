package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

public class RowDrop {


    private final int rowHashCode;
    private final Orientation orientation;

    //TODO should also has the place name?
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
