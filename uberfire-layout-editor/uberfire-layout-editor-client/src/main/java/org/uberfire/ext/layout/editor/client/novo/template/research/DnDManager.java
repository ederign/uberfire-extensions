package org.uberfire.ext.layout.editor.client.novo.template.research;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class DnDManager {

    @Inject
    Event<ColumnResizeEvent> columnResizeEvent;


    @Inject
    Event<RowDnDEvent> rowDnDEvent;


    private boolean isOnColumnResize;
    private int columnHashCode;
    private int beginColumnX;

    private boolean isOnRowMove;
    private int rowHashBegin;

    public void beginColumnResize( int columnHashCode, int beginX ) {
        this.columnHashCode = columnHashCode;
        this.isOnColumnResize = true;
        this.beginColumnX = beginX;
    }

    public void endColumnResize( int endX ) {
        if ( isOnColumnResize ) {
            handleColumnResize( endX );
            this.isOnColumnResize = false;
        }
    }

    public void resetColumnResize() {
        if ( isOnColumnResize ) {
            this.isOnColumnResize = false;
        }
    }

    private boolean left( int endX ) {
        return endX < beginColumnX;
    }

    private void handleColumnResize( int endX ) {
        if ( left( endX ) ) {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode ).left() );
        } else {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode ).right() );
        }
    }

    public void beginRowMove( int rowHashBegin ) {
        this.rowHashBegin = rowHashBegin;
        this.isOnRowMove = true;
    }

    public boolean isOnRowMove() {
        return isOnRowMove;
    }

    public void endRowMove( int rowHashCodeEnd ) {
        if ( isOnRowMove ) {
            rowDnDEvent.fire( new RowDnDEvent( rowHashBegin, rowHashCodeEnd ) );
        }
    }
}
