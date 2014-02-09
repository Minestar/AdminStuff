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

package de.minestar.AdminStuff.webserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import de.minestar.AdminStuff.webserver.pagehandler.AbstractHTMLHandler;
import de.minestar.AdminStuff.webserver.pagehandler.DoLoginPageHandler;
import de.minestar.AdminStuff.webserver.pagehandler.InvalidLoginPageHandler;
import de.minestar.AdminStuff.webserver.pagehandler.LoginPageHandler;
import de.minestar.AdminStuff.webserver.pagehandler.PageHandler;
import de.minestar.AdminStuff.webserver.pagehandler.SecurePageHandler;
import de.minestar.AdminStuff.webserver.units.AuthHandler;
import de.minestar.AdminStuff.webserver.units.HandlerList;
import de.minestar.AdminStuff.webserver.utils.ParameterFilter;

public class Webserver {

	private HttpServer server;

	public Webserver(int port) throws IOException {
		try {
			System.out.println("Starting webserver @ port: " + port);

			this.server = HttpServer.create(new InetSocketAddress(port), 0);

			// create mainHandler
			PageHandler pageHandler = new PageHandler();
			pageHandler.setDefaultPage("/login.html");
			server.createContext("/", pageHandler).getFilters()
					.add(new ParameterFilter());

			// create subHandler
			if (!AuthHandler.init()) {
				throw new Exception("AuthHandler not initialized!");
			}
			this.startUp();

			// createContext
			server.setExecutor(null);
			server.start();
			System.out.println("Webserver started @ port: " + port);
		} catch (Exception e) {
			System.out.println("ERROR: could not start server @ port: " + port);
			e.printStackTrace();
			this.server = null;
		}
	}

	private void startUp() {
		this.registerPage(new InvalidLoginPageHandler("/invalidLogin.html"),
				"/invalidLogin.html");
		this.registerPage(new LoginPageHandler("/login.html"), "/login.html");
		this.registerPage(new DoLoginPageHandler("/doLogin.html"),
				"/doLogin.html");
		this.registerPage(new SecurePageHandler("/securePage.html"),
				"/securePage.html");
	}

	public void registerPage(AbstractHTMLHandler handler, String path) {
		HandlerList.registerHandler(path, handler);
	}

	public HttpServer getServer() {
		return server;
	}

	public boolean isRunning() {
		return this.server != null;
	}

	public void close() {
		if (this.server != null) {
			this.server.stop(0);
		}
	}

	public static void main(String[] args) {
		try {
			new Webserver(8000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}