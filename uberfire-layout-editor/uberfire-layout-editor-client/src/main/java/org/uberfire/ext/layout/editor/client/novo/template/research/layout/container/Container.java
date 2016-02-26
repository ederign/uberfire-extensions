package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.EmptyDropRow;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDnDEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;
import org.uberfire.mvp.ParameterizedCommand;

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

    private final Instance<Row> rowInstance;

    private final Instance<EmptyDropRow> emptyDropRowInstance;

    private final View view;

    private List<Row> rows = new ArrayList<Row>();

    private EmptyDropRow emptyDropRow;


    @Inject
    public Container( final View view, Instance<Row> rowInstance, Instance<EmptyDropRow> emptyDropRowInstance ) {
        this.rowInstance = rowInstance;
        this.emptyDropRowInstance = emptyDropRowInstance;
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all rows instances
    }

    public void init() {
        emptyDropRow = createEmptyRow();
        view.addEmptyRow( emptyDropRow.getView() );
    }

    private EmptyDropRow createEmptyRow() {
        emptyDropRow = emptyDropRowInstance.get();
        emptyDropRow.init( createEmptyDropCommand() );
        return emptyDropRow;
    }

    ParameterizedCommand<RowDrop> createEmptyDropCommand() {
        return ( drop ) -> {
            rows.add( createFirstRow( drop ) );
            updateView();
        };
    }

    private Row createFirstRow( RowDrop drop ) {
        //TODO read DND DATA (from drop) and pass proper component as parameter-> pass for rone with oneColumn
        final Row row = rowInstance.get();
        row.init( createRegularRowDropCommand(), existentComponentRemovalCommand() );
        row.withOneColumn( drop );
        return row;
    }

    ParameterizedCommand<String> existentComponentRemovalCommand() {
        return ( placeName ) -> {

            for ( Row row : rows ) {
                final boolean rowUsedToHasThisComponent = row.removeColumn( placeName );
                if ( rowUsedToHasThisComponent ) {
                    if ( rowIsEmpty( row ) ) {
                        rows.remove( row );
                        break;
                    }
                }
            }
        };
    }

    private boolean rowIsEmpty( Row row ) {
        return !row.hasColumns();
    }

    private ParameterizedCommand<RowDrop> createRegularRowDropCommand() {
        return ( dropRow ) -> {
            List<Row> newRows = new ArrayList<Row>();
            for ( int i = 0; i < rows.size(); i++ ) {
                //TODO Refator DND
                Row row = rows.get( i );
                if ( dropRow.getRowHashCode() == row.hashCode() ) {
                    if ( dropRow.getOrientation() == RowDrop.Orientation.AFTER ) {
                        newRows.add( createFirstRow( dropRow ) );
                        newRows.add( row );
                    } else {
                        newRows.add( row );
                        newRows.add( createFirstRow( dropRow ) );
                    }
                } else {
                    newRows.add( row );
                }
            }
            rows = newRows;
            updateView();
        };
    }

    public void repaintContainer( @Observes RepaintContainerEvent repaintContainerEvent ) {
        updateView();
    }

    void updateView() {
        //TODO screens are not displayed in the first try UF BUG?
        updateViewMaybeUfBug();
        updateViewMaybeUfBug();
    }

    private void updateViewMaybeUfBug() {
        clearView();
        if ( !rows.isEmpty() ) {
            GWT.log(rows.size() + " row size");
            for ( int i = 0; i < rows.size(); i++ ) {
                Row row = rows.get( i );
                view.addRow( row.getView() );
            }
        }
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

        if ( begin >= 0 && end >= 0 ) {
            Collections.swap( rows, begin, end );
        }
        updateView();
    }

    public interface View extends UberView<Container> {

        void addRow( UberView<Row> view );

        void clear();

        void addEmptyRow( UberView<EmptyDropRow> emptyDropRow );
    }


    public UberView<Container> getView() {
        return view;
    }

    EmptyDropRow getEmptyDropRow() {
        return emptyDropRow;
    }

    List<Row> getRows() {
        return rows;
    }
}
