package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
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
        DivElement.as( container ).appendChild( new Label( "fsadfsd" ).asWidget().getElement() );
    }

    @Override
    public void addRow( UberView<Row> view ) {
        DivElement.as( container ).appendChild( view.asWidget().getElement() );
    }


    @EventHandler( "container" )
    public void dragOver( ClickEvent e ) {
        GWT.log( "containerclick" );
    }
}
