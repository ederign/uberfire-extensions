package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.html.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

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
        e.preventDefault();
        layout.setWidth( "320px" );
        presenter.changeResolution();
    }

    @EventHandler( "tablet" )
    public void tabletSize( ClickEvent e ) {
        e.preventDefault();
        layout.setWidth( "768px" );
        presenter.changeResolution();
    }

    @EventHandler( "desktop" )
    public void desktopSize( ClickEvent e ) {
        e.preventDefault();
        layout.setWidth( "100%" );
        presenter.changeResolution();
    }


    @Override
    public void addRow( UberView<Row> view ) {
        wrapper.add( view.asWidget() );
//        onAttachNative( view.asWidget() );
//        RootPanel.detachOnWindowClose( view.asWidget() );
//
//        DivElement.as( container ).appendChild( view.asWidget().getElement() );
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;


    //    @EventHandler( "container" )
    public void containerOut( MouseOutEvent e ) {
        //TODO
        presenter.containerOut();
    }

}
