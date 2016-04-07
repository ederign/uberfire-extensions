package org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra;

import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;

public class ColumnDrop {

    private final LayoutDragComponent component;
    private final int hash;
    private final Orientation orientation;

    public ColumnDrop( LayoutDragComponent component, int hash,
                       Orientation orientation ) {
        this.component = component;
        this.hash = hash;
        this.orientation = orientation;
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

    public LayoutDragComponent getComponent() {
        return component;
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