/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.layout.editor.client.novo.template.research.layout.screens;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Label;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnShutdown;
import org.uberfire.lifecycle.OnStartup;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;

@Dependent
@WorkbenchScreen( identifier = "HomeScreen2" )
public class HomeScreen2 {

    private static final String ORIGINAL_TEXT = "How do you feel2?";

    private final Label label = new Label( ORIGINAL_TEXT );

    @WorkbenchPartTitle
    public String getTitle() {
        return "homeScreen2";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return label;
    }


    @PostConstruct
    void postConstruct() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " bean created" );
    }

    @OnStartup
    void onStartup() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " starting" );
    }

    @OnOpen
    void onOpen() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " opening" );
    }

    @OnClose
    void onClose() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " closing" );
    }

    @OnShutdown
    void onShutdown() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " shutting down" );
    }

    @PreDestroy
    void preDestroy() {
        System.out.println( "HomeScreen@" + System.identityHashCode( this ) + " bean destroyed" );
    }
}