package org.uberfire.ext.layout.editor.client.novo.template.research;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.mvp.UberView;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class SimpleView extends Composite
        implements UberView<SimplePresenter>,
        SimplePresenter.View {

    private SimplePresenter presenter;

    @DataField
    private Element container = DOM.createDiv();

    @DataField
    private Element row1 = DOM.createDiv();

    @DataField
    private Element row1col1 = DOM.createDiv();

    @DataField
    private Element row1col2 = DOM.createDiv();

    @DataField
    private Element row1col3 = DOM.createDiv();

    @DataField
    private Element row1col1Content = DOM.createDiv();

    @DataField
    private Element row1col2Content = DOM.createDiv();

    @DataField
    private Element row1col3Content = DOM.createDiv();

    @Inject
    ColumnResizeManager dnd;

    @Override
    public void init( SimplePresenter presenter ) {
        this.presenter = presenter;



        row1col1Content.getStyle().setCursor( Style.Cursor.DEFAULT );
        row1col1.getStyle().setCursor( Style.Cursor.COL_RESIZE );

        row1col2Content.getStyle().setCursor( Style.Cursor.DEFAULT );
        row1col2.getStyle().setCursor( Style.Cursor.COL_RESIZE );

        row1col3Content.getStyle().setCursor( Style.Cursor.DEFAULT );
        row1col3.getStyle().setCursor( Style.Cursor.COL_RESIZE );

        dnd.registerRow(row1, row1col1, row1col2, row1col3);
    }

    @EventHandler( "row1col1" )
    public void row1col1Up( MouseUpEvent e ) {
        e.preventDefault();
        consoleLog( "MouseUpEvent row1col1 " + e.getClientX() );
//        dnd.endColumnResize( row1col1, e.getClientX() );
    }

    @EventHandler( "row1col1" )
    public void row1col1Down( MouseDownEvent e ) {
        e.preventDefault();
        consoleLog( "MouseDownEvent row1col1 " + e.getClientX() );
//        dnd.beginColumnResize( row1col1, e.getClientX() );
    }

    @EventHandler( "row1col2" )
    public void row1col2Down( MouseDownEvent e ) {
        e.preventDefault();
//        dnd.beginColumnResize( row1col2, e.getClientX() );
        consoleLog( "MouseDownEvent row1col2 " + e.getClientX() );
    }

    @EventHandler( "row1col2" )
    public void row1col2rMouseUp( MouseUpEvent e ) {
        e.preventDefault();
//        dnd.endColumnResize( row1col2, e.getClientX() );
        consoleLog( "MouseUpEvent row1col2 " + e.getClientX() );
    }

    @EventHandler( "row1col3" )
    public void row1col3rMouseUp( MouseUpEvent e ) {
        e.preventDefault();
//        dnd.endColumnResize( row1col3, e.getClientX() );
        consoleLog( "MouseUpEvent row1col3 " + e.getClientX() );
    }

    @EventHandler( "row1col3" )
    public void row1col3rMouseDown( MouseDownEvent e ) {
        e.preventDefault();
//        dnd.beginColumnResize( row1col3, e.getClientX() );
        consoleLog( "MouseDownEvent row1col3 " + e.getClientX() );
    }


    native void consoleLog( String message ) /*-{
        console.log("log:" + message);
    }-*/;

}
