/*
 * Copyright (C) 2014 MineStar.de 
 * 
 * This file is part of AdminStuff.
 * 
 * AdminStuff is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * AdminStuff is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AdminStuff.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.AdminStuff.webserver.pagehandler.main;

import com.sun.net.httpserver.HttpExchange;

import de.minestar.AdminStuff.webserver.pagehandler.AbstractHTMLHandler;
import de.minestar.AdminStuff.webserver.template.Template;
import de.minestar.AdminStuff.webserver.template.TemplateHandler;
import de.minestar.AdminStuff.webserver.template.TemplateReplacement;

public class InvalidLoginPageHandler extends AbstractHTMLHandler {

    private Template template;
    private TemplateReplacement rpl_navigation;

    public InvalidLoginPageHandler() {
        super(false);
        this.template = TemplateHandler.getTemplate("invalidLogin");
        this.rpl_navigation = new TemplateReplacement("NAVIGATION", TemplateHandler.getTemplate("tpl_navi_off").getString());
    }

    @Override
    public String handle(HttpExchange http) {
        return this.template.autoReplace(this.rpl_navigation);
    }
}