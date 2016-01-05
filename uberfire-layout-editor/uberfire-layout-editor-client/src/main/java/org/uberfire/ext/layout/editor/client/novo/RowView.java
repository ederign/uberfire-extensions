package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class RowView extends Composite
        implements UberView<RowPresenter>,
        RowPresenter.View {


    private RowPresenter presenter;

    @PostConstruct
    public void oi(){
    }
    @Override
    public void init( RowPresenter  presenter ) {
        this.presenter = presenter;
    }
}
