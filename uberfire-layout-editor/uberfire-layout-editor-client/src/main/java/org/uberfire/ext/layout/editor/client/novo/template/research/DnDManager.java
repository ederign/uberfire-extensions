package org.uberfire.ext.layout.editor.client.novo.template.research;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class DnDManager {


    @Inject
    Event<ColumnResizeEvent> columnResizeEvent;


    private int columnHashCode;
    private int beginX;
    private boolean isOnDnD;

    public void beginColumnResize( int columnHashCode, int beginX ) {
        this.columnHashCode = columnHashCode;
        this.isOnDnD = true;
        this.beginX = beginX;
    }

    public void endColumnResize( int endX ) {
        if ( isOnDnD ) {
            handle( endX );
            this.isOnDnD = false;
        }
    }


    public void resetColumnResize() {
        if ( isOnDnD ) {
            this.isOnDnD = false;
        }
    }

    private boolean left( int endX ) {
        return endX < beginX;
    }

    private void handle( int endX ) {
        if ( left( endX ) ) {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode ).left() );
        } else {
            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCode ).right() );
        }
    }
}
