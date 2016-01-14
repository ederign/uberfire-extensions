package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class RowDndManager {

    @Inject
    Event<RowDnDEvent> rowDnDEvent;

    private int rowHashCodeStart;

    private boolean isOnDnD;

    public void begin( int rowHashCodeStart ) {
        GWT.log( "BEGIN ROW DND" );
        this.rowHashCodeStart = rowHashCodeStart;
        this.isOnDnD = true;
    }

    public void end( int rowHashCodeEnd ) {
        GWT.log( "END ROW DND" );
        if ( isOnDnD() ) {
            isOnDnD = false;
            rowDnDEvent.fire( new RowDnDEvent( rowHashCodeStart, rowHashCodeEnd ) );
        }
    }

    public boolean isOnDnD() {
        return isOnDnD;
    }

    public void reset() {
        if(isOnDnD){
            GWT.log( "container out" );
            this.isOnDnD = false;
            this.rowHashCodeStart = -1;
        }
    }
}
