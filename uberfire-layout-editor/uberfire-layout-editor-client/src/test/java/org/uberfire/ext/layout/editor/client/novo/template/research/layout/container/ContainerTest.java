package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.EmptyDropRow;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
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
    private Row rowMock;

    @Before
    public void setup() {
        view = mock( Container.View.class );
        rowInstance = mock( Instance.class );
        rowMock = mock( Row.class );
        when( rowInstance.get() ).thenReturn( rowMock );
        emptyDropRowInstance = mock( Instance.class );
        emptyDropRow = mock( EmptyDropRow.class );
        when( emptyDropRowInstance.get() ).thenReturn( emptyDropRow );
        container = new Container( view, rowInstance, emptyDropRowInstance );
    }

    @Test
    public void initProcess() {
        assertNull( container.getEmptyDropRow() );
        container.init();
        assertNotNull( container.getEmptyDropRow() );
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
        verify( rowMock ).init( any( ParameterizedCommand.class ), any( ParameterizedCommand.class ) );
        verify( rowMock ).withOneColumn( drop );
    }


    @Test
    public void existentComponentRemovalCommand(){
        Container spy = spy( container );
        when( rowMock.removeColumn( any() ) ).thenReturn( true );
        initContainerWithMockRow( spy );

        assertFalse(spy.getRows().isEmpty());

        ParameterizedCommand<String> existentComponentRemovalCommand = spy.existentComponentRemovalCommand();
        existentComponentRemovalCommand.execute( "anyParameterBecauseIsMocked" );

        assertTrue(spy.getRows().isEmpty());
    }

    @Test
    public void createRegularRowDropCommand(){

    }

    @Test
    public void swapRowsTest(){

    }

    private void initContainerWithMockRow( Container spy ) {
        spy.init();
        ParameterizedCommand<RowDrop> emptyDropCommand = spy.createEmptyDropCommand();
        RowDrop drop = new RowDrop( 0, RowDrop.Orientation.AFTER );
        emptyDropCommand.execute( drop );
    }
}