package org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra;

import com.google.gwt.core.client.GWT;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDnDEvent;

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

    public void endColumnResize( int rowHashCodeEnd, int endX ) {
        if ( isOnColumnResize ) {
            handleColumnResize( endX, rowHashCodeEnd );
            this.isOnColumnResize = false;
        } else if ( isOnRowMove ) {
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
            this.rowHashBegin = rowHashBegin;
            this.isOnRowMove = true;
        }
    }

    public void endRowMove( int rowHashCodeEnd ) {
        if ( isOnRowMove ) {
            rowDnDEvent.fire( new RowDnDEvent( rowHashBegin, rowHashCodeEnd ) );
        }
    }


    public boolean isOnDnd() {
        return isOnRowMove || isOnColumnResize;
    }
}
