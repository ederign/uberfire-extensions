package org.uberfire.ext.layout.editor.client.novo.template.research;

public class MouseOverInfo {

    private final int mouseX;
    private final int mouseY;

    public MouseOverInfo( int mouseX, int mouseY ) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public String toString() {
        return "MouseOverInfo{" +
                "mouseX=" + mouseX +
                ", mouseY=" + mouseY +
                '}';
    }
}
