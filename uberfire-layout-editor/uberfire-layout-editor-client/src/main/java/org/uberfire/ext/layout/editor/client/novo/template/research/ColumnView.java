package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
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
    }

    @EventHandler( "col" )
    public void mouseUp( MouseUpEvent e ) {
        GWT.log("UP");
        e.preventDefault();
        dnd.end( col, e.getClientX() );
    }


    @EventHandler( "content" )
    public void click( ClickEvent e ) {
        consoleLog( "sdafsd" );
        GWT.log("click");
        e.preventDefault();
//        dnd.end( col, e.getClientX() );
    }


    @EventHandler( "col" )
    public void mouseDown( MouseDownEvent e ) {
        GWT.log("DOWN");
        e.preventDefault();
        dnd.begin( col, e.getClientX() );
    }

    native void consoleLog( String message ) /*-{
        console.log("log:" + message);
    }-*/;

}
