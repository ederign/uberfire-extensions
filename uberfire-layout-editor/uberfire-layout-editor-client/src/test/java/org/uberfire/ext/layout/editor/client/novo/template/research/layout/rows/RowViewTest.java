//package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;
//
//import com.google.gwt.event.dom.client.DragLeaveEvent;
//import com.google.gwt.event.dom.client.DragOverEvent;
//import com.google.gwt.event.dom.client.DropEvent;
//import com.google.gwt.event.dom.client.MouseOutEvent;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.ui.FlowPanel;
//import com.google.gwtmockito.GwtMockitoTestRunner;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.mockito.Mockito.*;
//
//@RunWith( GwtMockitoTestRunner.class )
//public class RowViewTest {
//
//    RowView rowView;
//    Row presenter;
//    FlowPanel content;
//    Element upper;
//    Element bottom;
//
//    @Before
//    public void setup() {
//        rowView = new RowView();
//        content = mock( FlowPanel.class );
//        upper = mock( Element.class );
//        bottom = mock( Element.class );
//        rowView.upper = upper;
//        rowView.bottom = bottom;
//        rowView.content = content;
//        presenter = mock( Row.class );
//        when( presenter.isDropEnable() ).thenReturn( true );
//        rowView.init( presenter );
//    }
//
//    @Test
//    public void dragOverUpper() {
//        rowView.dragOverUpper( mock( DragOverEvent.class ) );
//        verify( upper ).addClassName( RowView.ROW_DROP_PREVIEW );
//    }
//
//    @Test
//    public void dragLeaveUpper() {
//        rowView.dragLeaveUpper( mock( DragLeaveEvent.class ) );
//        verify( upper ).removeClassName( RowView.ROW_DROP_PREVIEW );
//    }
//
//    @Test
//    public void dropUpperEvent() {
//        rowView.dropUpperEvent( mock( DropEvent.class ) );
//        verify( upper ).removeClassName( RowView.ROW_DROP_PREVIEW );
//        verify( presenter ).drop( e, RowDrop.Orientation.BEFORE );
//    }
//
//    @Test
//    public void mouseOutUpper() {
//        rowView.mouseOutUpper( mock( MouseOutEvent.class ) );
//        verify( bottom ).removeClassName( RowView.ROW_DROP_PREVIEW );
//    }
//
//    @Test
//    public void dragOverBottom() {
//        rowView.dragOverBottom( mock( DragOverEvent.class ) );
//        verify( bottom ).addClassName( RowView.ROW_DROP_PREVIEW );
//    }
//
//    @Test
//    public void dropBottomEvent() {
//        rowView.dropBottomEvent( mock( DropEvent.class ) );
//        verify( bottom ).removeClassName( RowView.ROW_DROP_PREVIEW );
//        verify( presenter ).drop( e, RowDrop.Orientation.AFTER );
//    }
//
//    @Test
//    public void dragLeaveBottom() {
//        rowView.dragLeaveBottom( mock( DragLeaveEvent.class ) );
//        verify( bottom ).removeClassName( RowView.ROW_DROP_PREVIEW );
//    }
//
//    @Test
//    public void mouseOutBottom() {
//        rowView.mouseOutBottom( mock( MouseOutEvent.class ) );
//        verify( bottom ).removeClassName( RowView.ROW_DROP_PREVIEW );
//    }
//}
//
//
