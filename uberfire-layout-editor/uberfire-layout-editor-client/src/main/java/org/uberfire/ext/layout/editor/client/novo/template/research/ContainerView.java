package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.shared.api.annotations.DataField;
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
    private SimplePanel container;

    private FlowPanel wrapper = new FlowPanel(  );

    @Override
    public void init( Container presenter ) {
        this.presenter = presenter;
        container.add( wrapper );
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


}
