package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

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
    }

    @Override
    public void setCursor(){
        content.getStyle().setCursor( Style.Cursor.DEFAULT );
        if(presenter.canResize()){
            col.getStyle().setCursor( Style.Cursor.COL_RESIZE );
        }
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

    @EventHandler( "col" )
    public void colMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE DOWN" );
        presenter.onMouseDown(e.getClientX());
    }

    @EventHandler( "col" )
    public void colMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE UP" );
        presenter.onMouseUp(e.getClientX());
    }

    //    @EventHandler( "col" )
    public void colMouseOver( MouseMoveEvent e ) {
        e.preventDefault();
        GWT.log( "COL MOUSE OVER" );
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


}