package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;

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
        GWT.log( "beginColumnResize" );
        this.columnHashCode = columnHashCode;
        this.isOnColumnResize = true;
        this.beginColumnX = beginX;
    }

    public void endColumnResize( int rowHashCodeEnd, int endX ) {
        if ( isOnColumnResize ) {
            GWT.log( "endColumnResize" );
            handleColumnResize( endX, rowHashCodeEnd );
            this.isOnColumnResize = false;
        } else if ( isOnRowMove ) {
            GWT.log( "endRowMove" );
            rowDnDEvent.fire( new RowDnDEvent( rowHashBegin, rowHashCodeEnd ) );
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

    private void handleColumnResize( int endX, int rowHashCodeEnd ) {
        if ( left( endX ) ) {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode, rowHashCodeEnd ).left() );
        } else {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode, rowHashCodeEnd ).right() );
        }
    }

    public void beginRowMove( int rowHashBegin ) {
        if ( !isOnColumnResize ) {
            GWT.log( "beginRowMove" );
            this.rowHashBegin = rowHashBegin;
            this.isOnRowMove = true;
        }
    }

    public void endRowMove( int rowHashCodeEnd ) {
        if ( isOnRowMove ) {
            GWT.log( "endRowMove" );
            rowDnDEvent.fire( new RowDnDEvent( rowHashBegin, rowHashCodeEnd ) );
        }
    }


    public boolean isOnDnd() {
        return isOnRowMove || isOnColumnResize;
    }
}
