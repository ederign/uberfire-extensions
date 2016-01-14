package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
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
    @Inject
    RowDndManager dndManager;

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
    public void clearColumns() {
        wrapper.clear();
    }

    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;

    @EventHandler( "row" )
    public void rowClick( ClickEvent e ) {
        e.preventDefault();
        GWT.log( "click row" );
    }

    @EventHandler( "row" )
    public void dndBeginOnMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        //move to presenter
        dndManager.begin( presenter.hashCode() );
    }

    @EventHandler( "row" )
    public void dndEndOnMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        //move to presenter
        dndManager.end( presenter.hashCode() );
    }

}


