package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;

import javax.enterprise.inject.Instance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ColumnWithComponentsTest {

    private ColumnWithComponents column;
    private Instance<Row> rowInstance;
    private Row rowMock;

    @Before
    public void setup() {
        rowInstance = mock( Instance.class );
        rowMock = mock( Row.class );
        when( rowInstance.get() ).thenReturn( rowMock );
        column = new ColumnWithComponents( mock( ColumnWithComponents.View.class ), rowInstance );
    }

    @Test
    public void initTest() {
        int parentHashCode = -1;
        column.init( parentHashCode, 6 );
        assertEquals( parentHashCode, column.getParentHashCode() );
        assertEquals( 6, column.getSize().intValue() );
    }

    @Test
    public void incrementReduceSize() {
        int parentHashCode = -1;
        column.init( parentHashCode, 6 );
        assertEquals( 6, column.getSize().intValue() );
        column.reduzeSize();
        assertEquals( 5, column.getSize().intValue() );
        column.incrementSize();
        assertEquals( 6, column.getSize().intValue() );
    }

    @Test
    public void removeIfExists() {
        //TODO implement it
        column.removeIfExists( "place" );
    }

    @Test
    public void withComponents() {
        column.withComponents( mock( Column.class ), mock( Column.class ) );
        verify( rowMock ).disableDrop();
        verify( rowMock ).init( any(), any() );
        verify( rowMock ).addColumns( any(), any() );
    }

    @Test
    public void componentRemovalCommand() {
        //TODO implement this if necessary
        column.componentRemovalCommand().execute( "parameter" );
    }

    @Test
    public void createDropCommand() {
        //TODO implement it if necessary
        column.createDropCommand().execute( mock( RowDrop.class ) );
    }
}