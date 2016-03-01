package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.mvp.ParameterizedCommand;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ComponentColumnTest {

    public static final int ORIGINAL_SIZE = 12;
    private ComponentColumn componentColumn;
    private DnDManager dndManager;
    private ComponentColumn.View view;
    private int parentHashCode;
    private ParameterizedCommand command;

    @Before
    public void setup() {
        dndManager = mock( DnDManager.class );
        view = mock( ComponentColumn.View.class );
        componentColumn = new ComponentColumn( view, dndManager );
        parentHashCode = -1;
        command = mock( ParameterizedCommand.class );
        componentColumn.init( parentHashCode, ComponentColumn.Type.FIRST_COLUMN, ORIGINAL_SIZE,
                              command, "place" );
    }

    @Test
    public void reduceIncrementSize() {
        assertEquals( ORIGINAL_SIZE, componentColumn.getSize().intValue() );
        componentColumn.reduzeSize();
        assertEquals( ORIGINAL_SIZE - 1, componentColumn.getSize().intValue() );
        componentColumn.incrementSize();
        assertEquals( ORIGINAL_SIZE, componentColumn.getSize().intValue() );
        verify( view, times( 2 ) ).setSize( any() );
    }

    @Test
    public void setSize() {
        assertEquals( ORIGINAL_SIZE, componentColumn.getSize().intValue() );
        componentColumn.setSize( 11 );
        assertEquals( 11, componentColumn.getSize().intValue() );
        verify( view, times( 1 ) ).setSize( any() );
    }

    @Test
    public void onMouseDownTriggersResizeCol() {
        componentColumn.setColumnType( ComponentColumn.Type.MIDDLE );
        componentColumn.onMouseDown( 10 );
        verify( dndManager ).beginColumnResize( componentColumn.hashCode(), 10 );
    }


    @Test
    public void onMouseDownTriggersResizeRow() {
        componentColumn.setColumnType( ComponentColumn.Type.FIRST_COLUMN );
        componentColumn.onMouseDown( 10 );
        verify( dndManager ).beginRowMove( parentHashCode );
    }

    @Test
    public void onMouseDownEndResizeCol() {
        componentColumn.setColumnType( ComponentColumn.Type.MIDDLE );
        componentColumn.onMouseUp( 10 );
        verify( dndManager ).endColumnResize( parentHashCode, 10 );
    }

    @Test
    public void onDropTriggersCommand() {
        componentColumn.onDrop( ColumnDrop.Orientation.RIGHT, "dnd" );
        verify( command ).execute( any() );
    }

}