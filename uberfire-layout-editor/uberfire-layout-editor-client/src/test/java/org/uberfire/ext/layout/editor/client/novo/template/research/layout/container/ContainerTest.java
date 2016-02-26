package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.EmptyDropRow;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDnDEvent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.inject.Instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContainerTest {

    private Container container;
    private Container.View view;
    private Instance<Row> rowInstance;
    private Instance<EmptyDropRow> emptyDropRowInstance;
    private EmptyDropRow emptyDropRow;
    private Row rowMock1;
    private Row rowMock2;
    private Row rowMock3;

    @Before
    public void setup() {
        view = mock( Container.View.class );
        rowInstance = mock( Instance.class );
        rowMock1 = mock( Row.class );
        rowMock2 = mock( Row.class );
        rowMock3 = mock( Row.class );
        when( rowInstance.get() ).thenReturn( rowMock1 ).thenReturn( rowMock2 ).thenReturn( rowMock3 );
        emptyDropRowInstance = mock( Instance.class );
        emptyDropRow = mock( EmptyDropRow.class );
        when( emptyDropRowInstance.get() ).thenReturn( emptyDropRow );
        container = new Container( view, rowInstance, emptyDropRowInstance );
    }

    @Test
    public void viewInit() {
        container.post();
        verify( view ).init( container );
    }

    @Test
    public void initProcess() {
        assertNull( container.getEmptyDropRow() );
        container.init();
        assertNotNull( container.getEmptyDropRow() );
        assertEquals( view, container.getView() );
        verify( view ).addEmptyRow( container.getEmptyDropRow().getView() );
        verify( emptyDropRow ).init( any( ParameterizedCommand.class ) );
    }

    @Test
    public void emptyDropRowCommandTest() {
        Container spy = spy( container );
        spy.init();
        ParameterizedCommand<RowDrop> emptyDropCommand = spy.createEmptyDropCommand();

        assertTrue( spy.getRows().isEmpty() );

        RowDrop drop = new RowDrop( 0, RowDrop.Orientation.AFTER );
        emptyDropCommand.execute( drop );

        assertTrue( !spy.getRows().isEmpty() );
        verify( spy ).updateView();
        verify( rowMock1 ).init( any( ParameterizedCommand.class ), any( ParameterizedCommand.class ) );
        verify( rowMock1 ).withOneColumn( drop );
    }


    @Test
    public void removeComponentShouldRemoveRowWhenRowIsEmpty() {
        Container spy = spy( container );
        when( rowMock1.removeColumn( any() ) ).thenReturn( true );
        initContainerWithMockRow( spy );

        assertFalse( spy.getRows().isEmpty() );

        ParameterizedCommand<String> existentComponentRemovalCommand = spy.existentComponentRemovalCommand();
        existentComponentRemovalCommand.execute( "anyParameterBecauseIsMocked" );

        assertTrue( spy.getRows().isEmpty() );
    }


    @Test
    public void removeComponentShouldNotTRemoveRowWhenRowIsNotEmpty() {
        Container spy = spy( container );
        when( rowMock1.removeColumn( any() ) ).thenReturn( false );
        initContainerWithMockRow( spy );

        assertFalse( spy.getRows().isEmpty() );

        ParameterizedCommand<String> existentComponentRemovalCommand = spy.existentComponentRemovalCommand();
        existentComponentRemovalCommand.execute( "anyParameterBecauseIsMocked" );

        assertFalse( spy.getRows().isEmpty() );
    }

    @Test
    public void dropRowBefore() {
        Container spy = spy( container );
        initContainerWithMockRow( spy );

        assertEquals( 1, spy.getRows().size() );
        assertEquals( rowMock1, spy.getRows().get( 0 ) );

        ParameterizedCommand<RowDrop> rowDropCommand = spy.createRowDropCommand();
        rowDropCommand.execute( new RowDrop( rowMock1.hashCode(), RowDrop.Orientation.BEFORE ) );
        rowDropCommand.execute( new RowDrop( rowMock2.hashCode(), RowDrop.Orientation.AFTER ) );

        assertEquals( 3, spy.getRows().size() );
        assertEquals( rowMock2, spy.getRows().get( 0 ) );
        assertEquals( rowMock3, spy.getRows().get( 1 ) );
        assertEquals( rowMock1, spy.getRows().get( 2 ) );
        verify( spy, times( 3 ) ).updateView();
    }

    @Test
    public void dropRowAfter() {
        Container spy = spy( container );
        initContainerWithMockRow( spy );

        assertEquals( 1, spy.getRows().size() );
        assertEquals( rowMock1, spy.getRows().get( 0 ) );

        ParameterizedCommand<RowDrop> rowDropCommand = spy.createRowDropCommand();
        rowDropCommand.execute( new RowDrop( rowMock1.hashCode(), RowDrop.Orientation.AFTER ) );

        assertEquals( 2, spy.getRows().size() );
        assertEquals( rowMock1, spy.getRows().get( 0 ) );
        assertEquals( rowMock2, spy.getRows().get( 1 ) );
        verify( spy, times( 2 ) ).updateView();

    }

    @Test
    public void swapRowsTest() {
        Container spy = spy( container );
        initContainerWithMockRow( spy );
        addOtherRow( spy );

        assertEquals( rowMock1, spy.getRows().get( 0 ) );
        assertEquals( rowMock2, spy.getRows().get( 1 ) );

        spy.swapRows( new RowDnDEvent( rowMock1.hashCode(), rowMock2.hashCode() ) );

        assertEquals( rowMock2, spy.getRows().get( 0 ) );
        assertEquals( rowMock1, spy.getRows().get( 1 ) );

        spy.swapRows( new RowDnDEvent( rowMock1.hashCode(), rowMock1.hashCode() ) );

        assertEquals( rowMock2, spy.getRows().get( 0 ) );
        assertEquals( rowMock1, spy.getRows().get( 1 ) );

        int invalidHash = -32434232;
        spy.swapRows( new RowDnDEvent( invalidHash, invalidHash ) );

        assertEquals( rowMock2, spy.getRows().get( 0 ) );
        assertEquals( rowMock1, spy.getRows().get( 1 ) );

        spy.swapRows( new RowDnDEvent( rowMock1.hashCode(), rowMock2.hashCode() ) );
        assertEquals( rowMock1, spy.getRows().get( 0 ) );
        assertEquals( rowMock2, spy.getRows().get( 1 ) );

    }

    @Test
    public void repaintEventShouldUpdateView() {
        Container spy = spy( container );
        spy.repaintContainer( new RepaintContainerEvent() );
        verify( spy ).updateView();
    }

    @Test
    public void destroyShouldDestroyEverything() {
        Container spy = spy( container );
        doNothing().when( spy ).destroy( any() );
        initContainerWithMockRow( spy );
        ParameterizedCommand<RowDrop> rowDropCommand = spy.createRowDropCommand();
        rowDropCommand.execute( new RowDrop( rowMock1.hashCode(), RowDrop.Orientation.BEFORE ) );
        rowDropCommand.execute( new RowDrop( rowMock2.hashCode(), RowDrop.Orientation.AFTER ) );

        spy.preDestroy();

        verify( spy ).destroy( rowMock1 );
        verify( spy ).destroy( rowMock2 );
        verify( spy ).destroy( rowMock3 );
        verify( spy ).destroy( emptyDropRow );
    }

    private void addOtherRow( Container spy ) {
        ParameterizedCommand<RowDrop> rowDropCommand = spy.createRowDropCommand();
        rowDropCommand.execute( new RowDrop( rowMock1.hashCode(), RowDrop.Orientation.AFTER ) );
    }

    private void initContainerWithMockRow( Container spy ) {
        spy.init();
        ParameterizedCommand<RowDrop> emptyDropCommand = spy.createEmptyDropCommand();
        RowDrop drop = new RowDrop( 0, RowDrop.Orientation.AFTER );
        emptyDropCommand.execute( drop );
    }
}