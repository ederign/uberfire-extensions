package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

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

    @Inject
    private Instance<Row> rowInstance;

    @Inject
    private Instance<EmptyDropRow> emptyDropRowInstance;

    private List<Row> rows = new ArrayList<Row>();

    private EmptyDropRow emptyDropRow;


    private final View view;

    public void init() {
        emptyDropRow = createEmptyRow();
        view.addEmptyRow( emptyDropRow.getView() );
    }

    private EmptyDropRow createEmptyRow() {
        emptyDropRow = emptyDropRowInstance.get();
        emptyDropRow.init( createEmptyDropCommand() );
        return emptyDropRow;
    }

    private ParameterizedCommand<RowDrop> createEmptyDropCommand() {
//        return new ParameterizedCommand<RowDrop>() {
//            @Override
//            public void execute( RowDrop parameter ) {
//                rows.add( createRowFromDrop() );
//                updateView();
//            }
//        };
        return ( drop ) -> {
            rows.add( createRowFromDrop() );
            updateView();
        };
    }

    private Row createRowFromDrop() {
        final Row row = createRow();
        //TODO read DND DATA and pass proper component as parameter
        row.rowWithOneColumn();
        return row;
    }

    private Row createRow() {
        final Row row = rowInstance.get();
        row.init( createDropCommand(), componentRemovalCommand() );
        return row;
    }

    private ParameterizedCommand<String> componentRemovalCommand() {
        return (placeName) -> {
            for ( Row row : rows ) {
                final boolean removed = row.removeColumn( placeName );
                if ( removed ) {
                    //TODO remove row
                }
            }
        };
//        new ParameterizedCommand<String>() {
//            @Override
//            public void execute( String placeName ) {
//                for ( Row row : rows ) {
//                    final boolean removed = row.removeColumn( placeName );
//                    if ( removed ) {
//                        //TODO remove row
//                    }
//                }
//            }
//        };
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return (dropRow) ->{
            List<Row> newRows = new ArrayList<Row>();
            for ( int i = 0; i < rows.size(); i++ ) {
                //TODO Refator DND
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
        };
//        return new ParameterizedCommand<RowDrop>() {
//            @Override
//            public void execute( RowDrop dropRow ) {
//                List<Row> newRows = new ArrayList<Row>();
//                for ( int i = 0; i < rows.size(); i++ ) {
//                    //TODO Refator DND
//                    Row row = rows.get( i );
//                    if ( dropRow.getRowHashCode() == row.hashCode() ) {
//                        if ( dropRow.getOrientation() == RowDrop.Orientation.AFTER ) {
//                            newRows.add( createRowFromDrop() );
//                            newRows.add( row );
//                        } else {
//                            newRows.add( row );
//                            newRows.add( createRowFromDrop() );
//                        }
//                    } else {
//                        newRows.add( row );
//                    }
//                }
//                rows = newRows;
//                updateView();
//            }
//        };
    }

    public void load() {
        //TODO load a existent layout
    }

    public UberView<Container> changeResolution() {
        return getView();
    }

    public interface View extends UberView<Container> {

        void addRow( UberView<Row> view );

        void clear();

        void addEmptyRow( UberView<EmptyDropRow> emptyDropRow );
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


    public void repaintContainer( @Observes RepaintContainerEvent repaintContainerEvent ) {
        updateView();
    }

    private void updateView() {
        //TODO screens are not displayed in the first try UF BUG?
        updateViewMaybeUfBug();
        updateViewMaybeUfBug();
    }

    private void updateViewMaybeUfBug() {
        clearView();
        if ( !rows.isEmpty() ) {
            for ( int i = 0; i < rows.size(); i++ ) {
                Row row = rows.get( i );
                if ( row.hasColumns() ) {
                    view.addRow( row.getView() );
                }
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


    public UberView<Container> getView() {
        return view;
    }

}
