package org.uberfire.ext.layout.editor.client.novo.template.research.layout.columns;

import com.google.gwt.core.client.GWT;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.ColumnDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.DnDManager;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.infra.MouseOverInfo;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.Row;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.rows.RowDrop;
import org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens.Screens;
import org.uberfire.mvp.ParameterizedCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Dependent
public class ComponentColumn implements Column {

    @Inject
    DnDManager dndManager;

    private final View view;

    private int parentHashCode;

    private Type columnType;

    private Integer size;

    private ParameterizedCommand<ColumnDrop> dropCommand;

    @Inject
    Instance<Row> rowInstance;

    //gambiarra pra acertar o padding de inner columns
    boolean innerColumn = false;
    boolean containerColumn = false;
    private String place;
    //UF BUG
    private Integer panelSize = 100;


    boolean canResize() {
        return columnType == Type.MIDDLE;
    }

    boolean canMove() {
        return columnType == Type.FIRST;
    }

    public boolean canReduceSize() {
        return this.size > 1;
    }

    public void reduzeSize() {
        final int newSize = this.size - 1;
        setSize( newSize );
    }

    public void incrementSize() {
        final int newSize = this.size + 1;
        setSize( newSize );
    }

    public void onMouseDown( int xPosition ) {
        GWT.log( "onMouseDown" );
        if ( canResize() ) {
            GWT.log( "canResize" );
            dndManager.beginColumnResize( hashCode(), xPosition );
        } else if ( canMove() ) {
            GWT.log( "canMov" );
            dndManager.beginRowMove( parentHashCode );
        } else {
            GWT.log( "nop" );
        }
    }

    public void onMouseUp( int xPosition ) {
        dndManager.endColumnResize( parentHashCode, xPosition );
    }

    public void onMouseOver( MouseOverInfo mouseOverInfo ) {
        if ( dndManager.isOnDnd() ) {
            GWT.log( "MOUSE OVER DND: " + mouseOverInfo.toString() );
        }
    }

    private ParameterizedCommand<RowDrop> createDropCommand() {
        return new ParameterizedCommand<RowDrop>() {
            @Override
            public void execute( RowDrop parameter ) {
                GWT.log( "TODO DROP" );
            }
        };
    }

    public void innerColumn() {
        this.innerColumn = true;
    }

    public void containerColumn() {
        this.containerColumn = true;
    }

    public Integer getPanelSize() {
        return panelSize;
    }

    public String dragInfo() {
        return place;
    }

    public interface View extends UberView<ComponentColumn> {

        void setCursor();

        void setSize( String size );

        void calculateSize();

        void addRow( UberView<Row> view );

        void setContent( String place, String size );
    }

    @Inject
    public ComponentColumn( final View view ) {
        this.view = view;
    }

    @PostConstruct
    public void post() {
        view.init( this );
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

    public void halfParentPanelSize(Integer panelSize){
        this.panelSize = panelSize/2;
    }

    public void setPanelSize(Integer panelSize){
        this.panelSize = panelSize;
    }

    public void init( int parentHashCode, Type columnType, Integer size,
                      ParameterizedCommand<ColumnDrop> dropCommand, String place  ) {
        this.parentHashCode = parentHashCode;
        this.columnType = columnType;
        this.size = size;
        this.dropCommand = dropCommand;
        view.setSize( size.toString() );
        view.setCursor();
        this.place = place;
    }

    public int getParentHashCode() {
        return parentHashCode;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    public void setSize( Integer size ) {
        this.size = size;
        view.setSize( size.toString() );
    }

    public void onDrop( ColumnDrop.Orientation orientation, String dndData ) {
        GWT.log( dndData + " <- dndData" );
        dropCommand.execute( new ColumnDrop( hashCode(), orientation, dndData ) );
    }


    public enum Type {
        FIRST, MIDDLE;
    }

    public void setColumnType( Type columnType ) {
        this.columnType = columnType;
    }

    public void setParentHashCode( int parentHashCode ) {
        this.parentHashCode = parentHashCode;
    }

    @Override
    public String toString() {
        return "Column{" +
                "size=" + size +
                ", hashCode=" + hashCode() +
                ", parentHashCode=" + parentHashCode +
                ", columnType=" + columnType +
                '}';
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
