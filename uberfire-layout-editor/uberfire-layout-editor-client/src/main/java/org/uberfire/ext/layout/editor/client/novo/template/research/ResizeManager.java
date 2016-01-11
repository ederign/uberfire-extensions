package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ResizeManager {


    private Element beginElement;
    private int beginX;
    private Element endElement;
    private int endX;

    public void begin( Element beginElement, int beginX ) {
        this.beginElement = beginElement;
        this.beginX = beginX;
        GWT.log("begin");
    }

    public void end( Element endElement, int endX ) {
        this.endElement = endElement;
        this.endX = endX;
        GWT.log("end");
        if(beginElement==endElement){
            //TODO
            GWT.log("TODO: dnd in same event");
        }
        else{
            handle();
        }
    }

    private void handle() {
        //check if begin and end are thee same element
        if ( left() ) {
            reduce( endElement );
            expand( beginElement );
        } else {
            reduce( endElement );
            expand( beginElement );
        }
    }

    private boolean left() {
        return endX < beginX;
    }

    private void expand( Element column ) {
        GWT.log("TODO: precisa funcionar o algoritmo para 2 digitos");
        String t = column.getAttribute( "class" );
        int i = t.indexOf( " " );
        String columnSize1Str = String.valueOf( t.charAt( i - 1 ) );
        String css = " " + t.substring( i );
        Integer columnSize1 = Integer.valueOf( columnSize1Str ) + 1;
        column.setAttribute( "class", "col-md-" + columnSize1.toString() + css );
    }

    private void reduce( Element column ) {
        String t = column.getAttribute( "class" );
        int i = t.indexOf( " " );
        String columnSize1Str = String.valueOf( t.charAt( i - 1 ) );
        String css = " " + t.substring( i );
        Integer columnSize1 = Integer.valueOf( columnSize1Str ) - 1;
        column.setAttribute( "class", "col-md-" + columnSize1.toString() + css );
    }

    public void registerRow( Element row1, Element... rows ) {
    }

    native void consoleLog( String message ) /*-{
        console.log("log:" + message);
    }-*/;

}
