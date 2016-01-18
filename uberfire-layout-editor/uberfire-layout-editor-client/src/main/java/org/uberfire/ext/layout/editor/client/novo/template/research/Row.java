package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
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

    List<Column> columns = new ArrayList<Column>();

    Column defaultEmptyColumn;

    @Inject
    ColumnResizeManager columnResizeManager;

    public void defaultEmptyRow() {
        final Column column = columnInstance.get();
        column.defaultEmptyColumn( defaultEmptyRowDropCommand() );
        defaultEmptyColumn = column;
        columns.add( defaultEmptyColumn );
        updateView();
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
        columnResizeManager.reset();
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
                view.clear();
                List<Column> newRow = new ArrayList<Column>();
                for ( Column column : columns ) {
                    //TODO dont drop if the column size == 1
                    if ( dropIsOn( drop, column ) && column.getSize() != 1) {

                        Integer originalSize = column.getSize();
                        Integer newColumnSize = originalSize / 2;

                        if ( originalSize % 2 == 0 ) {
                            column.setSize( newColumnSize );
                            GWT.log( "both" + newColumnSize );
                        } else {
                            GWT.log( "actual" + newColumnSize );
                            column.setSize( newColumnSize + 1 );
                            GWT.log( "new" + newColumnSize );
                        }

                        if ( drop.dropXPosition < drop.columnMiddleX ) {
                            final Column newColumn = columnInstance.get();
                            newColumn.init( newColumnSize, dropCommand() );
                            newRow.add( newColumn );
                            newRow.add( column );

                        } else {
                            newRow.add( column );
                            final Column newColumn = columnInstance.get();
                            newColumn.init( newColumnSize, dropCommand() );
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
        for ( Integer colSpan : colSpans ) {
            final Column column = columnInstance.get();
            column.init( colSpan, dropCommand() );
            columns.add( column );
        }
    }

    public void resizeColumns( @Observes ColumnResizeEvent resize ) {
        Column beginResize = null;
        Column endResize = null;
        for ( Column column : columns ) {

            if ( resize.getColumnHashCodeBegin() == column.hashCode() ) {
                beginResize = column;
            }

            if ( resize.getColumnHashCodeEnd() == column.hashCode() ) {
                endResize = column;
            }
        }

        if ( beginResize != null && endResize != null ) {
            GWT.log( resize.toString() );
            GWT.log( beginResize + "" );
            GWT.log( endResize + "" );
            if ( resize.isLeft() ) {
                GWT.log( "YAAAY" );
                if ( beginResize.canReduceSize() ) {
                    GWT.log( "YAAAY1" );
                    beginResize.incrementSize();
                    endResize.reduzeSize();
                    updateView();
                }
            } else {
                GWT.log( "YAAAY2" );
                if ( endResize.canReduceSize() ) {
                    GWT.log( "YAAAY3" );
                    beginResize.incrementSize();
                    endResize.reduzeSize();
                    updateView();
                }
            }
        }
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
