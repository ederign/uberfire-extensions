package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RowDnDEvent {
    private final int rowHashCodeStart;
    private final int rowHashCodeEnd;

    public RowDnDEvent( int rowHashCodeStart, int rowHashCodeEnd ) {

        this.rowHashCodeStart = rowHashCodeStart;
        this.rowHashCodeEnd = rowHashCodeEnd;
    }

    @Override
    public String toString() {
        return rowHashCodeStart + " " + rowHashCodeEnd;
    }

    public int getRowHashCodeStart() {
        return rowHashCodeStart;
    }

    public int getRowHashCodeEnd() {
        return rowHashCodeEnd;
    }
}
