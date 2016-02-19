package org.uberfire.ext.layout.editor.client.novo;

import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.ParameterizedCommand;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DropRowPresenter {

    private final View view;

    public interface View extends UberView<DropRowPresenter> {

        void init( DropRowPresenter dropRowPresenter );

        void addDropCommand( ParameterizedCommand<String> onDrop );

    }

    @Inject
    public DropRowPresenter( final View view ) {
        this.view = view;
        view.init( this );
    }

    public void addDropCommand( ParameterizedCommand<String> onDrop ) {
        view.addDropCommand(onDrop);
    }
    public UberView<DropRowPresenter> getView() {
        return view;
    }

}
