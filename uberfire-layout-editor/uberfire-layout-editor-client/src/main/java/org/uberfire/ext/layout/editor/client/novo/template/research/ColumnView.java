package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ColumnView extends Composite
        implements UberView<Column>,
        Column.View {

    public static final String COL_CSS_CLASS = "col-md-";
    private Column presenter;

    @DataField
    private Element col = DOM.createDiv();


    @Inject
    @DataField
    private SimplePanel colUp;

    @Inject
    @DataField
    private SimplePanel colDown;


    @Inject
    @DataField
    private SimplePanel left;

    @Inject
    @DataField
    private SimplePanel right;

    @Inject
    @DataField
    private SimplePanel content;

    private FlowPanel contentWrapper;

    String cssSize = "";

    private final int originalLeftRightWidth = 5;


    @Override
    public void init( Column presenter ) {
        this.presenter = presenter;
        contentWrapper = GWT.create( FlowPanel.class );
        content.add( contentWrapper );
    }

    @Override
    public void calculateSize() {
        Scheduler.get().scheduleDeferred( new Command() {
            public void execute() {
                final int colWidth = col.getOffsetWidth();

                final int contentWidth = colWidth - originalLeftRightWidth * 2 - 1;
                left.setWidth( originalLeftRightWidth + "px" );
                right.setWidth( originalLeftRightWidth + "px" );

                if(!presenter.isContainerColumn()) {
                    //broke the layout, need
                    left.setHeight( content.getOffsetHeight() + "px" );
                    right.setHeight( content.getOffsetHeight() + "px" );
                }

                content.setWidth( contentWidth + "px" );

                //FIXME <- redimensiona browser
                colDown.setWidth( contentWidth + "px" );
                colUp.setWidth( contentWidth + "px" );


                    col.addClassName( "container" );


            }
        } );
    }

    @Override
    public void addRow( UberView<Row> view ) {
        contentWrapper.add( view );
    }

    @Override
    public void setCursor() {
        content.getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
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
        if(!presenter.isInnerColumn()){
            col.addClassName( "no-padding" );
        }
        else{
            if(col.hasClassName( "no-padding" )){
                col.removeClassName( "no-padding" );
                col.addClassName( "innerColumnPadding" );
            }
        }
    }

    @Override
    public void setContent( String contentLabel ) {
        if ( !contentLabel.isEmpty() )
            contentWrapper.add( new Label( contentLabel ) );
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

    //TODO MOUSE OUT ROW

    @EventHandler( "col" )
    public void colMouseOver( MouseMoveEvent e ) {
        e.preventDefault();
        //why?
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
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

        } else {
            colDown.getElement().addClassName( "colPreview" );
            colUp.getElement().removeClassName( "colPreview" );
        }
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }

    @EventHandler( "content" )
    public void dragLeaveCenter( DragLeaveEvent e ) {
        e.preventDefault();
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }


    @EventHandler( "left" )
    public void dragEnterLeft( DragEnterEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG ENTER left" );
        left.getElement().addClassName( "columnDropPreview dropPreview" );
        content.getElement().addClassName( "centerPreview" );
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }

    @EventHandler( "left" )
    public void dragLeaveLeft( DragLeaveEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG END left" );
        left.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
//        presenter.onMouseOver(new MouseOverInfo(e.getClientX(), e.getClientY()));
    }


    @EventHandler( "content" )
    public void dropInsideColumn( DropEvent e ) {
        GWT.log( "dropInsideColumnDown content" );
        presenter.onDrop( ColumnDrop.Orientation.INSIDE );
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );
    }

    @EventHandler( "colUp" )
    public void dropInsideColumnUp( DropEvent e ) {
        GWT.log( "dropInsideColumnDown up" );
        presenter.onDrop( ColumnDrop.Orientation.INSIDE );
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );
    }

    @EventHandler( "colDown" )
    public void dropInsideColumnDown( DropEvent e ) {
        GWT.log( "dropInsideColumnDown down" );
        presenter.onDrop( ColumnDrop.Orientation.INSIDE );
        colUp.getElement().removeClassName( "colPreview" );
        colDown.getElement().removeClassName( "colPreview" );

    }

    @EventHandler( "left" )
    public void dropColumnRight( DropEvent e ) {
        GWT.log( "DROP COLUMN LEFT EVENT" );
        e.preventDefault();
        left.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
        presenter.onDrop( ColumnDrop.Orientation.LEFT );
    }

    @EventHandler( "right" )
    public void dragEnterRight( DragEnterEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG ENTER right COLUMN" );
        right.getElement().addClassName( "columnDropPreview dropPreview" );
        content.getElement().addClassName( "centerPreview" );
    }

    @EventHandler( "right" )
    public void dragLeaveRight( DragLeaveEvent e ) {
        e.preventDefault();
        GWT.log( "DRAG END right COLUMN" );
        right.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
    }


    @EventHandler( "right" )
    public void dropColumnRIGHT( DropEvent e ) {
        GWT.log( "DROP right LEFT EVENT" );
        e.preventDefault();
        right.getElement().removeClassName( "columnDropPreview dropPreview" );
        content.getElement().removeClassName( "centerPreview" );
        presenter.onDrop( ColumnDrop.Orientation.RIGHT );
    }

    @EventHandler( "col" )
    public void dragOverEvent( DragOverEvent e ) {
        e.preventDefault();
    }

//    @EventHandler( "col" )
//    public void click( ClickEvent e ) {
//        GWT.log( "Click" );
//    }
//
//    @EventHandler( "col" )
//    public void dragOverEvent( DragOverEvent e ) {
//        e.preventDefault();
//    }


    private int calculateMiddle() {
        return getAbsoluteLeft() + ( getOffsetWidth() / 2 );
    }


}