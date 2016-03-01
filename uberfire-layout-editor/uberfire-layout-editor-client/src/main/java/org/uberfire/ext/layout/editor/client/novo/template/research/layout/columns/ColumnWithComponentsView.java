package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ColumnWithComponentsView extends Composite
        implements UberView<ColumnWithComponents>,
        ColumnWithComponents.View {

    public static final String COL_CSS_CLASS = "col-md-";

    private ColumnWithComponents presenter;

    @Inject
    @DataField
    FlowPanel col;
    String cssSize = "";

    @Override
    public void init( ColumnWithComponents presenter ) {
        this.presenter = presenter;
    }


    @Override
    public void setSize( String size ) {
        if ( hasCssSizeClass() ) {
            col.getElement().removeClassName( cssSize );
        }
        cssSize = COL_CSS_CLASS + size;
        col.getElement().addClassName( cssSize );
    }

    private boolean hasCssSizeClass() {
        return !cssSize.isEmpty() && col.getElement().hasClassName( cssSize );
    }

    @Override
    public void addRow( UberView<Row> view ) {
        col.add( view );
    }


}