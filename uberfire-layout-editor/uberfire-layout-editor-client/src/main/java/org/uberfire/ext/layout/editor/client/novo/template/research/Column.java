package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class Column {

    @Inject
    ColumnResizeManager columnResizeManager;

    @Inject
    DnDManager dndManager;

    private final View view;

    private Type columnType;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;


    boolean canResize() {
        return columnType == Type.MIDDLE;
    }

    public void endResize( int xPosition ) {
        columnResizeManager.end( hashCode(), xPosition );
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
        this.columnType = Type.DnD;
        this.dropCommand = columnDropParameterizedCommand;
        view.setSize( size.toString() );
        view.setContent( "Drop your first component here..." );
    }

    public void onMouseDown( int xPosition ) {
        if ( canResize() ) {
            dndManager.beginColumnResize( hashCode(), xPosition );
        }
    }

    public void onMouseUp( int xPosition ) {
        dndManager.endColumnResize( xPosition );
    }

    public interface View extends UberView<Column> {

        void setCursor();

        void setSize( String size );

        void setContent( String contentLabel );
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
        return view;
    }

    public void init( Type columnType, Integer size, ParameterizedCommand<ColumnDrop> dropCommand ) {
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setContent( hashCode() + "" );
        view.setCursor();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public void onDrop( int dropPosition, int columnMiddleX ) {
        dropCommand.execute( new ColumnDrop( hashCode(), dropPosition, columnMiddleX ) );
    }


    public enum Type {
        FIRST, MIDDLE, DnD;
    }
}
