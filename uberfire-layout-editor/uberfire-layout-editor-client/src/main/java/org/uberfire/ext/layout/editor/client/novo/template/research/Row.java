package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class Row {

    private final View view;

    private List<Column> columns = new ArrayList<Column>();

    @Inject
    Instance<Column> columnInstance;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    @Inject
    DnDManager dndManager;

    private ParameterizedCommand<RowDrop> dropCommand;

    public void firstEmptyRow() {
//        final Column column = createColumn();
//        column.defaultEmptyColumn( defaultEmptyRowDropCommand() );
//        columns.add( column );
        updateView();
    }

    public void firstRowWithColumn() {
        final Column column = createColumn();
        column.columnFromDrop( dropCommand() );
        columns.add( column );
        updateView();
    }

    private Column createColumn() {
        final Column column = columnInstance.get();
        column.setParentHashCode( hashCode() );
        return column;
    }


    public void init( ParameterizedCommand<RowDrop> dropCommand ) {
        this.dropCommand = dropCommand;
    }

    public void drop( RowDrop.Orientation orientation ) {
        dropCommand.execute( new RowDrop( hashCode(), orientation ) );
    }

    public interface View extends UberView<Row> {

        void addColumn( UberView<Column> view );

        void clear();

    }

    @Inject
    public Row( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public void rowOut() {
        dndManager.resetColumnResize();
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all get instances
    }

    private ParameterizedCommand<ColumnDrop> defaultEmptyRowDropCommand() {
        return new ParameterizedCommand<ColumnDrop>() {
            @Override
            public void execute( ColumnDrop parameter ) {
                view.clear();
                columns = new ArrayList<Column>();
                createColumns( 12 );
                updateView();
            }
        };
    }


    ParameterizedCommand<ColumnDrop> dropCommand() {
        return new ParameterizedCommand<ColumnDrop>() {
            @Override
            public void execute( ColumnDrop drop ) {
                List<Column> columns = new ArrayList<Column>();
                for ( int i = 0; i < Row.this.columns.size(); i++ ) {
                    Column column = Row.this.columns.get( i );

                    //TODO dont drop if the column size == 1
                    if ( dropIsOn( drop, column ) && column.getSize() != 1 ) {

                        if ( isASideDrop( drop ) ) {
                            handleSideDrop( drop, columns, i, column );
                        }
                        else{
                            handleNewComponentDrop(columns, column);
                        }
                    } else {
                        columns.add( column );
                    }
                }
                Row.this.columns = columns;
                updateView();
            }
        };
    }

    private void handleNewComponentDrop( List<Column> columns, Column column) {
        
        columns.add( column );
    }

    private void handleSideDrop( ColumnDrop drop, List<Column> columns, int i, Column column ) {
        Integer originalSize = column.getSize();
        Integer newColumnSize = originalSize / 2;

        if ( originalSize % 2 == 0 ) {
            column.setSize( newColumnSize );
        } else {
            column.setSize( newColumnSize + 1 );
        }

        if ( drop.getOrientation() == ColumnDrop.Orientation.LEFT ) {
            final Column newColumn = createColumn();
            Column.Type type = getColumnType( i );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand() );
            columns.add( newColumn );
            column.setColumnType( getColumnType( i + 1 ) );
            columns.add( column );

        } else {
            column.setColumnType( getColumnType( i ) );
            columns.add( column );
            final Column newColumn = createColumn();
            Column.Type type = getColumnType( i + 1 );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand() );
            columns.add( newColumn );
        }
    }

    private boolean isASideDrop( ColumnDrop drop ) {
        return drop.getOrientation() == ColumnDrop.Orientation.LEFT || drop
                .getOrientation() == ColumnDrop.Orientation.RIGHT;
    }

    private boolean dropIsOn( ColumnDrop drop, Column column ) {
        return drop.getHash() == column.hashCode();
    }


    private void createColumns( Integer... colSpans ) {
        for ( int i = 0; i < colSpans.length; i++ ) {
            Integer colSpan = colSpans[i];
            final Column column = createColumn();
            Column.Type type = getColumnType( i );
            column.init( hashCode(), type, colSpan, dropCommand() );
            columns.add( column );
        }
    }

    private Column.Type getColumnType( int i ) {
        Column.Type type;
        if ( i == 0 ) {
            type = Column.Type.FIRST;
        } else {
            type = Column.Type.MIDDLE;
        }
        return type;
    }

    public void resizeColumns( @Observes ColumnResizeEvent resize ) {
        if ( resize.getRowHashCode() == hashCode() ) {
            Column resizeColumn = getColumn( resize );

            if ( resizeColumn != null ) {
                Column affectedColumn = columns.get( columns.indexOf( resizeColumn ) - 1 );

                if ( resize.isLeft() ) {
                    resizeColumn.incrementSize();
                    affectedColumn.reduzeSize();
                } else {
                    affectedColumn.incrementSize();
                    resizeColumn.reduzeSize();
                }
            }
            updateView();
        }
    }


    private Column getColumn( ColumnResizeEvent resize ) {
        for ( Column column : columns ) {
            if ( resize.getColumnHashCode() == column.hashCode() ) {
                return column;
            }
        }
        return null;
    }

    private void updateView() {
        repaintContainerEvent.fire( new RepaintContainerEvent() );
    }

    public UberView<Row> getView() {
        for ( Column column : columns ) {
            view.addColumn( column.getView() );
        }
        return view;
    }


    @Override
    public String toString() {
        GWT.log( "ROW " + hashCode() );
        for ( Column column : columns ) {
            GWT.log( "COLUMN" );
            GWT.log( column.toString() );
        }
        GWT.log( "====================" );
        return super.toString();
    }
}
