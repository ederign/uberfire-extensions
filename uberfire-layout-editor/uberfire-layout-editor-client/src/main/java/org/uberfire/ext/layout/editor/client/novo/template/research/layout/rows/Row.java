package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.Column;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ColumnWithComponents;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ComponentColumn;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnResizeEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens.Screens;
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
    Instance<ComponentColumn> columnInstance;

    @Inject
    Instance<ColumnWithComponents> columnWithComponentsInstance;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    @Inject
    DnDManager dndManager;

    public boolean hasColumns() {
        return !columns.isEmpty();
    }

    public boolean removeColumn( String placeName ) {
        Column columnToRemove = null;
        for ( Column column : columns ) {
            if ( column instanceof ComponentColumn ) {
                ComponentColumn c = ( ComponentColumn ) column;
                if ( c.getPlace().equals( placeName ) ) {
                    GWT.log( "YO" );
                    columnToRemove = column;
                }
            } else {
                //TODO
            }
        }
        if ( columnToRemove != null ) {
            setSizeOfSibilinColumn( columnToRemove );

            columns.remove( columnToRemove );

        }
        return false;
    }

    private void setSizeOfSibilinColumn( Column columnToRemove ) {
        //write better algo and also to other types
        if ( columnToRemove instanceof ComponentColumn ) {
            ComponentColumn c = ( ComponentColumn ) columnToRemove;
            final int i = columns.indexOf( columnToRemove );
            final int size = c.getSize();
            if ( i > 0 ) {
                final Column column = columns.get( i - 1 );
                if ( column instanceof ComponentColumn ) {
                    ComponentColumn biggerColumn = ( ComponentColumn ) column;
                    final Integer originalSize = biggerColumn.getSize();
                    biggerColumn.setSize( originalSize + size );
                }
            } else {
                if ( columns.size() >= 2 ) {
                    final Column column = columns.get( 1 );
                    if ( column instanceof ComponentColumn ) {
                        ComponentColumn biggerColumn = ( ComponentColumn ) column;
                        final Integer originalSize = biggerColumn.getSize();
                        biggerColumn.setSize( originalSize + size );
                    }
                }
            }
        }
    }

    public interface View extends UberView<Row> {

        void addColumn( UberView<ComponentColumn> view );

        void clear();

    }

    private ParameterizedCommand<RowDrop> dropOnRowCommand;
    private ParameterizedCommand<String> existentComponentDropCommand;

    public void init( ParameterizedCommand<RowDrop> dropOnRowCommand,
                      ParameterizedCommand<String> existentComponentDropCommand ) {
        this.dropOnRowCommand = dropOnRowCommand;
        this.existentComponentDropCommand = existentComponentDropCommand;
    }


    public void rowWithOneColumn() {
        final ComponentColumn column = createColumn();
        column.columnFromDrop( dropCommand() );
        columns.add( column );
    }

    private ComponentColumn createColumn() {
        final ComponentColumn column = columnInstance.get();
        column.setParentHashCode( hashCode() );
        return column;
    }


    private ColumnWithComponents createColumnWithComponents() {
        final ColumnWithComponents column = columnWithComponentsInstance.get();
        column.setParentHashCode( hashCode() );
        return column;
    }

    public void drop( RowDrop.Orientation orientation ) {
        dropOnRowCommand.execute( new RowDrop( hashCode(), orientation ) );
    }

    public void addColumns( Column... _columns ) {
        for ( Column column : _columns ) {
            columns.add( column );
        }
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


    ParameterizedCommand<ColumnDrop> dropCommand() {
        return new ParameterizedCommand<ColumnDrop>() {
            @Override
            public void execute( ColumnDrop drop ) {

                if ( tempIsDndDataValid( drop.getDndData() ) ) {
                    existentComponentDropCommand.execute( drop.getDndData() );
                }

                List<Column> columns = new ArrayList<Column>();

                List<Column> currentColumns = Row.this.columns;
                for ( int i = 0; i <  currentColumns.size(); i++ ) {

                    final Column currentColumn = Row.this.columns.get( i );
                    if ( isComponentColumn( currentColumn ) ) {
                        ComponentColumn column = ( ComponentColumn ) currentColumn;

                        //TODO dont drop (remove dnd) if the column size == 1
                        handle( drop, columns, i, column );
                    } else {
                        ColumnWithComponents column = ( ColumnWithComponents ) currentColumn;

                        GWT.log( column.hasRows() + "<=" );
//                        if ( dropIsOn( drop, column ) && column.getSize() != 1 ) {
                        if(false){
                            if ( isASideDrop( drop ) ) {
//                                handleSideDrop( drop, columns, i, column );
                            } else {
                                //TODO Precisa colocar o side drop e convertar a column pra nodrop
//                                handleInnerComponentDrop( drop, columns, i, column );
                            }
                        } else {
                            columns.add( column );
                        }
                    }

                }
                Row.this.columns = columns;
                updateView();
            }


        };
    }

    private void handle( ColumnDrop drop, List<Column> columns, int i, ComponentColumn column ) {
        if ( dropIsOn( drop, column ) && column.getSize() != 1 ) {
            if ( isASideDrop( drop ) ) {
                handleSideDrop( drop, columns, i, column );
            } else {
                //TODO Precisa colocar o side drop e convertar a column pra nodrop
                handleInnerComponentDrop( drop, columns, column );
            }
        } else {
            columns.add( column );
        }
    }

    private boolean isComponentColumn( Column currentColumn ) {
        return currentColumn instanceof ComponentColumn;
    }


    private void handleInnerComponentDrop( ColumnDrop drop, List<Column> columns,
                                           ComponentColumn column ) {
        final ColumnWithComponents columnWithComponents = createColumnWithComponents();
        columnWithComponents.init( column.getSize() );
        //uf bug
        Integer originalColumnUFSize = column.getPanelSize();
        String place = !tempIsDndDataValid( drop.getDndData() ) ? Screens.next().name() : drop.getDndData();

        final ComponentColumn newColumn = createColumn();
        newColumn.init( column.getParentHashCode(), getColumnType( 0 ), 12, dropCommand(), place );
        newColumn.halfParentPanelSize( originalColumnUFSize );

        column.setSize( 12 );
        column.halfParentPanelSize( originalColumnUFSize );
        column.recalculateSize();
        column.setColumnType( getColumnType( 0 ) );
        if ( drop.getOrientation() == ColumnDrop.Orientation.DOWN ) {
            columnWithComponents.withComponents( column, newColumn );
        } else {
            columnWithComponents.withComponents( newColumn, column );
        }

        columns.add( columnWithComponents );

    }

    public boolean tempIsDndDataValid( String drop ) {
        if ( drop.isEmpty() ) {
            return false;
        }
        for ( Screens screens : Screens.values() ) {
            if ( drop.equalsIgnoreCase( screens.name() ) ) {
                return true;
            }
        }
        return false;
    }

        private void handleSideDrop( ColumnDrop drop, List<Column> columns, int columnINdex, ComponentColumn column ) {
        Integer originalSize = column.getSize();
        Integer newColumnSize = originalSize / 2;

        String place = !tempIsDndDataValid( drop.getDndData() ) ? Screens.next().name() : drop.getDndData();

        if ( originalSize % 2 == 0 ) {
            column.setSize( newColumnSize );
        } else {
            column.setSize( newColumnSize + 1 );
        }

        if ( drop.getOrientation() == ColumnDrop.Orientation.LEFT ) {
            final ComponentColumn newColumn = createColumn();
            ComponentColumn.Type type = getColumnType( columnINdex );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand(), place );
            columns.add( newColumn );
            column.setColumnType( getColumnType( columnINdex + 1 ) );
            columns.add( column );

        } else {
            column.setColumnType( getColumnType( columnINdex ) );
            columns.add( column );
            final ComponentColumn newColumn = createColumn();
            ComponentColumn.Type type = getColumnType( columnINdex + 1 );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand(), place );
            columns.add( newColumn );
        }
    }

    private boolean isASideDrop( ColumnDrop drop ) {
        return drop.getOrientation() == ColumnDrop.Orientation.LEFT || drop
                .getOrientation() == ColumnDrop.Orientation.RIGHT;
    }

    private boolean dropIsOn( ColumnDrop drop, ComponentColumn column ) {
        return drop.getHash() == column.hashCode();
    }


    private ComponentColumn.Type getColumnType( int i ) {
        ComponentColumn.Type type;
        if ( i == 0 ) {
            type = ComponentColumn.Type.FIRST;
        } else {
            type = ComponentColumn.Type.MIDDLE;
        }
        return type;
    }

    public void resizeColumns( @Observes ColumnResizeEvent resize ) {
        if ( resize.getRowHashCode() == hashCode() ) {
            //FIXME CAST
            ComponentColumn resizeColumn = ( ComponentColumn ) getColumn( resize );

            if ( resizeColumn != null ) {
                ComponentColumn affectedColumn = ( ComponentColumn ) columns
                        .get( columns.indexOf( resizeColumn ) - 1 );

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
        GWT.log( "UPDATE VIEW" + hashCode() );
        repaintContainerEvent.fire( new RepaintContainerEvent() );
    }

    public UberView<Row> getView() {
        view.clear();
        for ( Column column : columns ) {
            view.addColumn( column.getView() );
        }
        return view;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
