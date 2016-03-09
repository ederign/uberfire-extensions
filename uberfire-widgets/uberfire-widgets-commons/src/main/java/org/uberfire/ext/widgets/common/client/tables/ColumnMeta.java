/*
 * Copyright 2015 JBoss, by Red Hat, Inc
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
package org.uberfire.ext.widgets.common.client.tables;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import org.uberfire.ext.widgets.table.client.ColumnMetaClientOnly;

public class ColumnMeta<T> extends ColumnMetaClientOnly {

    public ColumnMeta( Column column, String caption ) {
        super( column, caption );
    }

    public ColumnMeta( Column column, String caption, boolean visible ) {
        super( column, caption, visible );
    }

    public ColumnMeta( Column column, String caption, boolean visible, int position ) {
        super( column, caption, visible, position );
    }

    public ColumnMeta( Column column, String caption, boolean visible, boolean extraColumn ) {
        super( column, caption, visible, extraColumn );
    }
}
