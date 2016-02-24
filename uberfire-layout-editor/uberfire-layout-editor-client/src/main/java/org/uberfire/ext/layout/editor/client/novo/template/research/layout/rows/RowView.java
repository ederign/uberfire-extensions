package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns.ComponentColumn;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class RowView extends Composite
        implements UberView<Row>,
        Row.View {

    private Row presenter;


    @DataField
    private Element upper = DOM.createDiv();

    @Inject
    @DataField
    private FlowPanel content;

    @DataField
    private Element bottom = DOM.createDiv();


    FlowPanel rowContentWrapper = new FlowPanel();


    @Override
    public void init( Row presenter ) {
        this.presenter = presenter;
        content.add( rowContentWrapper );
//        content.getElement().getStyle().setCursor( Style.Cursor.MOVE );
    }

    @Override
    public void addColumn( UberView<ComponentColumn> view ) {
        rowContentWrapper.add( view );
    }

    @Override
    public void clear() {
        rowContentWrapper.clear();
    }

    @EventHandler( "upper" )
    public void dragOverUpper( DragOverEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            upper.addClassName( "rowDropPreview" );
        }
    }

    @EventHandler( "upper" )
    public void dragLeaveUpper( DragLeaveEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            upper.removeClassName( "rowDropPreview" );
        }
    }

    @EventHandler( "upper" )
    public void dropUpperEvent( DropEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            upper.removeClassName( "rowDropPreview" );
            presenter.drop( RowDrop.Orientation.AFTER );
        }
    }

    @EventHandler( "bottom" )
    public void mouseOutUpper( MouseOutEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            bottom.removeClassName( "rowDropPreview" );
        }
    }

    @EventHandler( "bottom" )
    public void dragoverBottom( DragOverEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            bottom.addClassName( "rowDropPreview" );
        }

    }

    @EventHandler( "bottom" )
    public void dropBottomEvent( DropEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            bottom.removeClassName( "rowDropPreview" );
            presenter.drop( RowDrop.Orientation.BEFORE );
        }
    }


    @EventHandler( "bottom" )
    public void dragLeaveBottom( DragLeaveEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            bottom.removeClassName( "rowDropPreview" );
        }
    }

    @EventHandler( "bottom" )
    public void mouseOutBottom( MouseOutEvent e ) {
        if ( presenter.isDropEnable() ) {
            e.preventDefault();
            bottom.removeClassName( "rowDropPreview" );
        }
    }


}


