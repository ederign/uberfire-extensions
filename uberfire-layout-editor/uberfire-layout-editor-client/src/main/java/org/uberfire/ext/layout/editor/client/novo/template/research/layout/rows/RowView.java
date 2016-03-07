package org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows;

import com.google.gwt.core.client.GWT;
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

    static final String ROW_DROP_PREVIEW = "rowDropPreview";

    private Row presenter;


    @DataField
    Element upper = DOM.createDiv();

    @DataField
    Element bottom = DOM.createDiv();

    @Inject
    @DataField
    FlowPanel content;


    FlowPanel rowContentWrapper = GWT.create( FlowPanel.class );


    @Override
    public void init( Row presenter ) {
        this.presenter = presenter;
        content.add( rowContentWrapper );
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
            upper.addClassName( ROW_DROP_PREVIEW );
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
            presenter.drop( e, RowDrop.Orientation.BEFORE );
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
    public void dragOverBottom( DragOverEvent e ) {
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
            presenter.drop( e, RowDrop.Orientation.AFTER );
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


