package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.user.client.Window;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class RowPresenter {

    private final View view;

    public interface View extends UberView<RowPresenter> {

        void init( RowPresenter rowPresenter );

        void addDropCommand( Command onDrop );

    }

    @Inject
    public RowPresenter( final View view ) {
        this.view = view;
        view.init( this );
    }

    public void addDropCommand( Command onDrop ) {
        view.addDropCommand(onDrop);
    }
    public UberView<RowPresenter> getView() {
        return view;
    }

}
