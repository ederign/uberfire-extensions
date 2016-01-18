package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
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

    @Inject
    RowDndManager dndManager;

    final int i = Random.nextInt();

    List<Row> rows = new ArrayList<Row>();

    private final View view;

    public void init() {
        createDefaultRow();
        createDefaultRow();
        createDefaultRow();
    }

    private void createDefaultRow() {
        final Row row = rowInstance.get();
        row.defaultEmptyRow();
        rows.add( row );
        updateView();
    }

    public void load() {
        //TODO load a existent layout
    }

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
//        addRows( 6, 6 );
//        addRows( 12 );
//        addRows( 4, 4, 4 );
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all rows instances
    }

    public void containerOut() {
        dndManager.reset();
    }


    public void handleRowDnD( @Observes RowDnDEvent rowDndEvent ) {
        GWT.log( "rowDndEvent" );
        swapRows( rowDndEvent );
        updateView();
    }

    public void repaintContainer( @Observes RepaintContainerEvent repaintContainerEvent ) {
        GWT.log( "repaint" );
        updateView();
    }

    private void updateView() {
        clearView();
//        GWT.log( "ROWS (Update View)" );
//        GWT.log( rows.hashCode() + "" );
        for ( Row row : rows ) {
//            GWT.log( row.hashCode() + "" );
            view.addRow( row.getView() );
        }
//        GWT.log( "END" );
    }

    private void clearView() {
        view.clear();
    }

    private void swapRows( @Observes RowDnDEvent rowDndEvent ) {
        int begin = -1;
        int end = -1;

        for ( int i = 0; i < rows.size(); i++ ) {
            Row actual = rows.get( i );

            if ( actual.hashCode() == rowDndEvent.getRowHashCodeBegin() ) {
                begin = i;
            }
            if ( actual.hashCode() == rowDndEvent.getRowHashCodeEnd() ) {
                end = i;
            }
        }

//        final Row beginRow = rows.get( begin );
//        final Row endRow = rows.get( end );
//        List<Row> newRows = new ArrayList<Row>();
//        for ( Row row : rows ) {
//            if ( row == beginRow ) {
//                newRows.add( endRow );
//            } else if ( row == endRow ) {
//                newRows.add( beginRow );
//            } else {
//                newRows.add( row );
//            }
//        }
//        this.rows = newRows;
        Collections.swap( rows, begin, end );
    }

    public UberView<Container> getView() {
        return view;
    }

}
