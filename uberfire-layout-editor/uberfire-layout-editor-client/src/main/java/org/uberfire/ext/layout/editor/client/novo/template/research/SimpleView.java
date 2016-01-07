package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;

@Dependent
@Templated
public class SimpleView extends Composite
        implements UberView<SimplePresenter>,
        SimplePresenter.View {

    @DataField
    private Element simpleDiv = DOM.createDiv();

    @DataField
    private Element slider = DOM.createDiv();

    @DataField
    private Element slider2 = DOM.createDiv();

    @DataField
    private Element column1 = DOM.createDiv();

    @DataField
    private Element column2 = DOM.createDiv();

    @DataField
    private Element column3 = DOM.createDiv();

    private SimplePresenter presenter;

    private boolean mouseDown1;
    private boolean mouseDown2;

    @Override
    public void init( SimplePresenter presenter ) {
        this.presenter = presenter;

        slider.getStyle().setCursor( Style.Cursor.COL_RESIZE );
        slider2.getStyle().setCursor( Style.Cursor.COL_RESIZE );
    }

    @EventHandler( "simpleDiv" )
    public void dragLeave( DropEvent e ) {
        consoleLog( "dragLeave" );
    }

    @EventHandler( "simpleDiv" )
    public void dropEvent( DropEvent e ) {
        consoleLog( "dropEvent" );
    }

    @EventHandler( "simpleDiv" )
    public void dragOver( DragOverEvent e ) {
        consoleLog( "dragOver" );
    }

    @EventHandler( "slider" )
    public void onMouseOver( MouseOverEvent e ) {
        consoleLog( "MouseOverEvent" );
    }

    @EventHandler( "slider" )
    public void onMouseOut( MouseOutEvent e ) {
        consoleLog( "MouseOutEvent" );
        //deve ir soh no mouse out do container inteiro
        this.mouseDown1 = false;
    }

    @EventHandler( "slider2" )
    public void onMouseOut2( MouseOutEvent e ) {
        consoleLog( "MouseOutEvent" );
        //deve ir soh no mouse out do container inteiro
        this.mouseDown2 = false;
    }

    @EventHandler( "slider" )
    public void MouseDownEvent( MouseDownEvent e ) {
        consoleLog( "MouseDownEvent" );
        this.mouseDown1 = true;
    }
    @EventHandler( "slider2" )
    public void MouseDownEvent2( MouseDownEvent e ) {
        consoleLog( "MouseDownEvent" );
        this.mouseDown2 = true;
    }

    @EventHandler( "slider" )
    public void MouseUpEvent( MouseUpEvent e ) {
        consoleLog( "MouseUpEvent" );
        this.mouseDown1 = false;
    }

    @EventHandler( "slider2" )
    public void MouseUpEven2t( MouseUpEvent e ) {
        consoleLog( "MouseUpEvent" );
        this.mouseDown2 = false;
    }

    @EventHandler( "slider" )
    public void MouseMoveEvent( MouseMoveEvent e ) {
        if ( mouseDown1 ) {
            //just one movement per mouse down should also remove mouse

            final int mouse = e.getClientX();
            final int left = slider.getAbsoluteLeft();
            final int right = slider.getAbsoluteRight();
            final int delta = slider.getClientWidth() / 10;

            if ( left + delta >= mouse ) {
//                this.mouseDown1 = false;
                consoleLog( "resize left" );
                String t = column1.getAttribute( "class" );

                int i = t.indexOf( " " );
                String columnSize1Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize1 = Integer.valueOf( columnSize1Str ) - 1;


                t = column2.getAttribute( "class" );

                i = t.indexOf( " " );
                String columnSize2Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize2 = Integer.valueOf( columnSize2Str ) + 1;

                column1.setAttribute( "class", "col-md-"+columnSize1.toString() + " color1" );
                column2.setAttribute( "class", "col-md-"+columnSize2.toString() + " color2" );

            }

            if ( right - delta <= mouse ) {
//                this.mouseDown1 = false;
                consoleLog( "resize right" );
                String t = column2.getAttribute( "class" );
                int i = t.indexOf( " " );
                String columnSize2Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize2 = Integer.valueOf( columnSize2Str ) - 1;


                t = column1.getAttribute( "class" );
                i = t.indexOf( " " );
                String columnSize1Str = String.valueOf(t.charAt( i-1 ));

                Integer columnSize1 = Integer.valueOf( columnSize1Str ) + 1;

                column1.setAttribute( "class", "col-md-"+columnSize1.toString() + " color1" );
                column2.setAttribute( "class", "col-md-"+columnSize2.toString() + " color2" );
            }
        }
    }


    @EventHandler( "slider2" )
    public void MouseMoveEvent2( MouseMoveEvent e ) {
        if ( mouseDown2 ) {
            //just one movement per mouse down should also remove mouse

            final int mouse = e.getClientX();
            final int left = slider2.getAbsoluteLeft();
            final int right = slider2.getAbsoluteRight();
            final int delta = slider2.getClientWidth() / 10;

            if ( left + delta >= mouse ) {
//                this.mouseDown1 = false;
                consoleLog( "resize left" );
                String t = column2.getAttribute( "class" );

                int i = t.indexOf( " " );
                String columnSize2Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize2 = Integer.valueOf( columnSize2Str ) - 1;


                t = column3.getAttribute( "class" );

                i = t.indexOf( " " );
                String columnSize3Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize3 = Integer.valueOf( columnSize3Str ) + 1;

                column2.setAttribute( "class", "col-md-"+columnSize2.toString() + " color2" );
                column3.setAttribute( "class", "col-md-"+columnSize3.toString() + " color3" );

            }

            if ( right - delta <= mouse ) {
//                this.mouseDown1 = false;
                consoleLog( "resize right" );
                String t = column3.getAttribute( "class" );
                int i = t.indexOf( " " );
                String columnSize3Str = String.valueOf(t.charAt( i-1 ));
                Integer columnSize3 = Integer.valueOf( columnSize3Str ) - 1;


                t = column2.getAttribute( "class" );
                i = t.indexOf( " " );
                String columnSize2Str = String.valueOf(t.charAt( i-1 ));

                Integer columnSize2 = Integer.valueOf( columnSize2Str ) + 1;

                column2.setAttribute( "class", "col-md-"+columnSize2.toString() + " color2" );
                column3.setAttribute( "class", "col-md-"+columnSize3.toString() + " color3" );
            }
        }
    }

    native void consoleLog( String message ) /*-{
        console.log("log:" + message);
    }-*/;


}
