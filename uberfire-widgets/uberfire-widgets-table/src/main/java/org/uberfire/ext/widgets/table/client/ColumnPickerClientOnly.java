package org.uberfire.ext.widgets.table.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CommonResources;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;

import java.util.*;

public class ColumnPickerClientOnly<T> {

    private final DataGrid<T> dataGrid;
    private final List<ColumnMetaClientOnly<T>> columnMetaList = new ArrayList<ColumnMetaClientOnly<T>>();
    private final PopupPanel popup = GWT.create( PopupPanel.class );

    private final List<ColumnChangedHandlerClientOnly> columnChangedHandler = new ArrayList<ColumnChangedHandlerClientOnly>();

    public ColumnPickerClientOnly( DataGrid<T> dataGrid ) {
        this.dataGrid = dataGrid;
        popup.setAutoHideEnabled( true );
        popup.setAutoHideOnHistoryEventsEnabled( true );
    }

    public void addColumnChangedHandler( ColumnChangedHandlerClientOnly handler ) {
        columnChangedHandler.add( handler );
    }

    public void addColumns( List<ColumnMetaClientOnly<T>> columnMetas ) {
        columnMetaList.addAll( columnMetas );
        sortAndAddColumns( columnMetas );
        adjustColumnWidths();
    }

    public void adjustColumnWidths() {
    }

    public void removeColumn( ColumnMetaClientOnly<T> columnMeta ) {
        columnMetaList.remove( columnMeta );
        int count = dataGrid.getColumnCount();
        for ( int i = 0; i < count; i++ ) {
            dataGrid.removeColumn( 0 );
        }

        sortAndAddColumns( columnMetaList );
        adjustColumnWidths();
    }

    protected void sortAndAddColumns( List<ColumnMetaClientOnly<T>> columnMetas ) {
        // Sort based on preferences applied
        Collections.sort( columnMetas );
        //Add the columns based on the preferences
        for ( ColumnMetaClientOnly meta : columnMetas ) {
            addColumn( meta );
        }
    }

    public void addColumn( ColumnMetaClientOnly<T> columnMeta ) {
        if ( columnMeta == null ) {
            return;
        }
        if ( !columnMetaList.contains( columnMeta ) ) {
            columnMetaList.add( columnMeta );
        }
        if ( columnMeta.isVisible() ) {
            dataGrid.addColumn( columnMeta.getColumn(), columnMeta.getHeader() );
        }
    }

    public Button createToggleButton() {
        final Button button = GWT.create( Button.class );
        //FIXME
        button.addStyleName( "columnPickerButton" );
        button.setDataToggle( Toggle.BUTTON );
        button.setIcon( IconType.LIST_UL );
        button.setTitle( "CommonConstants.INSTANCE.ColumnPickerButtonTooltip()" );

        popup.addStyleName( "columnPickerPopup" );
        popup.addAutoHidePartner( button.getElement() );
        popup.addCloseHandler( new CloseHandler<PopupPanel>() {
            public void onClose( CloseEvent<PopupPanel> popupPanelCloseEvent ) {
                if ( popupPanelCloseEvent.isAutoClosed() ) {
                    button.setActive( false );
                }
            }
        } );

        button.addClickHandler( new ClickHandler() {
            public void onClick( ClickEvent event ) {
                if ( !button.isActive() ) {
                    showColumnPickerPopup( button.getAbsoluteLeft() + button.getOffsetWidth(),
                                           button.getAbsoluteTop() + button.getOffsetHeight() );
                } else {
                    popup.hide( false );
                }
            }
        } );
        return button;
    }

    protected DataGrid<T> getDataGrid() {
        return dataGrid;
    }


    protected int getVisibleColumnIndex( final ColumnMetaClientOnly<T> columnMeta ) {
        int index = 0;
        for ( final ColumnMetaClientOnly<T> cm : columnMetaList ) {
            if ( cm.equals( columnMeta ) ) {
                return index;
            }
            if ( cm.isVisible() ) {
                index++;
            }
        }
        return index;
    }

   

