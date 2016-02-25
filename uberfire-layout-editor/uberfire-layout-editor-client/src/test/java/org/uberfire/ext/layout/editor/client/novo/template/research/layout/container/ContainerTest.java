package org.uberfire.ext.layout.editor.client.novo.template.research.layout.container;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ContainerTest {

    private Container container;

    @Before
    public void setup() {
        Container.View view = mock( Container.View.class );
        container = new Container( view );
    }

    @Test
    public void x() {
        assertNotNull( container );
    }

}