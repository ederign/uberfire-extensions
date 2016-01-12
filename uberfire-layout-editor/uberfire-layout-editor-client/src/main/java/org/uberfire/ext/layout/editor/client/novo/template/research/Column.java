package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class Column {

    private final View view;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;

    public interface View extends UberView<Column> {

        void setSize( String size );
    }

    @Inject
    public Column( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public UberView<Column> getView() {
        return view;
    }

    public void setup( Integer size, ParameterizedCommand<ColumnDrop> dropCommand ) {
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
    }

    public Integer getSize() {
        return size;
    }

    public void setSize( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public void onDrop( int dropPosition, int columnMiddleX ) {
        dropCommand.execute( new ColumnDrop( hashCode(), dropPosition , columnMiddleX) );
    }

}
