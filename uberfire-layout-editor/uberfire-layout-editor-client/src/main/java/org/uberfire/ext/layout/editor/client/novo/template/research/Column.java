package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class Column {

    private final View view;

    private String size;

    public interface View extends UberView<Column> {
        void setSize( String size );
    }
    @Inject
    public Column( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post(){
        view.init( this );
    }

    public UberView<Column> getView() {
        return view;
    }

    public void setSize( String size ) {
        this.size = size;
        view.setSize(size);
    }

}
