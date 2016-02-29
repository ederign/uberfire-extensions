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

    public static final int COLUMN_DEFAULT_SIZE = 12;

    public interface View extends UberView<Row> {

        void addColumn( UberView<ComponentColumn> view );

        void clear();

    }

    private View view;

    private List<Column> columns = new ArrayList<Column>();

    private Instance<ComponentColumn> columnInstance;

    private Instance<ColumnWithComponents> columnWithComponentsInstance;

    private Event<RepaintContainerEvent> repaintContainerEvent;

    private ParameterizedCommand<RowDrop> dropOnRowCommand;

    private ParameterizedCommand<String> notifyThePossibilityOfDropAndExistentComponent;

    private DnDManager dndManager;

    private boolean dropEnable = true;

    @Inject
    public Row( View view, Instance<ComponentColumn> columnInstance,
                Instance<ColumnWithComponents> columnWithComponentsInstance,
                Event<RepaintContainerEvent> repaintContainerEvent, DnDManager dndManager ) {

        this.view = view;
        this.columnInstance = columnInstance;
        this.columnWithComponentsInstance = columnWithComponentsInstance;
        this.repaintContainerEvent = repaintContainerEvent;
        this.dndManager = dndManager;
    }

    public void init( ParameterizedCommand<RowDrop> dropOnRowCommand,
                      ParameterizedCommand<String> existentComponentDropCommand ) {
        this.dropOnRowCommand = dropOnRowCommand;
        this.notifyThePossibilityOfDropAndExistentComponent = existentComponentDropCommand;
    }

    public void addColumns( Column... _columns ) {
        for ( Column column : _columns ) {
            columns.add( column );
        }
    }

    public boolean hasColumns() {
        return !columns.isEmpty();
    }

    public void withOneColumn( RowDrop drop ) {
        final ComponentColumn column = createColumn();
        column.init( hashCode(), ComponentColumn.Type.FIRST, COLUMN_DEFAULT_SIZE,
                     dropCommand(), Screens.next().name() );
        columns.add( column );
    }

    private ComponentColumn createColumn() {
        final ComponentColumn column = columnInstance.get();
        return column;
    }

    ParameterizedCommand<ColumnDrop> dropCommand() {
        return ( drop ) -> {
            if ( tempIsDndDataValid( drop.getDndData() ) ) {
                notifyThePossibilityOfDropAndExistentComponent.execute( drop.getDndData() );
            }
            Row.this.columns = updateColumns( drop, Row.this.columns );
            updateView();
        };
    }

    boolean tempIsDndDataValid( String drop ) {
        //TODO validade to see if this is a drop of Layout Editor
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


    public boolean removeColumn( String placeName ) {
        Column columnToRemove = null;
        for ( Column column : columns ) {
            if ( column instanceof ComponentColumn ) {
                ComponentColumn c = ( ComponentColumn ) column;
                if ( c.getPlace().equals( placeName ) ) {
                    columnToRemove = column;
                }
            } else {
                //TODO
            }
        }
        if ( columnToRemove != null ) {
            setSizeOfSibilinColumn( columnToRemove );
            columns.remove( columnToRemove );
            return true;
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

    public void disableDrop() {
        this.dropEnable = false;
    }

    private ColumnWithComponents createColumnWithComponents() {
        final ColumnWithComponents column = columnWithComponentsInstance.get();
        return column;
    }

    public void drop( RowDrop.Orientation orientation ) {
        dropOnRowCommand.execute( new RowDrop( hashCode(), orientation ) );
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

    private List<Column> updateInnerColumns( ColumnDrop drop, List<Column> originalColumns,
                                             ColumnWithComponents parentColumn ) {
        List<Column> columns = new ArrayList<>();

        for ( int i = 0; i < originalColumns.size(); i++ ) {
            final Column currentColumn = originalColumns.get( i );
            if ( isComponentColumn( currentColumn ) ) {
                ComponentColumn column = ( ComponentColumn ) currentColumn;
                handleInner( drop, columns, i, column, parentColumn );
            }
        }
        return columns;
    }


    private List<Column> updateColumns( ColumnDrop drop, List<Column> originalColumns ) {
        List<Column> columns = new ArrayList<>();

        for ( int i = 0; i < originalColumns.size(); i++ ) {

            final Column currentColumn = originalColumns.get( i );
            if ( isComponentColumn( currentColumn ) ) {
                handleDropOnComponentColumn( drop, columns, i, currentColumn );
            } else {
                //next test
                ColumnWithComponents column = ( ColumnWithComponents ) currentColumn;
                if ( column.hasRow() ) {
                    Row row = column.getRow();
                    //check
                    row.columns = updateInnerColumns( drop, row.columns, column );
                }
                columns.add( column );

            }

        }
        return columns;
    }


    private void handleInner( ColumnDrop drop, List<Column> columns, int i, ComponentColumn column,
                              ColumnWithComponents parentColumn ) {
        //TODO dont drop (remove dnd) if the column size == 1
        GWT.log( "Handle inner" );
        if ( dropIsOn( drop, column ) && columnCanBeSplitted( column ) ) {
            if ( isASideDrop( drop ) ) {
                handleSideDrop( drop, columns, i, column );
            } else {

                Integer originalColumnUFSize = column.getPanelSize();
                String place = extractColumnPlace( drop );

                final ComponentColumn newColumn = createColumn();
                newColumn.init( column.getParentHashCode(), getColumnType( 0 ), COLUMN_DEFAULT_SIZE, dropCommand(),
                                place );
                //fixme
                newColumn.halfParentPanelSize( originalColumnUFSize * 2 );
                //TODO setar se foi em cima ou
                if ( drop.getOrientation() == ColumnDrop.Orientation.DOWN ) {
                    columns.add( column );
                    columns.add( newColumn );
                } else {
                    columns.add( newColumn );
                    columns.add( column );
                }

//                 copy of handleInnerComponentDrop( drop, columns, column );
            }
        } else {
            columns.add( column );
        }
    }

    private void handleDropOnComponentColumn( ColumnDrop drop, List<Column> columns, int i, Column column ) {
        ComponentColumn componentColumn = ( ComponentColumn ) column;
        if ( dropIsOn( drop, componentColumn ) && columnCanBeSplitted( componentColumn ) ) {
            if ( isASideDrop( drop ) ) {
                handleSideDrop( drop, columns, i, componentColumn );
            } else {
                //Next Test
                handleInnerComponentDrop( drop, columns, componentColumn );
            }
        } else {
            columns.add( componentColumn );
        }
    }

    private boolean columnCanBeSplitted( ComponentColumn componentColumn ) {
        return componentColumn.getSize() != 1;
    }


    private boolean isComponentColumn( Column currentColumn ) {
        return currentColumn instanceof ComponentColumn;
    }

    private void handleSideDrop( ColumnDrop drop, List<Column> columns, int columnIndex,
                                 ComponentColumn currentColumn ) {

        if ( dropIsOnTheLeftOfCurrentColumn( drop ) ) {
            final ComponentColumn newColumn = createNewComponentColumn( drop, currentColumn, columnIndex, currentColumn.getSize()/2 );
            currentColumn = updateCurrentColumn( currentColumn, ( columnIndex + 1 ) );

            columns.add( newColumn );
            columns.add( currentColumn );

        } else {
            final ComponentColumn newColumn = createNewComponentColumn( drop, currentColumn, columnIndex + 1, currentColumn.getSize()/2 );
            currentColumn =updateCurrentColumn( currentColumn, columnIndex );

            columns.add( currentColumn );
            columns.add( newColumn );
        }
    }

    private ComponentColumn createNewComponentColumn( ColumnDrop drop, ComponentColumn currentColumn,
                                                      int newColumnIndex, Integer columnSize ) {
        String place = extractColumnPlace( drop );
        final ComponentColumn newColumn = createColumn();
        ComponentColumn.Type type = getColumnType( newColumnIndex );
        newColumn.init( currentColumn.getParentHashCode(), type, columnSize, dropCommand(), place );
        newColumn.setPanelSize( currentColumn.getPanelSize() );
        return newColumn;
    }

    private ComponentColumn updateCurrentColumn( ComponentColumn currentColumn, int columnIndex ) {
        currentColumn.setColumnType( getColumnType( columnIndex + 1 ) );
        setupColumnSize( currentColumn );
        return currentColumn;
    }

    private boolean dropIsOnTheLeftOfCurrentColumn( ColumnDrop drop ) {
        return drop.getOrientation() == ColumnDrop.Orientation.LEFT;
    }

    private String extractColumnPlace( ColumnDrop drop ) {
        //TODO SHOULD BE REFACTORED TO REAL DATA
        return !tempIsDndDataValid( drop.getDndData() ) ? Screens.next().name() : drop.getDndData();
    }

    private Integer setupColumnSize( ComponentColumn column ) {
        Integer originalSize = column.getSize();
        Integer newColumnSize = originalSize / 2;
        if ( originalSize % 2 == 0 ) {
            column.setSize( newColumnSize );
        } else {
            column.setSize( newColumnSize + 1 );
        }
        return newColumnSize;
    }

    private void handleInnerComponentDrop( ColumnDrop drop, List<Column> columns,
                                           ComponentColumn column ) {
        final ColumnWithComponents columnWithComponents = createColumnWithComponents();
        columnWithComponents.init( hashCode(), column.getSize() );
        //uf bug
        Integer originalColumnUFSize = column.getPanelSize();
        String place = extractColumnPlace( drop );

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

    void updateView() {
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

    public boolean isDropEnable() {
        return dropEnable;
    }
}
