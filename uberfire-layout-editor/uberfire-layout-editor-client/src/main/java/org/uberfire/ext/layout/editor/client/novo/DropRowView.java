package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Row;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.context.Dependent;

@Dependent
public class DropRowView extends Composite
        implements UberView<DropRowPresenter>,
        DropRowPresenter.View {

    interface DropRowViewBinder
            extends
            UiBinder<Widget, DropRowView> {

    }

    private static DropRowViewBinder uiBinder = GWT.create( DropRowViewBinder.class );


    @UiField
    Row row;

    @UiField
    Row previewRow;

    @UiField
    Label component;

    private DropRowPresenter presenter;

    private ParameterizedCommand<String> onDrop;


    @AfterInitialization
    public void initWidget() {
        initWidget( uiBinder.createAndBindUi( this ) );
        previewRow.getElement().getStyle().setProperty( "display", "none" );
        addDragOverHandler( new DragOverHandler() {
            @Override
            public void onDragOver( DragOverEvent event ) {
                dragOverHandler( event );
            }
        } );
        previewRow.addBitlessDomHandler( new DragLeaveHandler() {
            @Override
            public void onDragLeave( DragLeaveEvent event ) {
                dragLeaveHandler( event );
            }
        }, DragLeaveEvent.getType() );
        addDropHandler( new DropHandler() {
            @Override
            public void onDrop( DropEvent event ) {
                dropHandler( event );
            }
        } );
    }

    private void dropHandler( DropEvent event ) {
        //fixme
        row.getElement().getStyle().setProperty( "display", "block" );
        onDrop.execute( event.getData( LayoutDragComponent.FORMAT ) );
    }

    private void dragLeaveHandler( DragLeaveEvent event ) {
        //fixme
        row.getElement().getStyle().setProperty( "display", "block" );
        previewRow.getElement().getStyle().setProperty( "display", "none" );
    }

    private void dragOverHandler( DragOverEvent event ) {
        //fixme
        row.getElement().getStyle().setProperty( "display", "none" );
        previewRow.getElement().getStyle().setProperty( "display", "block" );

        //call presenter to render component
        final String data = event.getData( LayoutDragComponent.FORMAT );
        component.setText( DndController.dragged );
    }

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

    private HandlerRegistration addDropHandler( DropHandler handler ) {
        return addBitlessDomHandler( handler, DropEvent.getType() );
    }

    private HandlerRegistration addDragOverHandler( DragOverHandler handler ) {
        return addBitlessDomHandler( handler, DragOverEvent.getType() );
    }

    private HandlerRegistration addDragLeaveHandler( DragLeaveHandler handler ) {
        return addBitlessDomHandler( handler, DragLeaveEvent.getType() );
    }
}
