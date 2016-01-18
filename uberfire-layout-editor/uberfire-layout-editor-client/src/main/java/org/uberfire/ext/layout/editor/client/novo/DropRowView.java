package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DropRowView extends Composite
        implements UberView<DropRowPresenter>,
        DropRowPresenter.View {

//    @DataField( "dropArea" )
//    private Element dropArea = DOM.createDiv();
//
//    @DataField( "row" )
//    private Element row = DOM.createDiv();
//
//
//    @DataField( "previewRow" )
//    private Element previewRow = DOM.createDiv();
//
//    @Inject
//    @DataField( "component" )
//    Label component;

    private DropRowPresenter presenter;

    private ParameterizedCommand<String> onDrop;


//    @PostConstruct
//    void init() {
////        previewRow.getStyle().setProperty( "display", "none" );
//    }
//
//    @EventHandler( "previewRow" )
//    public void dragLeave( DropEvent e ) {
//        row.getStyle().setProperty( "display", "block" );
//        previewRow.getStyle().setProperty( "display", "none" );
//    }
//
//    @EventHandler( "dropArea" )
//    public void dropEvent( DropEvent e ) {
//        row.getStyle().setProperty( "display", "block" );
//        onDrop.execute( e.getData( LayoutDragComponent.FORMAT ) );
//    }
//
//    @EventHandler( "dropArea" )
//    public void dragOverEvent( DragOverEvent e ) {
//        row.getStyle().setProperty( "display", "none" );
//        previewRow.getStyle().setProperty( "display", "block" );
//
//        //call presenter to render component
//        final String data = e.getData( LayoutDragComponent.FORMAT );
//        component.setText( DndController.dragged );
//    }

    native void consoleLog( String message ) /*-{
        console.log("me:" + message);
    }-*/;

    @Override
    public void init( DropRowPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addDropCommand( ParameterizedCommand<String> onDrop ) {
        this.onDrop = onDrop;
    }

}
