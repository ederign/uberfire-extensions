package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Container;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

@Dependent
public class LayoutContainerView extends Composite
        implements UberView<LayoutContainerPresenter>,
        LayoutContainerPresenter.View {

    @UiField
    Container container;

    interface LayoutContainerViewBinder
            extends
            UiBinder<Widget, LayoutContainerView> {

    }

    private static LayoutContainerViewBinder uiBinder = GWT.create( LayoutContainerViewBinder.class );

    private LayoutContainerPresenter presenter;

    @AfterInitialization
    public void initWidget() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( LayoutContainerPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addRow( IsWidget row ) {
        container.add( row );
    }
}
