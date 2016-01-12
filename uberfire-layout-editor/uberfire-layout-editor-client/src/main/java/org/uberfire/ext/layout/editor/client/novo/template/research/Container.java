package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class Container {


    @Inject
    Instance<Row> rowInstance;

    List<Row> rows = new ArrayList<Row>();

    private final View view;

    public interface View extends UberView<Container> {
        void addRow( UberView<Row> view );
    }

    @Inject
    public Container( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
        createRow();
//        createRow();
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all rows instances
    }


    private void createRow() {
        final Row row = rowInstance.get();
        rows.add( row );
        view.addRow( row.getView() );
    }

    public UberView<Container> getView() {
        return view;
    }

}
