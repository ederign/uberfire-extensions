package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ColumnView extends Composite
        implements UberView<Column>,
        Column.View {

    public static final String COL_CSS_CLASS = "col-md-";
    private Column presenter;

    @DataField
    private Element col = DOM.createDiv();

    @DataField
    private Element content = DOM.createDiv();

    String cssSize = "";

    @Override
    public void init( Column presenter ) {
        this.presenter = presenter;
        content.getStyle().setCursor( Style.Cursor.DEFAULT );
        col.getStyle().setCursor( Style.Cursor.COL_RESIZE );
//        test();
    }

    @Override
    public void setSize( String size ) {
        if ( !col.getClassName().isEmpty() ) {
            col.removeClassName( cssSize );
        }
        cssSize = COL_CSS_CLASS + size;
        col.addClassName( cssSize );
        col.addClassName( "no-padding" );
    }

    @Override
    public void setContent( String contentLabel ) {
        DivElement.as( content ).appendChild( new Label( contentLabel ).getElement() );
    }


    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;

    @EventHandler( "col" )
    public void colMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE DOWN" );
//        presenter.beginResize( e.getClientX() );
    }

    @EventHandler( "col" )
    public void colMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE UP" );
//        presenter.beginResize( e.getClientX() );
    }

//    @EventHandler( "col" )
    public void colMouseOver( MouseMoveEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE OVER" );
//        setMouseCursor( e.getClientX() );
//        presenter.beginResize( e.getClientX() );
    }

//    private void setMouseCursor( int clientX ) {
//        if ( isOnResizePosition( clientX ) ) {
//            GWT.log( "ROW_RESIZE" );
//            col.getElement().addClassName( "cursorResize" );
//            col.getElement().getStyle().setCursor( Style.Cursor.ROW_RESIZE );
//        } else {
//            GWT.log( "DEFAULT" );
//            col.getElement().removeClassName( "cursorResize" );
//            col.getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
//        }
//    }

//    private boolean isOnResizePosition( int clientX ) {
//        int delta = getOffsetWidth() / 10;
//        final boolean b1 = clientX > col.getAbsoluteLeft();
//        final boolean b2 = clientX <= col.getAbsoluteLeft() + delta;
//        if ( b1 && b2 ) {
//            return true;
//        }
//        return false;
//    }


    //    @EventHandler( "col" )
    public void dndEndOnMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        presenter.endResize( e.getClientX() );
    }


    @EventHandler( "col" )
    public void click( ClickEvent e ) {
        GWT.log( "Click" );
    }

    @EventHandler( "col" )
    public void dragOverEvent( DragOverEvent e ) {
        e.preventDefault();
    }

    @EventHandler( "col" )
    public void dropColumnEvent( DropEvent e ) {
        GWT.log( "DROP COLUMN EVENT" );
        e.preventDefault();
        presenter.onDrop( e.getNativeEvent().getClientX(), calculateMiddle() );
    }

    private int calculateMiddle() {
        return getAbsoluteLeft() + ( getOffsetWidth() / 2 );
    }

    //    @EventHandler( "col" )
    public void col( DragOverEvent e ) {
        e.preventDefault();
//        presenter.onDrop();
    }


}