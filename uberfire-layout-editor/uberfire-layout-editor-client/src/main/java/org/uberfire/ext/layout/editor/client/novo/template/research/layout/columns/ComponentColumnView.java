package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.components.LayoutDragComponent;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ComponentColumnView extends Composite
        implements UberView<ComponentColumn>,
        ComponentColumn.View {

    public static final String COL_CSS_CLASS = "col-md-";

    private ComponentColumn presenter;

    @DataField
    private Element col = DOM.createDiv();

    @Inject
    @DataField
    private FlowPanel colUp;

    @DataField
    private Element topPanel = DOM.createDiv();

    @Inject
    @DataField
    private FlowPanel colDown;

    @Inject
    @DataField
    private FlowPanel left;

    @Inject
    @DataField
    private FlowPanel right;

    @Inject
    @DataField
    private FlowPanel content;


    @Inject
    @DataField
    private Button file;


    @Inject
    PlaceManager placeManager;

    String cssSize = "";

    private final int originalLeftRightWidth = 5;

    private ColumnDrop.Orientation contentDropOrientation;

    @Override
    public void init( ComponentColumn presenter ) {
        this.presenter = presenter;
        setupDnD();
    }

    private void setupDnD() {
        file.addDomHandler( event -> {
            content.getElement().addClassName( "columnDropPreview" );
            event.setData( LayoutDragComponent.FORMAT, presenter.dragInfo() );
            event.getDataTransfer().setDragImage( content.getElement(), 10, 10 );
        }, DragStartEvent.getType() );

        file.addDomHandler( dragEndEvent -> content.getElement().removeClassName( "columnDropPreview" ),
                            DragEndEvent.getType() );

        file.getElement().setDraggable( com.google.gwt.user.client.Element.DRAGGABLE_TRUE );
    }

    @Override
    public void calculateSize() {
        Scheduler.get().scheduleDeferred( () -> {
            final int colWidth = col.getOffsetWidth();

            //make this calc better, right sometimes is on second line (content > necessary)
            int magicNumber = 2;
            final int contentWidth = colWidth - (originalLeftRightWidth * 2) - magicNumber;
            left.setWidth( originalLeftRightWidth + "px" );
            right.setWidth( originalLeftRightWidth + "px" );
            GWT.log( "colWid: " + colWidth );
            GWT.log( "contentWidth: " + contentWidth );
            GWT.log( "left: " + originalLeftRightWidth );
            GWT.log( "right: " + originalLeftRightWidth );
            GWT.log( "total " + (originalLeftRightWidth*2 + contentWidth) );

            if ( !presenter.isContainerColumn() ) {
                //FIXME bug small then uf screen
                GWT.log( "left/right height?" );
                left.setHeight( content.getOffsetHeight() + "px" );
                right.setHeight( content.getOffsetHeight() + "px" );
            }

            content.setWidth( contentWidth + "px" );

            colDown.setWidth( contentWidth + "px" );
            colUp.setWidth( contentWidth + "px" );


            col.addClassName( "container" );

        } );
    }

    @Override
    public void setCursor() {
        content.getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
        topPanel.getStyle().setCursor( Style.Cursor.MOVE );
        if ( presenter.canResize() ) {
            left.getElement().getStyle().setCursor( Style.Cursor.COL_RESIZE );
        } else {
            left.getElement().getStyle().setCursor( Style.Cursor.MOVE );
        }
    }

    @Override
    public void setSize( String size ) {
        if ( !col.getClassName().isEmpty() ) {
            col.removeClassName( cssSize );
        }
        cssSize = COL_CSS_CLASS + size;
        col.addClassName( cssSize );
        if ( !presenter.isInnerColumn() ) {
            col.addClassName( "no-padding" );
        } else {
            if ( col.hasClassName( "no-padding" ) ) {
                col.removeClassName( "no-padding" );
                col.addClassName( "innerColumnPadding" );
            }
        }
    }

    @Override
    public void setContent( IsWidget widget ) {
        content.clear();
        if ( widget != null ) {
            content.add( widget );
        }
        GWT.log("-< " + content.getElement().getInnerHTML() );
    }


    @EventHandler( "left" )
    public void colMouseDown( MouseDownEvent e ) {
        e.preventDefault();
        presenter.onMouseDown( e.getClientX() );
    }

    @EventHandler( "col" )
    public void colMouseUp( MouseUpEvent e ) {
        e.preventDefault();
        presenter.onMouseUp( e.getClientX() );
    }

    @EventHandler( "col" )
    public void colMouseOver( MouseMoveEvent e ) {
        e.preventDefault();
    }

    @EventHandler( "colUp" )
    public void dragEntercolUp( DragOverEvent e ) {
        colUp.getElement().addClassName( "colPreview" );
    }

    @EventHandler( "colUp" )
    public void dragLeftcolUp( DragLeaveEvent e ) {
        colUp.getElement().removeClassName( "colPreview" );
    }


    @EventHandler( "content" )
    public void dragOverCenter( DragOverEvent e ) {
        e.preventDefault();
        final int absoluteTop = content.getElement().getAbsoluteTop();
        final int absoluteBottom = content.getElement().getAbsoluteBottom();
        final int dragOverY = e.getNativeEvent().getClientY();

        if ( ( dragOverY - absoluteTop ) < ( absoluteBottom - dragOverY ) ) {
            colUp.getElement().addClassName( "colPreview" );
            colDown.getElement().removeClassName( "colPreview" );
            contentDropOrientation = ColumnDrop.Orientation.UP;

        } else {
            colDown.getElement().addClassName( "colPreview" );
            colUp.getElement().removeClassName( "colPreview" );
            contentDropOrientation = ColumnDrop.Orientation.DOWN;
        }
    }

    @EventHandler( "content" )
    public void dragLeaveCenter( DragLeaveEvent e ) {
        e.preventDefault();
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );
        contentDropOrientation = null;
    }


    @EventHandler( "left" )
    public void dragEnterLeft( DragEnterEvent e ) {
        e.preventDefault();
        left.getElement().addClassName( "columnDropPreview dropPreview" );
        content.getElement().addClassName( "centerPreview" );
    }

    @EventHandler( "left" )
    public void dragLeaveLeft( DragLeaveEvent e ) {
        e.preventDefault();
        left.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
    }


    @EventHandler( "content" )
    public void dropInsideColumn( DropEvent drop ) {
        if ( contentDropOrientation != null ) {
            presenter.onDrop( contentDropOrientation, drop );
        }
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );
    }

    @EventHandler( "left" )
    public void dropColumnRight( DropEvent drop ) {
        drop.preventDefault();
        left.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
        presenter.onDrop( ColumnDrop.Orientation.LEFT, drop );
    }

    @EventHandler( "right" )
    public void dragEnterRight( DragEnterEvent e ) {
        e.preventDefault();
        right.getElement().addClassName( "columnDropPreview dropPreview" );
        content.getElement().addClassName( "centerPreview" );
    }

    @EventHandler( "right" )
    public void dragLeaveRight( DragLeaveEvent e ) {
        e.preventDefault();
        right.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
    }


    @EventHandler( "right" )
    public void dropColumnRIGHT( DropEvent drop ) {
        drop.preventDefault();
        right.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
        presenter.onDrop( ColumnDrop.Orientation.RIGHT, drop );
    }

    @EventHandler( "col" )
    public void dragOverEvent( DragOverEvent e ) {
        e.preventDefault();
    }


    @EventHandler( "file" )
    public void dragComponent( DragStartEvent e ) {
    }

}