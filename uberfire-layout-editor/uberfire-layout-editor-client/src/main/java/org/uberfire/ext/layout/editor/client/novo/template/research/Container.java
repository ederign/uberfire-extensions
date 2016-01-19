package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;
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

    @Inject
    Instance<Row> rowInstance;

    List<Row> rows = new ArrayList<Row>();

    private final View view;

    public void init() {
        rows.add( createFirstRow() );
        updateView();
    }

    private Row createFirstRow() {
        final Row row = createRow();
        row.firstEmptyRow();
        return row;
    }

    private Row createRowFromDrop() {
        final Row row = createRow();
        row.firstRowWithColumn();
        return row;
    }

    private Row createRow() {
        final Row row = rowInstance.get();
        row.init( createDropCommand() );
        return row;
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return new ParameterizedCommand<RowDrop>() {
            @Override
            public void execute( RowDrop dropRow ) {
                List<Row> newRows = new ArrayList<Row>();
                for ( int i = 0; i < rows.size(); i++ ) {
                    Row row = rows.get( i );
                    if ( dropRow.getRowHashCode() == row.hashCode() ) {
                        if ( dropRow.getOrientation() == RowDrop.Orientation.AFTER ) {
                            newRows.add( createRowFromDrop() );
                            newRows.add( row );
                        } else {
                            newRows.add( row );
                            newRows.add( createRowFromDrop() );
                        }
                    } else {
                        newRows.add( row );
                    }
                }
                rows = newRows;
                updateView();
            }
        };
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
        updateView();
    }

    private void updateView() {
        clearView();
        for ( int i = 0; i < rows.size(); i++ ) {
            Row row = rows.get( i );
            if ( i == 0 ) {
                //todo maybe remove drop on new
            }
            view.addRow( row.getView() );
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

        Collections.swap( rows, begin, end );
        updateView();
    }


    public UberView<Container> getView() {
        return view;
    }

}
