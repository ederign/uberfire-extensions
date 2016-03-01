package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.Column;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ColumnWithComponents;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ComponentColumn;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnResizeEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens.Screens;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RowTest {

    private Row row;
    private Row otherRow;
    private Row.View view;
    private Instance<ComponentColumn> columnInstance;
    private Instance<ColumnWithComponents> columnWithComponentsInstance;
    private Instance<Row> rowInstance;
    private DnDManager dndManager;
    private Event<RepaintContainerEvent> repaintContainerEventEvent;
    private ParameterizedCommand<RowDrop> dropOnRowCommand;
    private ParameterizedCommand<String> existentComponentDropCommand;
    private ComponentColumn componentColumnMock1;
    private ComponentColumn componentColumnMock2;
    private ComponentColumn componentColumnMock3;
    private ComponentColumn componentColumnMock4;
    private ColumnWithComponents columnWithComponents1;

    @Before
    public void setup() {
        view = mock( Row.View.class );
        componentColumnMock1 = new ComponentColumn( mock( ComponentColumn.View.class ), mock( DnDManager.class ) );
        componentColumnMock2 = new ComponentColumn( mock( ComponentColumn.View.class ), mock( DnDManager.class ) );
        componentColumnMock3 = new ComponentColumn( mock( ComponentColumn.View.class ), mock( DnDManager.class ) );
        componentColumnMock4 = new ComponentColumn( mock( ComponentColumn.View.class ), mock( DnDManager.class ) );
        componentColumnMock4 = new ComponentColumn( mock( ComponentColumn.View.class ), mock( DnDManager.class ) );
        rowInstance = mock( Instance.class );

        columnWithComponents1 = new ColumnWithComponents( mock( ColumnWithComponents.View.class ), rowInstance );

        columnInstance = mock( Instance.class );
        when( columnInstance.get() ).thenReturn(
                componentColumnMock1 ).thenReturn( componentColumnMock2 ).thenReturn( componentColumnMock3 )
                .thenReturn( componentColumnMock4 );

        columnWithComponentsInstance = mock( Instance.class );

        dndManager = mock( DnDManager.class );
        repaintContainerEventEvent = mock( Event.class );
        dropOnRowCommand = mock( ParameterizedCommand.class );
        existentComponentDropCommand = mock( ParameterizedCommand.class );


        when( columnWithComponentsInstance.get() ).thenReturn( columnWithComponents1 );

        row = spy(
                new Row( view, columnInstance, columnWithComponentsInstance, repaintContainerEventEvent, dndManager ) );
        otherRow = spy(
                new Row( view, columnInstance, columnWithComponentsInstance, repaintContainerEventEvent, dndManager ) );

        when( rowInstance.get() ).thenReturn( otherRow );


    }

    @Test
    public void addColumns() {
        row.init( dropOnRowCommand, existentComponentDropCommand );

        assertFalse( row.hasColumns() );
        row.addColumns( mock( Column.class ), mock( Column.class ) );
        assertTrue( row.hasColumns() );
        assertEquals( 2, row.getColumns().size() );
    }

    @Test
    public void withOneColumn() {
        row.init( dropOnRowCommand, existentComponentDropCommand );
        row.withOneColumn( mock( RowDrop.class ) );
        assertTrue( row.hasColumns() );
        assertEquals( 1, row.getColumns().size() );
    }

    @Test
    public void tempIsDndDataValid() {
        //TODO
    }

    @Test
    public void sideColumnDrop() {
        row.init( dropOnRowCommand, existentComponentDropCommand );
        row.withOneColumn( mock( RowDrop.class ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE, componentColumnMock1.getSize().intValue() );
        ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand = row.dropCommand();


        //drop on the right of first column
        int dropTargetHash = componentColumnMock1.hashCode();
        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.RIGHT,
                                          "sampleDndScreen1" ) );

        assertEquals( 2, row.getColumns().size() );
        assertEquals( componentColumnMock1, row.getColumns().get( 0 ) );
        assertEquals( componentColumnMock2, row.getColumns().get( 1 ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, componentColumnMock1.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, componentColumnMock2.getSize().intValue() );

        //drop on the left first column
        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.LEFT,
                                          "sampleDndScreen2" ) );

        assertEquals( 3, row.getColumns().size() );
        assertEquals( componentColumnMock3, row.getColumns().get( 0 ) );
        assertEquals( componentColumnMock1, row.getColumns().get( 1 ) );
        assertEquals( componentColumnMock2, row.getColumns().get( 2 ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 4, componentColumnMock3.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 4, componentColumnMock1.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, componentColumnMock2.getSize().intValue() );

        //drop on the right first column
        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.RIGHT,
                                          "sampleDndScreen3" ) );

        assertEquals( 4, row.getColumns().size() );
        assertEquals( componentColumnMock3, row.getColumns().get( 0 ) );
        assertEquals( componentColumnMock1, row.getColumns().get( 1 ) );
        assertEquals( componentColumnMock4, row.getColumns().get( 2 ) );
        assertEquals( componentColumnMock2, row.getColumns().get( 3 ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 4, componentColumnMock3.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 6, componentColumnMock1.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 12, componentColumnMock4.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, componentColumnMock2.getSize().intValue() );

        verify( row, times( 3 ) ).updateView();
    }

    @Test
    public void innerColumnDrop() {
        row.init( dropOnRowCommand, existentComponentDropCommand );
        row.withOneColumn( mock( RowDrop.class ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE, componentColumnMock1.getSize().intValue() );

        ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand = row.dropCommand();

        //drop on the right first column
        columnDropParameterizedCommand
                .execute( new ColumnDrop( row.getColumns().get( 0 ).hashCode(), ColumnDrop.Orientation.RIGHT,
                                          "sampleDndScreen1" ) );

        assertEquals( 2, row.getColumns().size() );


        //drop up second column
        int dropTargetHash = componentColumnMock2.hashCode();
        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.UP,
                                          "sampleDndScreen2" ) );


        assertEquals( 2, row.getColumns().size() );

        assertTrue( row.getColumns().get( 1 ) instanceof ColumnWithComponents );
        ColumnWithComponents column = ( ColumnWithComponents ) row.getColumns().get( 1 );
        assertTrue( column.hasRow() );
        Row innerRowColumn2Component = column.getRow();
        assertEquals( componentColumnMock3, innerRowColumn2Component.getColumns().get( 0 ) );
        assertEquals( componentColumnMock2, innerRowColumn2Component.getColumns().get( 1 ) );

        //other drop down second column, should keep the same column and add a component
        columnDropParameterizedCommand
                .execute( new ColumnDrop( componentColumnMock2.hashCode(), ColumnDrop.Orientation.DOWN,
                                          "sampleDndScreen3" ) );
        assertEquals( 2, row.getColumns().size() );
        Row innerRowColumn3Components = column.getRow();
        assertEquals( innerRowColumn2Component, innerRowColumn3Components );
        assertEquals( componentColumnMock3, innerRowColumn2Component.getColumns().get( 0 ) );
        assertEquals( componentColumnMock2, innerRowColumn2Component.getColumns().get( 1 ) );
        assertEquals( componentColumnMock4, innerRowColumn2Component.getColumns().get( 2 ) );

        verify( row, times( 3 ) ).updateView();
    }

    @Test
    public void removeColumnTest() {
        row.init( dropOnRowCommand, existentComponentDropCommand );
        row.withOneColumn( mock( RowDrop.class ) );
        ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand = row.dropCommand();

        //drop on the right first column
        columnDropParameterizedCommand
                .execute( new ColumnDrop( row.getColumns().get( 0 ).hashCode(), ColumnDrop.Orientation.RIGHT,
                                          Screens.AnotherScreen.name() ) );

        assertEquals( 2, row.getColumns().size() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 0 ).getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 1 ).getSize().intValue() );

        row.removeColumn( "doNothing" );

        assertEquals( 2, row.getColumns().size() );

        row.removeColumn( Screens.AnotherScreen.name() );
        assertEquals( 1, row.getColumns().size() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE, row.getColumns().get( 0 ).getSize().intValue() );

        verify( row, times( 1 ) ).updateView();
    }

    @Test
    public void resizeColumnsTest() {
        row.init( dropOnRowCommand, existentComponentDropCommand );
        row.withOneColumn( mock( RowDrop.class ) );
        ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand = row.dropCommand();

        columnDropParameterizedCommand
                .execute( new ColumnDrop( row.getColumns().get( 0 ).hashCode(), ColumnDrop.Orientation.RIGHT,
                                          Screens.AnotherScreen.name() ) );

        assertEquals( 2, row.getColumns().size() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 0 ).getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 1 ).getSize().intValue() );


        row.resizeColumns( new ColumnResizeEvent( componentColumnMock2.hashCode(), row.hashCode() ).right() );

        assertEquals( ( Row.COLUMN_DEFAULT_SIZE / 2 + 1 ), row.getColumns().get( 0 ).getSize().intValue() );
        assertEquals( ( Row.COLUMN_DEFAULT_SIZE / 2 - 1 ), row.getColumns().get( 1 ).getSize().intValue() );

        row.resizeColumns( new ColumnResizeEvent( componentColumnMock2.hashCode(), row.hashCode() ).left() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 0 ).getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, row.getColumns().get( 1 ).getSize().intValue() );

        verify( row, times( 3 ) ).updateView();
    }
}