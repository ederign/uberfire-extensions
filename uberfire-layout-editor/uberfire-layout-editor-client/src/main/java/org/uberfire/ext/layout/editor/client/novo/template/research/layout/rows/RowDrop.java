package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;

public class RowDrop {


    private final LayoutDragComponent component;
    private final int rowHashCode;
    private final Orientation orientation;

    public RowDrop( LayoutDragComponent component, int rowHashCode, Orientation orientation ) {
        this.component = component;
        this.rowHashCode = rowHashCode;
        this.orientation = orientation;
    }

    public int getRowHashCode() {
        return rowHashCode;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public LayoutDragComponent getComponent() {
        return component;
    }

    public enum Orientation {
        BEFORE, AFTER
    }

}
