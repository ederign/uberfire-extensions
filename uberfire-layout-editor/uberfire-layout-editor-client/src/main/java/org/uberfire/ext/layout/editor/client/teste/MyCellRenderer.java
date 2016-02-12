package org.uberfire.ext.layout.editor.client.teste;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.Window;

public class MyCellRenderer extends AbstractCell<YoDTO> {

    public interface Renderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String name);

        void onBrowserEvent(MyCellRenderer o, NativeEvent e, Element p, YoDTO n);
    }

    private final Renderer uiRenderer;

    public MyCellRenderer() {
        super( BrowserEvents.CLICK);
        this.uiRenderer = GWT.create( Renderer.class );
    }

    @Override
    public void onBrowserEvent( Context context, Element parent, YoDTO value, NativeEvent event,
                                ValueUpdater valueUpdater) {
        uiRenderer.onBrowserEvent(this, event, parent, value);
    }

    @Override
    public void render( Context context, YoDTO value, SafeHtmlBuilder safeHtmlBuilder) {
        String name = value.getName()+ " " + value.getDogName();

        uiRenderer.render(safeHtmlBuilder, name);
    }


    @UiHandler({"click"})
    void click(ClickEvent event, Element parent, YoDTO value) {
        //Maybe use the ActionCell.Delegate to process the action elsewhere...
        GWT.log("Click : " + value.getName());
    }
}