package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class Column {

    @Inject
    DnDManager dndManager;

    private final View view;

    private int parentHashCode;
    private Type columnType;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;


    boolean canResize() {
        return columnType == Type.MIDDLE;
    }

    boolean canMove() {
        return columnType == Type.FIRST;
    }

    public boolean canReduceSize() {
        return this.size > 1;
    }

    public void reduzeSize() {
        final int newSize = this.size - 1;
        setSize( newSize );
    }

    public void incrementSize() {
        final int newSize = this.size + 1;
        setSize( newSize );
    }

    public void defaultEmptyColumn( ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand ) {
        this.size = 12;
        this.columnType = Type.FIRST;
        this.dropCommand = columnDropParameterizedCommand;
        view.setSize( size.toString() );
    }

    public void columnFromDrop( ParameterizedCommand<ColumnDrop> columnDropParameterizedCommand ) {
        this.size = 12;
        this.columnType = Type.FIRST;
        this.dropCommand = columnDropParameterizedCommand;
        view.setSize( size.toString() );
        view.setContent( hashCode() + "" );
    }

    public void onMouseDown( int xPosition ) {
        GWT.log( "onMouseDown" );
        if ( canResize() ) {
            GWT.log( "canResize" );
            dndManager.beginColumnResize( hashCode(), xPosition );
        } else if ( canMove() ) {
            GWT.log( "canMov" );
//            dndManager.beginRowMove( parentHashCode );
        } else {
            GWT.log( "nop" );
        }
    }

    public void onMouseUp( int xPosition ) {
        dndManager.endColumnResize( parentHashCode, xPosition );
    }

    public void onMouseOver( MouseOverInfo mouseOverInfo ) {
        if ( dndManager.isOnDnd() ) {
            GWT.log( "MOUSE OVER DND: " + mouseOverInfo.toString() );
        }
    }

    public interface View extends UberView<Column> {

        void setCursor();

        void setSize( String size );

        void setContent( String contentLabel );

        void calculateSize();
    }

    @Inject
    public Column( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public UberView<Column> getView() {
        //TODO COLUMN UPDATE PROCSS
        view.calculateSize();
        view.setCursor();
        return view;
    }

    public void init( int parentHashCode, Type columnType, Integer size,
                      ParameterizedCommand<ColumnDrop> dropCommand ) {
        this.parentHashCode = parentHashCode;
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setContent( "Parent: " + parentHashCode + " " + hashCode() + "" );
        view.setCursor();
    }

    public int getParentHashCode() {
        return parentHashCode;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public void onDrop( ColumnDrop.Orientation orientation ) {
        dropCommand.execute( new ColumnDrop( hashCode(), orientation ) );
    }


    public enum Type {
        FIRST, MIDDLE;
    }

    public void setColumnType( Type columnType ) {
        this.columnType = columnType;
    }

    public void setParentHashCode( int parentHashCode ) {
        this.parentHashCode = parentHashCode;
    }

    @Override
    public String toString() {
        return "Column{" +
                "size=" + size +
                ", hashCode=" + hashCode() +
                ", parentHashCode=" + parentHashCode +
                ", columnType=" + columnType +
                '}';
    }
}
