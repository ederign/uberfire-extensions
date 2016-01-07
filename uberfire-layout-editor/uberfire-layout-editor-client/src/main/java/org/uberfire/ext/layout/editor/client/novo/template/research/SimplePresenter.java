package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.DropRowPresenter;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class SimplePresenter {

    private final View view;

    public interface View extends UberView<SimplePresenter> {

    }

    @Inject
    public SimplePresenter( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post(){
        view.init( this );
    }

    public UberView<SimplePresenter> getView() {
        return view;
    }

}
