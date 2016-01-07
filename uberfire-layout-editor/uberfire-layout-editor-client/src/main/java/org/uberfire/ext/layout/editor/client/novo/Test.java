package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class Test extends Composite{

    @Inject
    @DataField
    private TextBox username;

    @DataField("drop")
    private Element drop = DOM.createDiv();

    @EventHandler("drop")
    public void drop(DragOverEvent e) {
        consoleLog( "drop" );
    }

    @EventHandler("username")
    public void doSomethingC1(DragOverEvent e) {
        consoleLog( username.getText() );
    }

    native void consoleLog( String message ) /*-{
        console.log("me:" + message);
    }-*/;

}
