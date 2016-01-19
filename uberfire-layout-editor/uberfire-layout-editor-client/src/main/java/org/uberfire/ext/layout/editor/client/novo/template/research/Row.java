package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
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

    @Inject
    Instance<Column> columnInstance;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    List<Column> columns = new ArrayList<Column>();

    Column previewColumn;

    @ApplicationScoped
    Container container;

    @Inject
    DnDManager dndManager;

    public void defaultEmptyRow() {
        final Column column = createColumn();
        column.defaultEmptyColumn( defaultEmptyRowDropCommand() );
        columns.add( column );
        updateView();
    }

    private Column createColumn() {
        return columnInstance.get();
    }

    public void mouseDown() {
        dndManager.beginRowMove( hashCode() );
    }

    public void mouseUp() {
        dndManager.endRowMove( hashCode() );
    }

    public void dragEnterEvent() {
        //TODO avoid leak
        if(previewColumn==null){
            previewColumn = createColumn();
            previewColumn.defaultEmptyColumn( defaultEmptyRowDropCommand() );
        }
        view.addColumn( previewColumn.getView() );
    }

    public void dragEndEvent() {
        view.removeColumn(previewColumn.getView());
    }

    public interface View extends UberView<Row> {

        void addColumn( UberView<Column> view );

        void clear();

        void removeColumn( UberView<Column> view );
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

                        Integer originalSize = column.getSize();
                        Integer newColumnSize = originalSize / 2;

                        if ( originalSize % 2 == 0 ) {
                            column.setSize( newColumnSize );
                        } else {
                            column.setSize( newColumnSize + 1 );
                        }

                        if ( drop.dropXPosition < drop.columnMiddleX ) {
                            final Column newColumn = createColumn();
                            Column.Type type = getColumnType( i );
                            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand() );
                            columns.add( newColumn );
                            columns.add( column );

                        } else {
                            columns.add( column );
                            final Column newColumn = createColumn();
                            Column.Type type = getColumnType( i + 1 );
                            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand() );
                            columns.add( newColumn );
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

    private boolean dropIsOn( ColumnDrop drop, Column column ) {
        return drop.hash == column.hashCode();
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
        GWT.log( "resizeColumns" );
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
