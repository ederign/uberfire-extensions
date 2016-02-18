package org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra;

public class ColumnDrop{

    private final int hash;
    private  final Orientation orientation;

    public ColumnDrop( int hash,
                       Orientation orientation ) {
        this.hash = hash;
        this.orientation = orientation;
    }

    public enum Orientation {
        LEFT, RIGHT, INSIDE,
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getHash() {
        return hash;
    }
}