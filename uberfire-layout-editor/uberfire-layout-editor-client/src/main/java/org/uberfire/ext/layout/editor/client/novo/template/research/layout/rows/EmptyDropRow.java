package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Dependent
public class EmptyDropRow {

    private final View view;

    @Inject
    Event<RepaintContainerEvent> repaintContainerEvent;

    private ParameterizedCommand<RowDrop> dropCommand;


    public void init( ParameterizedCommand<RowDrop> dropCommand ) {
        this.dropCommand = dropCommand;
//        updateView();
    }

    public void drop() {
        dropCommand.execute( new RowDrop( hashCode(), RowDrop.Orientation.AFTER ) );
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