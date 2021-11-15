package com.adailsilva.controller;

/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named
public class GuestPreferences implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, String> themeColors;

	private String theme = "indigo";

	private String menuClass = "layout-menu-dark";

	private String profileMode = "inline";

	private String menuLayout = "static";

	private boolean compact = false;

	@PostConstruct
	public void init() {
		themeColors = new HashMap<String, String>();
		themeColors.put("indigo", "#3F51B5");

	}

	public String getMenuClass() {
		return this.menuClass;
	}

	public String getProfileMode() {
		return this.profileMode;
	}

	public String getTheme() {
		return theme;
	}

	public String getMenuLayout() {
		if (this.menuLayout.equals("static"))
			return "menu-layout-static";
		else if (this.menuLayout.equals("overlay"))
			return "menu-layout-overlay";
		else if (this.menuLayout.equals("horizontal"))
			return "menu-layout-static menu-layout-horizontal";
		else
			return "menu-layout-static";
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setLightMenu() {
		this.menuClass = null;
	}

	public void setDarkMenu() {
		this.menuClass = "layout-menu-dark";
	}

	public void setProfileMode(String profileMode) {
		this.profileMode = profileMode;
	}

	public void setMenuLayout(String menuLayout) {
		this.menuLayout = menuLayout;
	}

	@SuppressWarnings("rawtypes")
	public Map getThemeColors() {
		return this.themeColors;
	}

	public void setCompact(boolean value) {
		this.compact = value;
	}

	public boolean isCompact() {
		return this.compact;
	}
}
