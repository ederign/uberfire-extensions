package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;

@Dependent
public class ColumnWithComponents implements Column {

    private final View view;
    private Integer size;
    private int parentHashCode;


    Row row;

    @Inject
    Instance<Row> rowInstance;

    public void setParentHashCode( int parentHashCode ) {
        this.parentHashCode = parentHashCode;
    }

    public void withComponents( Column... _columns ) {
        row = rowInstance.get();
        row.disableDrop();
        row.init( createDropCommand(), componentRemovalCommand() );
        row.addColumns( _columns );
//        column.setParentHashCode( row.hashCode() );
    }

    private ParameterizedCommand<String> componentRemovalCommand() {
        return new ParameterizedCommand<String>() {
            @Override
            public void execute( String parameter ) {
                GWT.log( "remove " + parameter );
            }
        };
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return new ParameterizedCommand<RowDrop>() {
            @Override
            public void execute( RowDrop parameter ) {
                GWT.log( "Drop Column With components" );
            }
        };
    }

    public void addColumnToRow( ComponentColumn newColumn ) {
        row.addColumns( newColumn );
    }

    public interface View extends UberView<ColumnWithComponents> {

        void setSize( String size );

        void addRow( UberView<Row> view );
    }

    @Inject
    public ColumnWithComponents( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    @Override
    public UberView<ColumnWithComponents> getView() {
        if ( hasRow() ) {
            view.addRow( row.getView() );
        }
        return view;
    }

    public boolean hasRow() {
        return row != null;
    }

    public void init( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public Row getRow() {
        return row;
    }
}
