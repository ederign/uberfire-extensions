package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class ContainerView extends Composite
        implements UberView<Container>,
        Container.View {

    private Container presenter;

    @DataField
    private Element container = DOM.createDiv();

    @Override
    public void init( Container presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addRow( UberView<Row> view ) {
        onAttachNative( view.asWidget() );
        RootPanel.detachOnWindowClose( view.asWidget() );

        DivElement.as( container ).appendChild( view.asWidget().getElement() );
    }

    private static native void onAttachNative( Widget w ) /*-{
        w.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;


}
