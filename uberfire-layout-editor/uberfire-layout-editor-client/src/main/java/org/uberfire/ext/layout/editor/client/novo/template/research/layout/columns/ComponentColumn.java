package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
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

    //UF BUG
    private Integer panelSize = 100;

    public interface View extends UberView<ComponentColumn> {

        void setCursor();

        void setSize( String size );

        void calculateSize();

        void setContent( String place, String size );

    }

    @Inject
    public ComponentColumn( final View view, DnDManager dndManager ) {
        this.view = view;
        this.dndManager = dndManager;
    }

    @PostConstruct
    public void post() {
        view.init( this );
    }

    public void init( int parentHashCode, Type columnType, Integer size,
                      ParameterizedCommand<ColumnDrop> dropCommand, String place ) {
        this.parentHashCode = parentHashCode;
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setCursor();
        this.place = place;
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
        view.setContent( place, panelSize.toString() );
        return view;
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



    public void onDrop( ColumnDrop.Orientation orientation, String dndData ) {
        dropCommand.execute( new ColumnDrop( hashCode(), orientation, dndData ) );
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
