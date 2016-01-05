package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class RowView extends Composite
        implements UberView<RowPresenter>,
        RowPresenter.View {


    private RowPresenter presenter;

    @DataField("dropRow")
    private Element dropRow = DOM.createDiv();

//    @Inject
//    @DataField("field-1")
//    private Label div;

    private Command onDrop;


    @PostConstruct
    public void x() {
        dropRow.setAttribute( "x", "y" );
    }

//    @EventHandler( "field-1" )
//    public void onDrop( DropEvent dropEvent ) {
//        if ( onDrop != null ) {
//            onDrop.execute();
//        }
//    }
//
//    @EventHandler( "field-1" )
//    public void z( ClickEvent dropEvent ) {
//        Window.alert( "click" );
//    }


    @Override
    public void init( RowPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addDropCommand( Command onDrop ) {
        this.onDrop = onDrop;
    }
}
