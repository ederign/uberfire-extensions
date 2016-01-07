package org.uberfire.ext.layout.editor.client.novo.template.research;

public class TEst {

    public static void main( String[] args ) {
        String t = "col-md-5 color1";
        final int i = t.indexOf( " " );
        String column = String.valueOf(t.charAt( i-1 ));
        System.out.println(column);

    }
}
