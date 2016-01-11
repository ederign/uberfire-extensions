package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Dependent
public class Container {


    @Inject
    Instance<Row> rowInstance;

    private final View view;

    public interface View extends UberView<Container> {
        void addRow( UberView<Row> view );
    }

    @Inject
    public Container( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post(){
        view.init( this );
//        createRow();
        createRow();
    }

    private void createRow() {
        final Row row = rowInstance.get();
        view.addRow( row.getView() );
    }

    public UberView<Container> getView() {
        return view;
    }

}
