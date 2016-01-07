package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class LayoutContainerPresenter {

    private final View view;
    private List<DropRowPresenter> rows = new ArrayList<DropRowPresenter>();

    @Inject
    private Instance<DropRowPresenter> instance;

    public interface View extends UberView<LayoutContainerPresenter> {

        void addRow( IsWidget view );

    }

    @Inject
    public LayoutContainerPresenter( final View view ) {
        this.view = view;
        view.init( this );

    }

    @PostConstruct
    public void post(){
//        createDefaultDropRow();
    }

    private void createDefaultDropRow() {
        final DropRowPresenter dropRow =instance.get();
        dropRow.addDropCommand( new ParameterizedCommand<String>() {
            @Override
            public void execute( String parameter ) {
                Window.alert( parameter );
            }
        } );
        rows.add( dropRow );
        view.addRow( dropRow.getView() );
        Window.alert( "yo" );
    }

    public UberView<LayoutContainerPresenter> getView() {
        return view;
    }

}
