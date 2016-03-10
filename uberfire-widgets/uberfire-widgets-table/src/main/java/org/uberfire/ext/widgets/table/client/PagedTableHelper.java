package org.uberfire.ext.widgets.table.client;

import com.google.gwt.core.client.GWT;
import org.gwtbootstrap3.client.ui.ListBox;
import org.uberfire.ext.widgets.table.client.resources.i18n.CommonConstants;

public class PagedTableHelper {

    public static void setSelectedValue( final ListBox listbox,
                                   final String value ) {

        for ( int i = 0; i < listbox.getItemCount(); i++ ) {
            if ( listbox.getValue( i ).equals( value ) ) {
                listbox.setSelectedIndex( i );
                return;
            }
        }
    }

    public static void setSelectIndexOnPageSizesSelector( int minPageSize, int maxPageSize, int incPageSize,
                                                    ListBox pageSizesSelector, int pageSize ) {
        for ( int i = minPageSize; i <= maxPageSize; i = i + incPageSize ) {
            pageSizesSelector
                    .addItem( String.valueOf( i ) + " " + CommonConstants.INSTANCE.Items(), String.valueOf( i ) );
            if ( i == pageSize ) {
                for ( int z = 0; z < pageSizesSelector.getItemCount(); z++ ) {
                    if ( pageSizesSelector.getValue( z ).equals( String.valueOf( i ) ) ) {
                        pageSizesSelector.setSelectedIndex( z );
                        break;
                    }
                }
            }
        }
    }
}
