package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Dependent
public class ColumnWithComponents implements Column {

    private final View view;
    private Integer size;
    int parentHashCode;
    private Row row;
    private Instance<Row> rowInstance;

    @Inject
    public ColumnWithComponents( final View view, Instance<Row> rowInstance ) {
        this.view = view;
        this.rowInstance = rowInstance;
    }

    public interface View extends UberView<ColumnWithComponents> {

        void setSize( String size );

        void addRow( UberView<Row> view );

    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public void init( Integer parentHashCode, Integer size ) {
        this.size = size;
        this.parentHashCode = parentHashCode;
        view.setSize( size.toString() );
    }


    public void removeIfExists( String placeName ) {
        if ( hasRow() ) {

        }
    }

    public void withComponents( Column... _columns ) {
        row = rowInstance.get();
        row.disableDrop();
        row.init( createDropCommand(), componentRemovalCommand() );
        row.addColumns( _columns );
    }

    ParameterizedCommand<String> componentRemovalCommand() {
        return parameter -> {
        };
    }

    ParameterizedCommand<RowDrop> createDropCommand() {
        return rowDrop -> {
        };
    }


    @Override
    public UberView<ColumnWithComponents> getView() {
        if ( hasRow() ) {
            view.addRow( row.getView() );
        }
        return view;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void reduzeSize() {
        final int newSize = this.size - 1;
        setSize( newSize );
    }

    @Override
    public void incrementSize() {
        final int newSize = this.size + 1;
        setSize( newSize );
    }

    public void setSize( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public boolean hasRow() {
        return row != null;
    }


    public Row getRow() {
        return row;
    }

    public int getParentHashCode() {
        return parentHashCode;
    }
}
