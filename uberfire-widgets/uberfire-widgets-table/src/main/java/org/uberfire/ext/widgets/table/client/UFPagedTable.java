package org.uberfire.ext.widgets.table.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import org.gwtbootstrap3.client.ui.ListBox;
import org.uberfire.ext.widgets.table.client.resources.i18n.CommonConstants;

public class UFPagedTable<T>
        extends UFSimpleTable<T> {

    interface Binder
            extends
            UiBinder<Widget, UFPagedTable> {

    }

    public static final int DEFAULT_PAGE_SIZE = 10;

    private static Binder uiBinder = GWT.create( Binder.class );

    private int pageSize = 0;

    private AbstractDataProvider<T> dataProvider;

    @UiField
    public UberfireSimplePager pager;

    @UiField
    public ListBox pageSizesSelector;

    protected boolean showPageSizesSelector = false;

    public UFPagedTable() {
        this( DEFAULT_PAGE_SIZE );
    }

    public UFPagedTable( final int pageSize ) {
        this( pageSize, null );
    }

    public UFPagedTable( final int pageSize,
                         final ProvidesKey<T> providesKey ) {
        this( pageSize, providesKey, false );
    }

    public UFPagedTable( final int pageSize,
                         final ProvidesKey<T> providesKey,
                         final boolean showPageSizesSelector ) {

        this( pageSize, providesKey, showPageSizesSelector, false, false );
    }

    public UFPagedTable( final int pageSize,
                         final ProvidesKey<T> providesKey,
                         final boolean showPageSizesSelector,
                         final boolean showFFButton,
                         final boolean showLButton ) {
        super( providesKey );
        this.showPageSizesSelector = showPageSizesSelector;
        this.pageSize = pageSize;
        this.dataGrid.setPageSize( pageSize );
        PagedTableHelper.setSelectedValue( pageSizesSelector, String.valueOf( pageSize ) );
        this.pager.setDisplay( dataGrid );
        this.pageSizesSelector.setVisible( this.showPageSizesSelector );
        setShowFastFordwardPagerButton( showFFButton );
        setShowLastPagerButton( showLButton );
        createPageSizesListBox( 5, 20, 5 );
    }

    @Override
    protected Widget makeWidget() {
        return uiBinder.createAndBindUi( this );
    }

    public void setDataProvider( final AbstractDataProvider<T> dataProvider ) {
        this.dataProvider = dataProvider;
        this.dataProvider.addDataDisplay( this );
    }

    public AbstractDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageStart() {
        return this.pager.getPageStart();
    }

    public void createPageSizesListBox( int minPageSize,
                                        int maxPageSize,
                                        int incPageSize ) {
        pageSizesSelector.clear();
        PagedTableHelper.setSelectIndexOnPageSizesSelector( minPageSize, maxPageSize, incPageSize, pageSizesSelector,
                                                            pageSize );
        pageSizesSelector.addChangeHandler( event -> loadPageSizePreferences() );

        loadPageSizePreferences();

    }

    public final void loadPageSizePreferences() {
        this.dataGrid.setPageSize( pageSize );
        this.pager.setPageSize( pageSize );
        this.dataGrid.setHeight( ( ( pageSize == 0 ? 1 : pageSize ) * 30 + 10 ) + "px" );
    }


    public void setShowLastPagerButton( boolean showLastPagerButton ) {
        this.pager.setShowLastPageButton( showLastPagerButton );
    }

    public void setShowFastFordwardPagerButton( boolean showFastFordwardPagerButton ) {
        this.pager.setShowFastFordwardPageButton( showFastFordwardPagerButton );
    }


}