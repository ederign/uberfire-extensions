package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Dependent
public class Container {


    @Inject
    Instance<Row> rowInstance;

    List<Row> rows = new ArrayList<Row>();

    private final View view;

    public interface View extends UberView<Container> {
        void addRow( UberView<Row> view );

        void clear();
    }

    @Inject
    public Container( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
        addRow( 6, 6 );
        addRow( 12 );
        addRow( 4, 4, 4 );
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all rows instances
    }


    private void addRow( Integer... colSpans ) {
        final Row row = rowInstance.get();
        row.init( colSpans );
        rows.add( row );
        view.addRow( row.getView() );
    }

    public void handle( @Observes RowDnDEvent rowDndEvent ) {
        view.clear();
        int begin = -1;
        int end = -1;

        for ( int i = 0; i < rows.size(); i++ ) {
            Row actual = rows.get( i );

            if ( actual.hashCode() == rowDndEvent.getRowHashCodeStart() ) {
                begin = i;
            }
            if ( actual.hashCode() == rowDndEvent.getRowHashCodeEnd() ) {
                end = i;
            }
        }
        GWT.log(begin + " " + end);
        Collections.swap( rows, begin, end );
        for ( Row row : rows ) {
            view.addRow( row.getView() );
        }

    }

    public UberView<Container> getView() {
        return view;
    }

}
