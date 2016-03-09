/*
 * Copyright 2015 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.widgets.common.client.tables;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.uberfire.ext.services.shared.preferences.GridColumnPreference;
import org.uberfire.ext.services.shared.preferences.GridPreferencesStore;
import org.uberfire.ext.widgets.common.client.resources.CommonResources;
import org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants;
import org.uberfire.ext.widgets.table.client.ColumnChangedHandlerClientOnly;
import org.uberfire.ext.widgets.table.client.ColumnMetaClientOnly;
import org.uberfire.ext.widgets.table.client.ColumnPickerClientOnly;

import java.util.*;

public class ColumnPicker<T> extends ColumnPickerClientOnly<T> {


    private GridPreferencesStore gridPreferences;

    public ColumnPicker( DataGrid<T> dataGrid,
                         GridPreferencesStore gridPreferences ) {
        super( dataGrid );
        this.gridPreferences = gridPreferences;
    }

    public ColumnPicker( DataGrid<T> dataGrid ) {
        super( dataGrid );
    }


    @Override
    protected void sortAndAddColumns( List<ColumnMetaClientOnly<T>> columnMetas ) {
        // Check for column preferences and orders
        for ( ColumnMetaClientOnly meta : columnMetas ) {
            checkColumnMeta( meta );
        }
      super.sortAndAddColumns( columnMetas );
    }

    protected void checkColumnMeta( ColumnMetaClientOnly<T> columnMeta ) {
        if ( gridPreferences != null ) {
            List<GridColumnPreference> columnPreferences = gridPreferences.getColumnPreferences();
            if ( !columnPreferences.isEmpty() ) {
                boolean found = false;
                for ( int i = 0; i < gridPreferences.getColumnPreferences().size() && !found; i++ ) {
                    GridColumnPreference gcp = gridPreferences.getColumnPreferences().get( i );
                    if ( gcp.getName().equals( getColumnStoreName( columnMeta ) ) ) {
                        columnMeta.setVisible( true );
                        if ( gcp.getWidth() != null ) {
                            getDataGrid().setColumnWidth( columnMeta.getColumn(), gcp.getWidth() );
                        } else {
                            getDataGrid().setColumnWidth( columnMeta.getColumn(), 100, Style.Unit.PCT );
                        }
                        columnMeta.setPosition( gcp.getPosition() );
                        found = true;
                    }
                }
                if ( !found ) {
                    columnMeta.setPosition( -1 );
                    columnMeta.setVisible( false );
                }
            } else if ( gridPreferences.getGlobalPreferences() != null ) {
                int position = gridPreferences.getGlobalPreferences().getInitialColumns()
                        .indexOf( getColumnStoreName( columnMeta ) );
                if ( position != -1 ) {
                    columnMeta.setVisible( true );
                    columnMeta.setPosition( position );
                } else {
                    columnMeta.setPosition( -1 );
                    columnMeta.setVisible( false );
                }
            }
        }
    }


    public void setGridPreferencesStore( GridPreferencesStore gridPreferences ) {
        this.gridPreferences = gridPreferences;
    }



    public List<GridColumnPreference> getColumnsState() {
        List<GridColumnPreference> state = new ArrayList<GridColumnPreference>();
        for ( final ColumnMetaClientOnly<T> cm : getColumnMetaList() ) {
            if ( cm.isVisible() ) {
                state.add( new GridColumnPreference( getColumnStoreName( cm ),
                                                     getDataGrid().getColumnIndex( cm.getColumn() ),
                                                     getDataGrid().getColumnWidth( cm.getColumn() ) ) );
            }
        }
        return state;
    }


    @Override
    protected boolean hasResetButtonOnColumnPicker() {
        return gridPreferences != null;
    }

    @Override
    protected boolean addThisColumnToPicker( ColumnMetaClientOnly<T> columnMeta ) {
        return gridPreferences == null || !gridPreferences.getGlobalPreferences()
                .getBannedColumns().contains( getColumnStoreName( columnMeta ) );
    }

    @Override
    public void adjustColumnWidths() {
        for ( ColumnChangedHandlerClientOnly handler : getColumnChangedHandler() ) {
            handler.afterColumnChanged();
        }

        List<GridColumnPreference> preferences = getColumnsState();

        if ( preferences.isEmpty() ) {
            return;
        }
        if ( preferences.size() == 1 ) {
            getDataGrid().setColumnWidth( getDataGrid().getColumn( 0 ),
                                     100,
                                     Style.Unit.PCT );
            return;
        }

        int fixedColumnsWidth = 0;
        Map<String, String> fixedWidths = new HashMap<String, String>();
        List<String> columnsToCalculate = new ArrayList<String>();

        for ( GridColumnPreference preference : preferences ) {
            if ( preference.getWidth() != null && preference.getWidth().endsWith( Style.Unit.PX.getType() ) ) {
                fixedWidths.put( preference.getName(), preference.getWidth() );
                fixedColumnsWidth += Integer.decode( preference.getWidth().substring( 0, preference.getWidth()
                        .indexOf( Style.Unit.PX.getType() ) ) );
            } else {
                columnsToCalculate.add( preference.getName() );
            }
        }

        if ( columnsToCalculate.size() > 0 ) {

            double columnPCT = 100 / columnsToCalculate.size();

            if ( getDataGrid().getOffsetWidth() != 0 ) {
                int availabelColumnSpace = getDataGrid().getOffsetWidth() - fixedColumnsWidth;
                double availablePCT = availabelColumnSpace * 100 / getDataGrid().getOffsetWidth();
                columnPCT = columnPCT * availablePCT / 100;
            }

            for ( ColumnMetaClientOnly<T> cm : getColumnMetaList() ) {
                if ( cm.isVisible() ) {
                    if ( columnsToCalculate.contains( getColumnStoreName( cm ) ) ) {
                        getDataGrid().setColumnWidth( cm.getColumn(),
                                                 columnPCT,
                                                 Style.Unit.PCT );
                    } else {
                        getDataGrid().setColumnWidth( cm.getColumn(), fixedWidths.get( getColumnStoreName( cm ) ) );
                    }
                }
            }
        }
    }

    @Override
    protected void resetTableColumns( int left,
                                      int top ) {
        gridPreferences.resetGridColumnPreferences();
        int count = getDataGrid().getColumnCount();
        for ( int i = 0; i < count; i++ ) {
            getDataGrid().removeColumn( 0 );
        }

        for ( final ColumnMetaClientOnly<T> columnMeta : getColumnMetaList() ) {
            int position = gridPreferences.getGlobalPreferences().getInitialColumns().indexOf( getColumnStoreName( columnMeta ) );
            columnMeta.setPosition( position );
            columnMeta.setVisible( position > -1 );
        }

        sortAndAddColumns( new ArrayList<ColumnMetaClientOnly<T>>( getColumnMetaList() ) );

        adjustColumnWidths();

        showColumnPickerPopup( left, top );
    }

}
