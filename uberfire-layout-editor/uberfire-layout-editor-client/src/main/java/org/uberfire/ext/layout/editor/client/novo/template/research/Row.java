package org.uberfire.ext.layout.editor.client.novo.template.research;

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

    @Inject
    Instance<Column> columnInstance;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    List<Column> columns = new ArrayList<Column>();

    Column defaultEmptyColumn;

    @Inject
    ColumnResizeManager columnResizeManager;

    @Inject
    DnDManager dndManager;

    public void defaultEmptyRow() {
        final Column column = columnInstance.get();
        column.defaultEmptyColumn( defaultEmptyRowDropCommand() );
        defaultEmptyColumn = column;
        columns.add( defaultEmptyColumn );
        updateView();
    }

    public void mouseDown() {
        dndManager.beginRowMove( hashCode() );
    }

    public void mouseUp() {
        dndManager.endRowMove( hashCode() );
    }

    public interface View extends UberView<Row> {

        void addColumn( UberView<Column> view );

        void clear();

    }

    @Inject
    public Row( final View view ) {
        this.view = view;
    }

    //old one
    public void initDemo( Integer[] colSpans ) {
        createColumns( colSpans );
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
                //mudei aqui
                repaintContainerEvent.fire( new RepaintContainerEvent() );
            }
        };
    }

    ParameterizedCommand<ColumnDrop> dropCommand() {
        return new ParameterizedCommand<ColumnDrop>() {
            @Override
            public void execute( ColumnDrop drop ) {
                view.clear();
                List<Column> newRow = new ArrayList<Column>();
                for ( int i = 0; i < columns.size(); i++ ) {
                    Column column = columns.get( i );

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
                            final Column newColumn = columnInstance.get();
                            Column.Type type = getColumnType( i );
                            newColumn.init( type, newColumnSize, dropCommand() );
                            newRow.add( newColumn );
                            newRow.add( column );

                        } else {
                            newRow.add( column );
                            final Column newColumn = columnInstance.get();
                            Column.Type type = getColumnType( i + 1 );
                            newColumn.init( type, newColumnSize, dropCommand() );
                            newRow.add( newColumn );
                        }


                    } else {
                        newRow.add( column );
                    }
                }
                columns = newRow;
                for ( Column column : newRow ) {
                    view.addColumn( column.getView() );
                }
            }
        };
    }

    private boolean dropIsOn( ColumnDrop drop, Column column ) {
        return drop.hash == column.hashCode();
    }


    private void createColumns( Integer... colSpans ) {
        for ( int i = 0; i < colSpans.length; i++ ) {
            Integer colSpan = colSpans[i];
            final Column column = columnInstance.get();
            Column.Type type = getColumnType( i );
            column.init( type, colSpan, dropCommand() );
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


    private Column getColumn( ColumnResizeEvent resize ) {
        for ( Column column : columns ) {
            if ( resize.getColumnHashCode() == column.hashCode() ) {
                return column;
            }
        }
        return null;
    }

    private void updateView() {
        view.clear();
        for ( Column column : columns ) {
            view.addColumn( column.getView() );
        }
    }

    public UberView<Row> getView() {
        return view;
    }


}
