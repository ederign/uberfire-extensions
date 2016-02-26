package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.client.mvp.UberView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith( GwtMockitoTestRunner.class )
public class ContainerViewTest {

    ContainerView containerView;
    Container presenter;
    private FlowPanel layoutMock;

    @Before
    public void setup() {
        containerView = new ContainerView();
        layoutMock = GWT.create( FlowPanel.class );
        containerView.layout = layoutMock;
        presenter = mock( Container.class );
        containerView.init( presenter );
    }

    @Test
    public void mobileSizeTest() {
        //TODO
    }

    @Test
    public void tabletSizeTest() {
        //TODO
    }

    @Test
    public void desktopSizeTest() {
        //TODO
    }

    @Test
    public void addRowTest() {
        containerView.addRow( mock( UberView.class ) );
        verify( layoutMock ).add( any() );
    }

    @Test
    public void clearTest() {
        containerView.clear();
        verify( layoutMock ).clear();
    }

    @Test
    public void addEmptyRowTest() {
        containerView.addEmptyRow( mock( UberView.class ) );
        verify( layoutMock ).add( any() );
    }

}