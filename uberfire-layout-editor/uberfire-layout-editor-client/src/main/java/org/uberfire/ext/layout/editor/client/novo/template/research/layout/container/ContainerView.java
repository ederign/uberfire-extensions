package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.html.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
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
    @DataField
    private SimplePanel layout;

    @Inject
    @DataField
    private Span mobile;

    @Inject
    @DataField
    private Span tablet;

    @Inject
    @DataField
    private Span desktop;

    private FlowPanel wrapper = new FlowPanel();

    @Override
    public void init( Container presenter ) {
        this.presenter = presenter;
        layout.add( wrapper );
    }

    @EventHandler( "mobile" )
    public void mobileSize( ClickEvent e ) {
        wrapper.clear();
        Frame f = new Frame( "test.html" );
        wrapper.add( f );
        String containerPreview = getElement().getInnerHTML();
        f.setWidth( "200px" );
    }

    public static native void alert( String msg ) /*-{
        $wnd.eval(msg);

    }-*/;

    @EventHandler( "tablet" )
    public void tabletSize( ClickEvent e ) {
        GWT.log( "tablet" );
        wrapper.clear();
        Frame f = new Frame( "test.html" );
        wrapper.add( f );
        f.setWidth( "767px" );
        e.preventDefault();
    }

    @EventHandler( "desktop" )
    public void desktopSize( ClickEvent e ) {
        GWT.log( "tablet" );
        wrapper.clear();
        Frame f = new Frame( "test.html" );
        wrapper.add( f );

        f.setWidth( getOffsetWidth() + "px" );

        e.preventDefault();
    }


    @Override
    public void addRow( UberView<Row> view ) {
        wrapper.add( view.asWidget() );
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    @Override
    public void addEmptyRow( UberView<EmptyDropRow> emptyDropRow ) {
        wrapper.add( emptyDropRow.asWidget() );
    }


    //    @EventHandler( "container" )
    public void containerOut( MouseOutEvent e ) {
        //TODO cancel everything/reset css
        presenter.containerOut();
    }

}
