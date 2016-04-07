package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DropEvent;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;
import org.uberfire.ext.layout.editor.client.dnd.DndDataJSONConverter;
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

    private DndDataJSONConverter converter = new DndDataJSONConverter();

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
        column.init( hashCode(), ComponentColumn.Type.FIRST_COLUMN, COLUMN_DEFAULT_SIZE,
                     dropCommand(), Screens.next().name(), drop.getComponent() );
        columns.add( column );
    }

    private ComponentColumn createColumn() {
        final ComponentColumn column = columnInstance.get();
        return column;
    }

    ParameterizedCommand<ColumnDrop> dropCommand() {
        return ( drop ) -> {
            //ederign
//            if ( tempIsDndDataValid( drop.getDndData() ) ) {
//                notifyThePossibilityOfDropAndExistentComponent.execute( drop.getDndData() );
//            }
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
            if ( isComponentColumn( column ) ) {
                ComponentColumn c = ( ComponentColumn ) column;
                if ( c.getPlace().equals( placeName ) ) {
                    columnToRemove = column;
                }
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
        if ( isComponentColumn( columnToRemove ) ) {
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
                    if ( isComponentColumn( columnToRemove ) ) {
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

    public void drop( DropEvent dropEvent, RowDrop.Orientation orientation ) {
        LayoutDragComponent component = extractComponent( dropEvent );
        if ( thereIsAComponent( component ) ) {
            dropOnRowCommand.execute( new RowDrop( component, hashCode(), orientation ) );
        }
    }

    //ederign leak gwt
    private LayoutDragComponent extractComponent( DropEvent dropEvent ) {
        return converter
                .readJSONDragComponent( dropEvent.getData( LayoutDragComponent.FORMAT ) );
    }

    private boolean thereIsAComponent( LayoutDragComponent component ) {
        return component != null;
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


    private List<Column> updateColumns( ColumnDrop drop, List<Column> originalColumns ) {
        List<Column> columns = new ArrayList<>();

        for ( int i = 0; i < originalColumns.size(); i++ ) {

            final Column currentColumn = originalColumns.get( i );
            if ( isComponentColumn( currentColumn ) ) {
                handleDropOnComponentColumn( drop, columns, i, currentColumn );
            } else {
                handleDropOnColumnWithComponents( drop, columns,
                                                  ( ColumnWithComponents ) currentColumn );

            }

        }
        return columns;
    }

    private void handleDropOnColumnWithComponents( ColumnDrop drop, List<Column> columns,
                                                   ColumnWithComponents currentColumn ) {
        ColumnWithComponents column = currentColumn;
        if ( column.hasRow() ) {
            Row innerRow = column.getRow();
            innerRow.columns = updateInnerColumns( drop, innerRow.getColumns(), column );
        }
        columns.add( column );
    }

    private List<Column> updateInnerColumns( ColumnDrop drop, List<Column> originalColumns,
                                             ColumnWithComponents parentColumn ) {
        List<Column> columns = new ArrayList<>();

        for ( int i = 0; i < originalColumns.size(); i++ ) {
            final Column currentColumn = originalColumns.get( i );
            if ( isComponentColumn( currentColumn ) ) {
                handleDropInnerColumn( drop, columns, i, currentColumn, parentColumn );
            }
        }
        return columns;
    }


    private void handleDropInnerColumn( ColumnDrop drop, List<Column> columns, int i, Column column,
                                        ColumnWithComponents parentColumn ) {
        ComponentColumn currentColumn = ( ComponentColumn ) column;
        if ( dropIsOn( drop, currentColumn ) && columnCanBeSplitted( currentColumn ) ) {
            if ( drop.isASideDrop() ) {
                handleSideDrop( drop, columns, i, currentColumn );
            } else {
                handleInnerDrop( drop, columns, currentColumn );
            }
        } else {
            columns.add( currentColumn );
        }
    }

    private void handleInnerDrop( ColumnDrop drop, List<Column> columns, ComponentColumn currentColumn ) {
        final ComponentColumn newColumn = createNewInnerColumn( drop, currentColumn );
        if ( drop.isADownDrop() ) {
            columns.add( currentColumn );
            columns.add( newColumn );
        } else {
            columns.add( newColumn );
            columns.add( currentColumn );
        }
    }

    private ComponentColumn createNewInnerColumn( ColumnDrop drop, ComponentColumn currentColumn ) {
        final ComponentColumn newColumn = createColumn();
        newColumn.init( currentColumn.getParentHashCode(), getColumnType( 0 ), COLUMN_DEFAULT_SIZE,
                        dropCommand(),
                        null, drop.getComponent() ); //TODO ederign
        newColumn.setSize( currentColumn.getPanelSize() );
        return newColumn;
    }

    private void handleInnerComponentDrop( ColumnDrop drop, List<Column> columns,
                                           ComponentColumn currentColumn ) {
        final ColumnWithComponents columnWithComponents = createColumnWithComponents();
        columnWithComponents.init( hashCode(), currentColumn.getSize() );

        final ComponentColumn newColumn = createNewComponentColumn( drop, currentColumn );
        currentColumn = updateCurrentColumn( currentColumn );

        if ( drop.isADownDrop() ) {
            columnWithComponents.withComponents( currentColumn, newColumn );
        } else {
            columnWithComponents.withComponents( newColumn, currentColumn );
        }

        columns.add( columnWithComponents );

    }

    private ComponentColumn updateCurrentColumn( ComponentColumn currentColumn ) {
        currentColumn.setSize( 12 );
        currentColumn.halfParentPanelSize( currentColumn.getPanelSize() );
        currentColumn.recalculateSize();
        currentColumn.setColumnType( getColumnType( 0 ) );
        return currentColumn;
    }

    private ComponentColumn createNewComponentColumn( ColumnDrop drop, ComponentColumn currentColumn ) {
        final ComponentColumn newColumn = createColumn();
        newColumn.init( currentColumn.getParentHashCode(), getColumnType( 0 ), 12, dropCommand(),
                        null, drop.getComponent() ); //todo ederign
        newColumn.halfParentPanelSize( currentColumn.getPanelSize() );
        return newColumn;
    }

    private ComponentColumn createNewComponentColumn( ColumnDrop drop, ComponentColumn currentColumn,
                                                      int newColumnIndex, Integer columnSize ) {
        final ComponentColumn newColumn = createColumn();
        newColumn.init( currentColumn.getParentHashCode(), getColumnType( newColumnIndex ), columnSize, dropCommand(),
                        null, drop.getComponent() ); //todo ederign
        newColumn.setPanelSize( currentColumn.getPanelSize() );
        return newColumn;
    }

    private void handleDropOnComponentColumn( ColumnDrop drop, List<Column> columns, int i, Column column ) {
        ComponentColumn componentColumn = ( ComponentColumn ) column;
        if ( dropIsOn( drop, componentColumn ) && columnCanBeSplitted( componentColumn ) ) {
            if ( drop.isASideDrop() ) {
                handleSideDrop( drop, columns, i, componentColumn );
            } else {
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

        if ( drop.isALeftDrop() ) {
            final ComponentColumn newColumn = createNewComponentColumn( drop, currentColumn, columnIndex,
                                                                        currentColumn.getSize() / 2 );
            currentColumn = updateCurrentColumn( currentColumn, ( columnIndex + 1 ) );

            columns.add( newColumn );
            columns.add( currentColumn );

        } else {
            final ComponentColumn newColumn = createNewComponentColumn( drop, currentColumn, columnIndex + 1,
                                                                        currentColumn.getSize() / 2 );
            currentColumn = updateCurrentColumn( currentColumn, columnIndex );

            columns.add( currentColumn );
            columns.add( newColumn );
        }
    }


    private ComponentColumn updateCurrentColumn( ComponentColumn currentColumn, int columnIndex ) {
        currentColumn.setColumnType( getColumnType( columnIndex + 1 ) );
        setupColumnSize( currentColumn );
        return currentColumn;
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


    private boolean dropIsOn( ColumnDrop drop, ComponentColumn column ) {
        return drop.getHash() == column.hashCode();
    }


    private ComponentColumn.Type getColumnType( int i ) {
        ComponentColumn.Type type;
        if ( i == 0 ) {
            type = ComponentColumn.Type.FIRST_COLUMN;
        } else {
            type = ComponentColumn.Type.MIDDLE;
        }
        return type;
    }

    public void resizeColumns( @Observes ColumnResizeEvent resize ) {
        if ( resizeEventIsinThisRow( resize ) ) {

            Column resizedColumn = getColumn( resize );

            if ( resizedColumn != null ) {
                Column affectedColumn = columns
                        .get( columns.indexOf( resizedColumn ) - 1 );
                if ( resize.isLeft() ) {
                    resizedColumn.incrementSize();
                    affectedColumn.reduzeSize();
                } else {
                    affectedColumn.incrementSize();
                    resizedColumn.reduzeSize();
                }
            }
            updateView();
        }
    }

    private boolean resizeEventIsinThisRow( @Observes ColumnResizeEvent resize ) {
        return resize.getRowHashCode() == hashCode();
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
            GWT.log("getView -> " );
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
