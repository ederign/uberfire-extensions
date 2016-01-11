package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class ColumnView extends Composite
        implements UberView<Column>,
        Column.View {

    private Column presenter;

    @DataField
    private Element col = DOM.createDiv();


    @Override
    public void init( Column presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setSize( String size ) {
        col.addClassName( "col-md-" + size );
    }
}
