package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.Column;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ColumnWithComponents;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ComponentColumn;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RowTest {

    private Row row;
    private Row.View view;
    private Instance<ComponentColumn> columnInstance;
    private Instance<ColumnWithComponents> columnWithComponentsInstance;
    private DnDManager dndManager;
    private Event<RepaintContainerEvent> repaintContainerEventEvent;
    private ParameterizedCommand<RowDrop> dropOnRowCommand;
    private ParameterizedCommand<String> existentComponentDropCommand;
    private ComponentColumn columnMock1;
    private ComponentColumn columnMock2;
    private ComponentColumn columnMock3;

    @Before
    public void setup() {
        view = mock( Row.View.class );
        columnMock1 = new ComponentColumn( mock( ComponentColumn.View.class ) );
        columnMock2 = new ComponentColumn( mock( ComponentColumn.View.class ) );
        columnMock3 = new ComponentColumn( mock( ComponentColumn.View.class ) );
        columnInstance = mock( Instance.class );
        when( columnInstance.get() ).thenReturn( columnMock1 ).thenReturn( columnMock2 ).thenReturn( columnMock3 );
        columnWithComponentsInstance = mock( Instance.class );
        dndManager = mock( DnDManager.class );
        repaintContainerEventEvent = mock( Event.class );
        dropOnRowCommand = mock( ParameterizedCommand.class );
        existentComponentDropCommand = mock( ParameterizedCommand.class );
        row = spy(
                new Row( view, columnInstance, columnWithComponentsInstance, repaintContainerEventEvent, dndManager ) );
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
        assertEquals( Row.COLUMN_DEFAULT_SIZE, columnMock1.getSize().intValue() );
        ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand = row.dropCommand();

        int dropTargetHash = columnMock1.hashCode();

        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.RIGHT,
                                          "sampleDndScreen" ) );

        assertEquals( 2, row.getColumns().size() );
        assertEquals( columnMock1, row.getColumns().get( 0 ) );
        assertEquals( columnMock2, row.getColumns().get( 1 ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, columnMock1.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, columnMock2.getSize().intValue() );

        columnDropParameterizedCommand
                .execute( new ColumnDrop( dropTargetHash, ColumnDrop.Orientation.LEFT,
                                          "sampleDndScreen" ) );

        assertEquals( 3, row.getColumns().size() );
        assertEquals( columnMock3, row.getColumns().get( 0 ) );
        assertEquals( columnMock1, row.getColumns().get( 1 ) );
        assertEquals( columnMock2, row.getColumns().get( 2 ) );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 4, columnMock3.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 4, columnMock1.getSize().intValue() );
        assertEquals( Row.COLUMN_DEFAULT_SIZE / 2, columnMock2.getSize().intValue() );

        verify( row, times( 2 ) ).updateView();
    }
}