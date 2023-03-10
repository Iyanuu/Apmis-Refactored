/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalLayout;

//@JavaScript("trans.js")
//@CssImport("./styles/shared-styssles.css")
@StyleSheet("https://stackpath.b/bootstrap/4.3.1/css/bootstrap.min.css")
public class MainScreenAbstract extends HorizontalLayout implements View {

public static final String NEW_ID = "afterend";

public MainScreenAbstract() {
	
	Page.getCurrent().getJavaScript().execute("alert('yeah!!!');");
}
}



