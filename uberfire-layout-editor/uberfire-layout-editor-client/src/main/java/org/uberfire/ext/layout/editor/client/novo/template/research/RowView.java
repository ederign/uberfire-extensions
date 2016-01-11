package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class RowView extends Composite
        implements UberView<Row>,
        Row.View {

    private Row presenter;


    @DataField
    private Element row = DOM.createDiv();


    @Override
    public void init( Row presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addColumn( UberView<Column> view ) {
        DivElement.as( row ).appendChild( view.asWidget().getElement() );
    }
}
