package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith( GwtMockitoTestRunner.class )
public class ColumnWithComponentsViewTest {

    ColumnWithComponentsView view;
    ColumnWithComponents presenter;
    FlowPanel col;
    Element element = mock( Element.class );

    @Before
    public void setup() {
        view = new ColumnWithComponentsView();
        presenter = mock( ColumnWithComponents.class );
        col = mock( FlowPanel.class );
        view.init( presenter );
        view.col = col;
    }

    @Test
    public void setSizeTest() {
        when( col.getElement() ).thenReturn( element );

        assertEquals( "", view.cssSize );

        view.setSize( "12" );

        assertEquals( ColumnWithComponentsView.COL_CSS_CLASS + 12, view.cssSize );
        verify( element ).addClassName( view.cssSize );

        view.setSize( "6" );

        assertEquals( ColumnWithComponentsView.COL_CSS_CLASS + 6, view.cssSize );
        verify( element ).addClassName( view.cssSize );
    }
}
