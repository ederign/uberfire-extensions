package org.uberfire.ext.layout.editor.client.novo;

import com.google.gwt.dom.client.Element;

import java.util.HashMap;
import java.util.Map;

public class CSSHack {

    public static void setClassLayoutContainer( Element element ) {
        Map<String, String> styles = new HashMap<String, String>();
        styles.put( "backgroundColor", "#9c9cff" );

        for ( String s : styles.keySet() ) {
            element.getStyle().setProperty( s, styles.get( s ) );
        }

    }

    public static void setClassLayoutContainer2( Element element ) {
        Map<String, String> styles = new HashMap<String, String>();
        styles.put( "backgroundColor", "red" );

        for ( String s : styles.keySet() ) {
            element.getStyle().setProperty( s, styles.get( s ) );
        }

    }
}
