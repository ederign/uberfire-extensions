package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
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

    FlowPanel wrapper = new FlowPanel(  );


    @Override
    public void init( Row presenter ) {
        this.presenter = presenter;
        row.add( wrapper );

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
}


