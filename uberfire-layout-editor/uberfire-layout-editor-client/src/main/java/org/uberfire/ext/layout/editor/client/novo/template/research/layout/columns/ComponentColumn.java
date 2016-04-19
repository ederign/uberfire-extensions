package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import org.gwtbootstrap3.client.ui.Modal;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.api.editor.LayoutComponent;
import org.uberfire.ext.layout.editor.client.components.*;
import org.uberfire.ext.layout.editor.client.dnd.DndDataJSONConverter;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.RepaintContainerEvent;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Dependent
public class ComponentColumn implements Column {

    private DnDManager dndManager;

    private final View view;

    private int parentHashCode;

    private Type columnType;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;

    boolean innerColumn = false;

    boolean containerColumn = false;

    private String place;

    // eder
    //We should create a Component Presenter containing view
    //properties and etc.
    private LayoutDragComponent component;

    //ederign properties from ^
    //ainda nao sei pq tem isto
    private LayoutComponent layoutComponent = new LayoutComponent();


    //UF BUG - remove it
    private Integer panelSize = 100;

    private Event<RepaintContainerEvent> repaintContainerEvent;
    //leak
    private boolean previewWidget;

    public interface View extends UberView<ComponentColumn> {

        void setCursor();

        void setSize( String size );

        void calculateSize();

        void setContent( IsWidget widget );

    }

    @Inject
    public ComponentColumn( final View view, DnDManager dndManager,
                            Event<RepaintContainerEvent> repaintContainerEvent ) {
        this.view = view;
        this.dndManager = dndManager;
        this.repaintContainerEvent = repaintContainerEvent;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public void init( int parentHashCode, Type columnType, Integer size,
                      ParameterizedCommand<ColumnDrop> dropCommand, String place, LayoutDragComponent component ) {
        this.parentHashCode = parentHashCode;
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setCursor();
        //WHY ederign
        this.place = place;
        this.component = component;
        if ( componentHasConfiguration( component ) ) {
            showConfigurationScreen( component );
        }
    }

    private boolean componentHasConfiguration( LayoutDragComponent component ) {
        return component instanceof HasConfiguration;
    }

    private void showConfigurationScreen( LayoutDragComponent component ) {
        //extract to otherClassPresenter
        if ( component instanceof HasModalConfiguration ) {
            ModalConfigurationContext ctx = new ModalConfigurationContext( layoutComponent,
                                                                           this::configurationFinish,
                                                                           this::configurationCanceled );
            Modal configModal = ( ( HasModalConfiguration ) component ).getConfigurationModal( ctx );
            configModal.show();
        } else if ( component instanceof HasPanelConfiguration ) {
            PanelConfigurationContext ctx = new PanelConfigurationContext( layoutComponent, null, null );
            Panel configPanel = ( ( HasPanelConfiguration ) component ).getConfigurationPanel( ctx );
            //TODO update view
//            componentEditorWidget.getWidget().clear();
//            componentEditorWidget.getWidget().add( configPanel );
        }
    }

    private void configurationFinish() {
        this.previewWidget = true;
//        //ederign widget leak
        view.setContent( getPreviewWidget() );
        view.calculateSize();
        GWT.log( "config finish" );
    }

    private void configurationCanceled() {
        GWT.log( "configurationCanceled" );
    }

    private LayoutComponent getLayoutComponent() {
        return null;
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

    public void onMouseDown( int xPosition ) {
        if ( canResize() ) {
            dndManager.beginColumnResize( hashCode(), xPosition );
        } else if ( canMove() ) {
            dndManager.beginRowMove( parentHashCode );
        }
    }

    boolean canResize() {
        return columnType == Type.MIDDLE;
    }

    boolean canMove() {
        return columnType == Type.FIRST_COLUMN;
    }


    public void onMouseUp( int xPosition ) {
        dndManager.endColumnResize( parentHashCode, xPosition );
    }


    public Integer getPanelSize() {
        return panelSize;
    }

    public String dragInfo() {
        return place;
    }


    public UberView<ComponentColumn> getView() {
        view.calculateSize();
        view.setCursor();
        //ederign
        return view;
    }

    private IsWidget getPreviewWidget() {
        IsWidget previewWidget = component.getPreviewWidget( new RenderingContext( layoutComponent, null ) );
        return previewWidget;
    }

    public void recalculateSize() {
        view.calculateSize();
    }

    public void halfParentPanelSize( Integer panelSize ) {
        this.panelSize = panelSize / 2;
    }

    public void setPanelSize( Integer panelSize ) {
        this.panelSize = panelSize;
    }

    public int getParentHashCode() {
        return parentHashCode;
    }

    @Override
    public Integer getSize() {
        return size;
    }


    //ederign duplicated code from row.java
    private DndDataJSONConverter converter = new DndDataJSONConverter();

    private LayoutDragComponent extractComponent( DropEvent dropEvent ) {
        return converter
                .readJSONDragComponent( dropEvent.getData( LayoutDragComponent.FORMAT ) );
    }

    public void onDrop( ColumnDrop.Orientation orientation, DropEvent dropEvent ) {
        LayoutDragComponent layoutDragComponent = extractComponent( dropEvent );
        dropCommand.execute( new ColumnDrop( layoutDragComponent, hashCode(), orientation ) );
    }

    public enum Type {
        FIRST_COLUMN, MIDDLE;
    }

    public void setColumnType( Type columnType ) {
        this.columnType = columnType;
    }

    protected boolean isInnerColumn() {
        return innerColumn;
    }

    public boolean isContainerColumn() {
        return containerColumn;
    }

    public String getPlace() {
        return place;
    }
}
