package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.Window;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;
import org.uberfire.ext.layout.editor.client.dnd.DndDataJSONConverter;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Dependent
public class EmptyDropRow {

    private DndDataJSONConverter converter = new DndDataJSONConverter();

    private final View view;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    private ParameterizedCommand<RowDrop> dropCommand;


    public void init( ParameterizedCommand<RowDrop> dropCommand ) {
        this.dropCommand = dropCommand;
    }

    public void drop( DropEvent dropEvent ) {
        LayoutDragComponent component = extractComponent( dropEvent );
        if ( thereIsAComponent( component ) ) {
            dropCommand.execute( new RowDrop( component, hashCode(), RowDrop.Orientation.AFTER ) );
        }
    }

    private LayoutDragComponent extractComponent( DropEvent dropEvent ) {
        return converter
                .readJSONDragComponent( dropEvent.getData( LayoutDragComponent.FORMAT ) );
    }

    private boolean thereIsAComponent( LayoutDragComponent component ) {
        return component != null;
    }


    public interface View extends UberView<EmptyDropRow> {

    }

    @Inject
    public EmptyDropRow( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }


    @PreDestroy
    public void preDestroy() {
        //TODO destroy all get instances
    }


    public UberView<EmptyDropRow> getView() {
        return view;
    }

}