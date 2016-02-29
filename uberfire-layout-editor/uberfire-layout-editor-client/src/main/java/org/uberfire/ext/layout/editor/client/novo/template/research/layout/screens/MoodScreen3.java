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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchScreen;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
@WorkbenchScreen( identifier = "MoodScreen3" )
public class MoodScreen3 extends Composite {

    @Inject
    @DataField
    private TextBox moodTextBox;

    @WorkbenchPartTitle
    public String getScreenTitle() {
        return "Change Mood3";
    }

    @EventHandler( "moodTextBox" )
    private void onKeyDown( KeyDownEvent event ) {
        if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ) {
            moodTextBox.setText( "" );
        }
    }

}