    public void columnMoved( final int visibleFromIndex,
                                final int visibleBeforeIndex ) {
        int visibleColumnFromIndex = 0;
        ColumnMetaClientOnly<T> columnMetaToMove = null;
        for ( int i = 0; i < columnMetaList.size(); i++ ) {
            final ColumnMetaClientOnly<T> columnMeta = columnMetaList.get( i );
            if ( columnMeta.isVisible() ) {
                if ( visibleFromIndex == visibleColumnFromIndex ) {
                    columnMetaToMove = columnMeta;
                    break;
                }
                visibleColumnFromIndex++;
            }
        }
        if ( columnMetaToMove == null ) {
            return;
        }

        columnMetaList.remove( columnMetaToMove );

        boolean columnInserted = false;
        int visibleColumnBeforeIndex = 0;
        for ( int i = 0; i < columnMetaList.size(); i++ ) {
            final ColumnMetaClientOnly<T> columnMeta = columnMetaList.get( i );
            if ( columnMeta.isVisible() ) {
                if ( visibleBeforeIndex == visibleColumnBeforeIndex ) {
                    columnMetaList.add( i,
                                        columnMetaToMove );
                    columnInserted = true;
                    break;
                }
                visibleColumnBeforeIndex++;
            }
        }
        if ( !columnInserted ) {
            columnMetaList.add( columnMetaToMove );
        }
    }

    protected String getColumnStoreName( ColumnMetaClientOnly columnMeta ) {
        if ( columnMeta != null ) {
            if ( columnMeta.getColumn() != null ) {
                String colStoreName = columnMeta.getColumn().getDataStoreName();
                if ( colStoreName != null && !colStoreName.isEmpty() ) {
                    return colStoreName;
                }
            }
            return columnMeta.getCaption();
        }
        return "";
    }


    public Collection<ColumnMetaClientOnly<T>> getColumnMetaList() {
        return columnMetaList;
    }

    protected void showPopup( int left, int top, VerticalPanel popupContent ) {
        popup.setWidget( popupContent );
        popup.show();
        int finalLeft = left - popup.getOffsetWidth();
        popup.setPopupPosition( finalLeft, top );
    }

    protected boolean hasResetButtonOnColumnPicker() {
        return false;
    }

    protected boolean addThisColumnToPicker( ColumnMetaClientOnly<T> columnMeta ) {
        return true;
    }

    protected void showColumnPickerPopup( final int left,
                                        final int top ) {
        VerticalPanel popupContent = GWT.create( VerticalPanel.class );

        for ( final ColumnMetaClientOnly<T> columnMeta : getColumnMetaList() ) {
            if ( addThisColumnToPicker( columnMeta ) ) {
                final CheckBox checkBox = GWT.create( CheckBox.class );
                checkBox.setText( columnMeta.getHeader().getValue() );
                checkBox.setValue( columnMeta.isVisible() );
                checkBox.addValueChangeHandler( new ValueChangeHandler<Boolean>() {
                    public void onValueChange( ValueChangeEvent<Boolean> booleanValueChangeEvent ) {
                        boolean visible = booleanValueChangeEvent.getValue();
                        if ( visible ) {
                            getDataGrid().insertColumn( getVisibleColumnIndex( columnMeta ),
                                                        columnMeta.getColumn(),
                                                        columnMeta.getHeader() );
                        } else {
                            getDataGrid().removeColumn( columnMeta.getColumn() );
                        }
                        columnMeta.setVisible( visible );
                        adjustColumnWidths();
                    }
                } );

                popupContent.add( checkBox );
            }
        }

        if ( hasResetButtonOnColumnPicker() ) {
            Button resetButton = GWT.create( Button.class );
            //FIXME
            resetButton.setText( "Reset" );
            resetButton.setSize( ButtonSize.EXTRA_SMALL );
            resetButton.addClickHandler( new ClickHandler() {

                @Override
                public void onClick( ClickEvent event ) {
                    resetTableColumns( left, top );
                }
            } );

            popupContent.add( resetButton );
        }
        showPopup( left, top, popupContent );
    }

    protected void resetTableColumns( int left, int top ) {
    }

    protected List<ColumnChangedHandlerClientOnly> getColumnChangedHandler() {
        return columnChangedHandler;
    }

}
