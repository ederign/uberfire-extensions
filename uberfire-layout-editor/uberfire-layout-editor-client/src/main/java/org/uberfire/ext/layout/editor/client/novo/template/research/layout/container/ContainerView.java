package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import org.gwtbootstrap3.client.ui.html.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.EmptyDropRow;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ContainerView extends Composite
        implements UberView<Container>,
        Container.View {

    private Container presenter;

    @Inject
    PlaceManager placeManager;

    @Inject
    @DataField
    private FlowPanel layout;

    @Inject
    @DataField
    private Span mobile;

    @Inject
    @DataField
    private Span tablet;

    @Inject
    @DataField
    private Span desktop;

    @Override
    public void init( Container presenter ) {
        this.presenter = presenter;
    }

    @EventHandler( "mobile" )
    public void mobileSize( ClickEvent e ) {
        GWT.log( "mobileSize" );
//        layout.clear();
//        Frame f = new Frame( "test.html" );
//        layout.add( f );
//        String containerPreview = getElement().getInnerHTML();
//        f.setWidth( "200px" );
    }

    public static native void alert( String msg ) /*-{
        $wnd.eval(msg);

    }-*/;

    @EventHandler( "tablet" )
    public void tabletSize( ClickEvent e ) {
        GWT.log( "tablet" );
//        layout.clear();
//        Frame f = new Frame( "test.html" );
//        layout.add( f );
//        f.setWidth( "767px" );
//        e.preventDefault();
    }

    @EventHandler( "desktop" )
    public void desktopSize( ClickEvent e ) {
        GWT.log( "desktop" );
//        layout.clear();
//        Frame f = new Frame( "test.html" );
//        layout.add( f );
//
//        f.setWidth( getOffsetWidth() + "px" );
//
//        e.preventDefault();
    }


    @Override
    public void addRow( UberView<Row> view ) {
        layout.add( view.asWidget() );
    }

    @Override
    public void clear() {
        layout.clear();
    }

    @Override
    public void addEmptyRow( UberView<EmptyDropRow> emptyDropRow ) {
        layout.add( emptyDropRow.asWidget() );
    }

}
