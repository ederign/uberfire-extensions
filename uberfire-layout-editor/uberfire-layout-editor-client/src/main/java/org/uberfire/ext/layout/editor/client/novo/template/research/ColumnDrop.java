package org.uberfire.ext.layout.editor.client.novo.template.research;

public class ColumnDrop{

    final int hash;
    final int dropXPosition;
    int columnMiddleX;

    ColumnDrop( int hash, int dropXPosition, int columnMiddleX){

        this.hash = hash;
        this.dropXPosition = dropXPosition;
        this.columnMiddleX = columnMiddleX;
    }
}