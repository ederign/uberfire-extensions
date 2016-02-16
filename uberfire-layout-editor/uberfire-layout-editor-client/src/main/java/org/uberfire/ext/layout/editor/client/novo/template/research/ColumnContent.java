package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ColumnContent extends Composite {

    @Inject
    @DataField
    private SimplePanel content;

    @Inject
    @DataField
    private SimplePanel dndUp;

    @Inject
    @DataField
    private SimplePanel dndDown;

    public void addContent( Widget widget ) {
        content.add( widget );
    }

//    @EventHandler( "dndUp" )
//    public void dragEnterUp( DragEnterEvent e ) {
//        e.preventDefault();
//        GWT.log( "DRAG ENTER UP" );
//        dndUp.getElement().addClassName( "dropPreview" );
//    }
//
//    @EventHandler( "dndUp" )
//    public void dragLeaveUp( DragLeaveEvent e ) {
//        e.preventDefault();
//        GWT.log( "DRAG END UP" );
//        dndUp.getElement().removeClassName( "dropPreview" );
//    }
//
//    @EventHandler( "dndUp" )
//    public void dragEnterDown( DragEnterEvent e ) {
//        e.preventDefault();
//        GWT.log( "DRAG ENTER DOWN" );
//        dndDown.getElement().addClassName( "dropPreview" );
//    }
//
//    @EventHandler( "dndUp" )
//    public void dragLeaveDown( DragLeaveEvent e ) {
//        e.preventDefault();
//        GWT.log( "DRAG END DOWN" );
//        dndDown.getElement().removeClassName( "dropPreview" );
//    }

}