package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Dependent
public class Row {

    private final View view;

    @Inject
    Instance<Column> columnInstance;

    public interface View extends UberView<Row> {

        void addColumn( UberView<Column> view );
    }

    @Inject
    public Row( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post(){
        view.init( this );
        createColumn();
//        createColumn();
    }

    @PreDestroy
    public void preDestroy(){
        //TODO destroy all get instances
    }

    private void createColumn() {
        final Column column = columnInstance.get();
        column.setSize("12");
        view.addColumn( column.getView());
    }

    public UberView<Row> getView() {
        return view;
    }

}
