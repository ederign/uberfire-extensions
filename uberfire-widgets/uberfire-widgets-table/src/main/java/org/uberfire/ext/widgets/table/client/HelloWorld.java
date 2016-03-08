package org.uberfire.ext.widgets.table.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

public class HelloWorld extends Composite{


    public HelloWorld(){
        GWT.log( "HELLO" );
    }
}
