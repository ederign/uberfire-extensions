package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class LayoutContainerView extends Composite
        implements UberView<LayoutContainerPresenter>,
        LayoutContainerPresenter.View {

    @DataField( "container" )
    private Element container = DOM.createDiv();


    private LayoutContainerPresenter presenter;

    @Override
    public void init( LayoutContainerPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addRow( IsWidget row ) {
        container.appendChild( row.asWidget().getElement() );
    }
}
