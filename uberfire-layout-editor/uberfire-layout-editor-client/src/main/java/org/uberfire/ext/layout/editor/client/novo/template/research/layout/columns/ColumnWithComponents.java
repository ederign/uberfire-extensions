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
import java.util.ArrayList;
import java.util.List;

@Dependent
public class ColumnWithComponents implements Column {

    private final View view;
    private Integer size;
    private int parentHashCode;


    List<Row> rows = new ArrayList<>();
    
    @Inject
    Instance<Row> rowInstance;

    public void setParentHashCode( int parentHashCode ) {
        this.parentHashCode = parentHashCode;
    }

    public void withComponents( Column... _columns ) {
        final Row row = rowInstance.get();
        row.init( createDropCommand() );
        row.addColumns( _columns );
//        column.setParentHashCode( row.hashCode() );
        rows.add( row );
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return new ParameterizedCommand<RowDrop>() {
            @Override
            public void execute( RowDrop parameter ) {
                GWT.log("Drop");
            }
        };
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
        if ( hasRows() ) {
            for ( Row row : rows ) {
                view.addRow( row.getView() );
            }
        }
        return view;
    }

    private boolean hasRows() {
        return !rows.isEmpty();
    }

    public void init( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

}
