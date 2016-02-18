package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

public class RowDnDEvent {
    private int rowHashCodeBegin;
    private int rowHashCodeEnd;

    public RowDnDEvent( int rowHashCodeBegin, int rowHashCodeEnd ) {

        this.rowHashCodeBegin = rowHashCodeBegin;
        this.rowHashCodeEnd = rowHashCodeEnd;
    }

    public int getRowHashCodeBegin() {
        return rowHashCodeBegin;
    }

    public int getRowHashCodeEnd() {
        return rowHashCodeEnd;
    }


    public RowDnDEvent(){}
}
