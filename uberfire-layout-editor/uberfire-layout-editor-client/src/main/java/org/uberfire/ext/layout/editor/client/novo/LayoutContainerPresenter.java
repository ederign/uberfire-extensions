package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class LayoutContainerPresenter {

    private final View view;
    private List<RowPresenter> rows = new ArrayList<RowPresenter>(  );

    public interface View extends UberView<LayoutContainerPresenter> {

        void addRow( IsWidget view );

    }

    @Inject
    public LayoutContainerPresenter( final View view ) {
        this.view = view;
        view.init( this );
        createDefaultRow();
    }

    private void createDefaultRow() {
        final RowPresenter defaultRow =
                IOC.getBeanManager().lookupBean( RowPresenter.class ).getInstance();
        rows.add( defaultRow );
        view.addRow( defaultRow.getView() );
    }

    public UberView<LayoutContainerPresenter> getView() {
        return view;
    }
}
