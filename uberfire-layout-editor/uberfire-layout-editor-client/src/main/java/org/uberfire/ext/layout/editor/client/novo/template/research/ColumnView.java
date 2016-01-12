package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
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

    private Column presenter;

    @DataField
    private Element col = DOM.createDiv();

    @DataField
    private Element content = DOM.createDiv();

    //FIXME move to presenter
    @Inject
    ResizeManager dnd;


    @Override
    public void init( Column presenter ) {
        this.presenter = presenter;
        content.getStyle().setCursor( Style.Cursor.DEFAULT );
        col.getStyle().setCursor( Style.Cursor.COL_RESIZE );
    }

    @Override
    public void setSize( String size ) {
        col.addClassName( "col-md-" + size );

        //// FIXME: 1/12/16
        test();

    }

    public void test() {
        DivElement.as( content ).appendChild( new Label( hashCode() + "" ).getElement() );
    }

    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;

    @EventHandler( "col" )
    public void dndBeginOnMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        dnd.begin( col, e.getClientX() );
    }

    @EventHandler( "col" )
    public void dndEndOnMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        dnd.end( col, e.getClientX() );
    }

    @EventHandler( "col" )
    public void yo( DropEvent e ) {
//        GWT.log( "drop on content" );
        e.preventDefault();
        presenter.onDrop();
    }

    @EventHandler( "col" )
    public void col( DragOverEvent e ) {
        e.preventDefault();
//        presenter.onDrop();
    }

}