package org.uberfire.ext.layout.editor.client.novo.template.research;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RowDndManager {


    @Inject
    Event<RowDnDEvent> rowDnDEvent;


    private int rowHashCodeStart;
    private int rowHashCodeEnd;

    public void begin( int rowHashCodeStart ) {
        this.rowHashCodeStart = rowHashCodeStart;
    }

    public void end( int rowHashCodeEnd ) {
        this.rowHashCodeEnd = rowHashCodeEnd;

        rowDnDEvent.fire( new RowDnDEvent( rowHashCodeStart, rowHashCodeEnd ) );
    }


}
