package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class LayoutContainerPresenter {

    private final View view;
    private List<DropRowPresenter> rows = new ArrayList<DropRowPresenter>();

    public interface View extends UberView<LayoutContainerPresenter> {

        void addRow( IsWidget view );

    }

    @Inject
    public LayoutContainerPresenter( final View view ) {
        this.view = view;
        view.init( this );
        createDefaultDropRow();
    }

    private void createDefaultDropRow() {
        final DropRowPresenter dropRow =
                IOC.getBeanManager().lookupBean( DropRowPresenter.class ).getInstance();
        dropRow.addDropCommand( new ParameterizedCommand<String>() {
            @Override
            public void execute( String parameter ) {
                Window.alert( parameter );
            }
        } );
        rows.add( dropRow );
        view.addRow( dropRow.getView() );
    }

    public UberView<LayoutContainerPresenter> getView() {
        return view;
    }

}
