package org.uberfire.ext.layout.editor.client.novo.template.research;

import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class Row {

    private final View view;

    @Inject
    Instance<Column> columnInstance;

    List<Column> columns = new ArrayList<Column>();

    public interface View extends UberView<Row> {

        void addColumn( UberView<Column> view );

        void clearColumns();
    }

    @Inject
    public Row( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
        createDefaultColumns( 12 );
//        createDefaultColumns( 6, 6 );
    }

    @PreDestroy
    public void preDestroy() {
        //TODO destroy all get instances
    }

    ParameterizedCommand<String> dropCommand() {
        return new ParameterizedCommand<String>() {
            @Override
            public void execute( String hash ) {
                view.clearColumns();

                List<Column> newRow = new ArrayList<Column>(  );
                for ( Column column : columns ) {
                    if ( hash.equalsIgnoreCase( column.hashCode() + "" ) ) {
                        //TODO write a better algo
                        final Integer originalSize = column.getSize();
                        final Integer newColumnSize = originalSize / 2;

                        if ( originalSize % 2 == 0 ) {
                            column.setSize( newColumnSize );
                        } else {
                            column.setSize( newColumnSize + 1 );
                        }
                        newRow.add( column );

                        final Column newColumn = columnInstance.get();
                        newColumn.setup( newColumnSize, dropCommand() );
                        newRow.add( newColumn );
                    }
                    else{
                        newRow.add( column );
                    }
                }
                columns = newRow;
                for ( Column column : newRow ) {
                    view.addColumn( column.getView() );
                }
            }
        };
    }


    private void createDefaultColumns( Integer... colSpans ) {
        for ( Integer colSpan : colSpans ) {
            final Column column = columnInstance.get();
            column.setup( colSpan, dropCommand() );
            columns.add( column );
            view.addColumn( column.getView() );
        }
    }

    public UberView<Row> getView() {
        return view;
    }

}
