package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
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

    public void init() {
        createDefaultRow();
    }

    private void createDefaultRow() {
        final Row row = createRow();
        row.defaultEmptyRow();
        getRows().add( row );
        updateView();
    }

    private Row createRow() {
        return rowInstance.get();
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
        //TODO
//        dndManager.reset();
    }

    public void repaintContainer( @Observes RepaintContainerEvent repaintContainerEvent ) {
        GWT.log( "repaint" );
//        teste();
        updateView();
    }

    private void updateView() {
        clearView();
//        GWT.log( "ROWS (Update View)" );
//        GWT.log( rows.hashCode() + "" );
        for ( Row row : getRows() ) {
//            GWT.log( row.hashCode() + "" );
            view.addRow( row.getView() );
        }
//        GWT.log( "END" );
    }

    private List<Row> getRows() {
//        GWT.log( "Container" );
//        GWT.log( "=============" );
//        for ( Row row : rows ) {
//            GWT.log( row.toString() );
//            GWT.log( "............" );
//        }
        return rows;
    }

    private void clearView() {
        view.clear();
    }

    private void swapRows(@Observes RowDnDEvent rowDndEvent ) {
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
//        final Row endRow = rows.get( endColumnResize );
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
        GWT.log( "old" );
        teste();
        GWT.log( "new" );
        Collections.swap( rows, begin, end );
        teste();
        updateView();
    }

    public void teste() {
        for ( Row row : rows ) {
            GWT.log( row.hashCode()+"" );
        }
    }

    public UberView<Container> getView() {
        return view;
    }

}
