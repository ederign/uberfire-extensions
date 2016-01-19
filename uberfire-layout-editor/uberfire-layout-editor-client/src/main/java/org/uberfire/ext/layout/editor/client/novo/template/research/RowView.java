package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class RowView extends Composite
        implements UberView<Row>,
        Row.View {

    private Row presenter;


    @Inject
    @DataField
    private SimplePanel row;

    //move to presenter
//    @Inject
//    RowDndManager dndManager;

    FlowPanel wrapper = new FlowPanel();


    @Override
    public void init( Row presenter ) {
        this.presenter = presenter;
        row.add( wrapper );
        row.getElement().getStyle().setCursor( Style.Cursor.MOVE );
    }

    @Override
    public void addColumn( UberView<Column> view ) {
        wrapper.add( view );
//        onAttachNative( view.asWidget() );
//        RootPanel.detachOnWindowClose( view.asWidget() );
//        DivElement.as( row ).appendChild( view.asWidget().getElement() );
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    @Override
    public void removeColumn( UberView<Column> view ) {
        wrapper.remove( view );
    }

    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;

    @EventHandler( "row" )
    public void colMouseOver( DragEnterEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG ENTER ROW" );
        presenter.dragEnterEvent();
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }

    @EventHandler( "row" )
    public void colMouseOver( DragLeaveEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG END ROW" );
        presenter.dragEndEvent();
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }

    @EventHandler( "row" )
    public void rowOut( MouseOutEvent e ) {
        presenter.rowOut();
    }

    //    @EventHandler( "row" )
    public void dndBeginOnMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        //move to presenter
        presenter.mouseDown();
    }

    //    @EventHandler( "row" )
    public void dndEndOnMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        GWT.log( "ROW MOUSE UP" );
        presenter.mouseUp();
        //move to presenter
//        dndManager.endColumnResize( presenter.hashCode() );
    }

    //    @EventHandler( "row" )
    public void dndEndOnMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        GWT.log( "ROW MOUSE DOWN" );
        presenter.mouseDown();
        //move to presenter
//        dndManager.endColumnResize( presenter.hashCode() );
    }
}


