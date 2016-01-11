package org.uberfire.ext.layout.editor.client.novo.template.research.bug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class Simple extends Composite {


    @DataField
    private Element blueBox = DOM.createDiv();


    @EventHandler( "blueBox" )
    public void click( ClickEvent e ) {
        GWT.log( "blueBox" );

    }


}
