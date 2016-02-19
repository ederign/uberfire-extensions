package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.Column;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ComponentColumn;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.NoDropColumn;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnResizeEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
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
    Instance<NoDropColumn> noDropColumnInstance;


    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    @Inject
    DnDManager dndManager;

    public interface View extends UberView<Row> {

        void addColumn( UberView<ComponentColumn> view );

        void clear();

    }

    private ParameterizedCommand<RowDrop> dropCommand;

    public void init( ParameterizedCommand<RowDrop> dropCommand ) {
        this.dropCommand = dropCommand;
    }


    public void rowWithOneColumn() {
        final ComponentColumn column = createColumn();
        column. columnFromDrop( dropCommand() );
        columns.add( column );
        updateView();
    }

    private ComponentColumn createColumn() {
        final ComponentColumn column = columnInstance.get();
        column.setParentHashCode( hashCode() );
        return column;
    }


    private NoDropColumn createNoDropColumn() {
        final NoDropColumn column = noDropColumnInstance.get();
        column.setParentHashCode( hashCode() );
        return column;
    }

    public void drop( RowDrop.Orientation orientation ) {
        dropCommand.execute( new RowDrop( hashCode(), orientation ) );
    }

    public void addColumns( Column... _columns ) {
        for ( Column column : _columns ) {
            columns.add(column);
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
                List<Column> columns = new ArrayList<Column>();
                GWT.log( "->" + Row.this.columns.size() );
                for ( int i = 0; i < Row.this.columns.size(); i++ ) {
                    //TODO Precisa colocar o side drop e convertar a column pra nodrop
                    ComponentColumn column = ( ComponentColumn ) Row.this.columns.get( i );

                    //TODO dont drop if the column size == 1
                    if ( dropIsOn( drop, column ) && column.getSize() != 1 ) {

                        if ( isASideDrop( drop ) ) {
                            handleSideDrop( drop, columns, i, column );
                        } else {
                            handleNewComponentDrop( columns, i, column );
                        }
                    } else {

                        columns.add( column );
                    }
                }
                Row.this.columns = columns;
                GWT.log( Row.this.columns.size() + " =1" );
                updateView();
            }
        };
    }

    private void handleNewComponentDrop( List<Column> columns, int columnIndex, ComponentColumn column ) {
        final NoDropColumn containerColumn = createNoDropColumn();
        containerColumn.containerColumn();
        containerColumn.init( column.getParentHashCode(), NoDropColumn.Type.FIRST, column.getSize(), dropCommand(), "" );

        final NoDropColumn newColumn = createNoDropColumn();
        newColumn.innerColumn();
        newColumn.init( column.getParentHashCode(), NoDropColumn.Type.FIRST, 12, dropCommand(), hashCode()+"" );

//TODO Precisa colocar o side drop e convertar a column pra nodrop
        column.setColumnType( getColumnType( 0 ) );
        column.innerColumn();
        column.setSize( 12 );

        containerColumn.withComponents( column, newColumn );
        columns.add( containerColumn );

        column.recalculateSize();
    }

    private void handleSideDrop( ColumnDrop drop, List<Column> columns, int columnINdex, ComponentColumn column ) {
        Integer originalSize = column.getSize();
        Integer newColumnSize = originalSize / 2;

        if ( originalSize % 2 == 0 ) {
            column.setSize( newColumnSize );
        } else {
            column.setSize( newColumnSize + 1 );
        }

        if ( drop.getOrientation() == ColumnDrop.Orientation.LEFT ) {
            final ComponentColumn newColumn = createColumn();
            ComponentColumn.Type type = getColumnType( columnINdex );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand(),
                            "Parent: " + column.getParentHashCode() + " " + hashCode() + "" );
            columns.add( newColumn );
            column.setColumnType( getColumnType( columnINdex + 1 ) );
            columns.add( column );

        } else {
            column.setColumnType( getColumnType( columnINdex ) );
            columns.add( column );
            final ComponentColumn newColumn = createColumn();
            ComponentColumn.Type type = getColumnType( columnINdex + 1 );
            newColumn.init( column.getParentHashCode(), type, newColumnSize, dropCommand(),
                            "Parent: " + column.getParentHashCode() + " " + hashCode() + "" );
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


    private void createColumns( Integer... colSpans ) {
        for ( int i = 0; i < colSpans.length; i++ ) {
            Integer colSpan = colSpans[i];
            final ComponentColumn column = createColumn();
            ComponentColumn.Type type = getColumnType( i );
            column.init( hashCode(), type, colSpan, dropCommand(), " " + hashCode() + "" );
            columns.add( column );
        }
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
                ComponentColumn affectedColumn = ( ComponentColumn ) columns.get( columns.indexOf( resizeColumn ) - 1 );

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
        view.clear();
        for ( Column column : columns ) {
            view.addColumn( column.getView() );
        }
        return view;
    }

}
