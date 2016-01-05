package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class LayoutContainerView extends Composite
        implements UberView<LayoutContainerPresenter>,
        LayoutContainerPresenter.View {

    private LayoutContainerPresenter presenter;

    @DataField
    private Element container = DOM.createDiv();

    @Override
    public void init( LayoutContainerPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addRow( IsWidget row ) {
        //duvida cristian,
        // mais uma que tem que botar um div externa qdo eu dou create div
        container.appendChild( row.asWidget().getElement() );
    }
}
