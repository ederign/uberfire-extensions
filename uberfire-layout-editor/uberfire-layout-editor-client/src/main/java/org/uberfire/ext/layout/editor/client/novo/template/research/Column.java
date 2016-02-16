package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class Column {

    @Inject
    DnDManager dndManager;

    private final View view;

    private int parentHashCode;

    private Type columnType;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;

    @Inject
    Instance<Row> rowInstance;

    List<Row> rows = new ArrayList<Row>();


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
            dndManager.beginRowMove( parentHashCode );
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

    public void withComponents( Column column, Column newColumn ) {
        GWT.log( "TODO YEAH" );
        final Row row = rowInstance.get();
        row.init( createDropCommand() );
        row.addColumns(column, newColumn);
        rows.add( row );
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return new ParameterizedCommand<RowDrop>() {
            @Override
            public void execute( RowDrop parameter ) {
                GWT.log( "TODO DROP" );
            }
        };
    }

    public interface View extends UberView<Column> {

        void setCursor();

        void setSize( String size );

        void setContent( String contentLabel );

        void calculateSize();

        void addRow( UberView<Row> view );
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
        if ( hasRows() ) {
            GWT.log( "YOOOOOPIUI" + rows.size() );
            for ( Row row : rows ) {
                view.addRow(row.getView());
            }
            
        } else {
            view.calculateSize();
            view.setCursor();
        }

        return view;
    }

    private boolean hasRows() {
        return !rows.isEmpty();
    }

    public void init( int parentHashCode, Type columnType, Integer size,
                      ParameterizedCommand<ColumnDrop> dropCommand, String content ) {
        this.parentHashCode = parentHashCode;
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setContent( content );
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
