package org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class ColumnResizeManager {

    @Inject
    Event<ColumnResizeEvent> columnResizeEvent;


    private int columnHashCodeBegin;
    private int columnHashCodeEnd;
    private boolean isOnDnD;


    public void begin( int columnHashCodeBegin, int beginX ) {
        GWT.log( "beginColumnResize resize" );
        this.columnHashCodeBegin = columnHashCodeBegin;
//        this.beginX = beginX;
        this.isOnDnD = true;
    }

    public void end( int columnHashCodeEnd, int endX ) {
        if ( isOnDnD ) {
            //check if is in the not same column
            if ( columnHashCodeBegin == columnHashCodeEnd ) {
                GWT.log( "TODO: columnResizeManager in same event or in the same column" );
            } else {
                handle( endX );
            }
        }
        this.columnHashCodeEnd = columnHashCodeEnd;
        GWT.log( "endColumnResize resize" );
    }

    public boolean isOnDnD() {
        return isOnDnD;
    }

    public void reset() {
        if ( isOnDnD() ) {
            GWT.log( "row out" );
            this.isOnDnD = false;
//            this.columnHashCodeBegin = -1;
//            this.beginX = -1;
        }
    }

    private void handle( int endX ) {
        //check if beginColumnResize and endColumnResize are thee same element
        if ( left( endX ) ) {
//            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCodeBegin, columnHashCodeEnd ).left() );
//            reduce( endElement );
//            expand( beginElement );
        } else {
//            columnResizeEvent.fire( new ColumnResizeEvent( columnHashCodeBegin, columnHashCodeEnd ).right() );
//            reduce( endElement );
//            expand( beginElement );
        }
    }

    private boolean left( int endX ) {
//        return endX < beginX;
        return false;
    }

    private void expand( Element column ) {
        GWT.log( "TODO: precisa funcionar o algoritmo para 2 digitos" );
        //TODO write real parser
        String t = column.getAttribute( "class" );
//        int i = t.indexOf( " " );
        String columnSize1Str = String.valueOf( t.charAt( t.length() - 1 ) );
//        String columnSize1Str = String.valueOf( t.charAt( i - 1 ) );
//        String css = " " + t.substring( i );
        String css = "";
        Integer columnSize1 = Integer.valueOf( columnSize1Str ) + 1;
        column.setAttribute( "class", "col-md-" + columnSize1.toString() + css );
    }

    private void reduce( Element column ) {
        String t = column.getAttribute( "class" );
//        int i = t.indexOf( " " );
//        String columnSize1Str = String.valueOf( t.charAt( i - 1 ) );
        String columnSize1Str = String.valueOf( t.charAt( t.length() - 1 ) );
//        String css = " " + t.substring( i );
        String css = "";
        Integer columnSize1 = Integer.valueOf( columnSize1Str ) - 1;
        column.setAttribute( "class", "col-md-" + columnSize1.toString() + css );
    }

    public void registerRow( Element row1, Element... rows ) {
    }

    native void consoleLog( String message ) /*-{
        console.log("log:" + message);
    }-*/;
}
