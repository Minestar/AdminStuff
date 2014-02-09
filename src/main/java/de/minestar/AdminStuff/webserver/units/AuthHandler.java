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

package de.minestar.AdminStuff.webserver.units;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

import de.minestar.AdminStuff.webserver.utils.SHA;

public class AuthHandler {
	private static HashMap<String, UserData> loggedInUsers;
	private static HashMap<String, AdminData> adminData;

	public static boolean init() {
		loggedInUsers = new HashMap<String, UserData>();
		adminData = new HashMap<String, AdminData>();
		try {
			String hashed = SHA.getHash("test".getBytes());
			addAdmin("gemo", hashed, UUID.randomUUID().toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return true;
	}

	private static void addAdmin(String userName, String hashedPassword,
			String passwordSalt) {
		try {
			AdminData user = new AdminData(userName, hashedPassword,
					passwordSalt);
			adminData.put(userName, user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static boolean doLogin(String userName, String clearPassword) {
		try {
			AdminData user = adminData.get(userName);
			if (user == null) {
				return false;
			}

			// get hashed password
			String hashedPassword = SHA.getHash(clearPassword.getBytes());
			if (user.isPasswordCorrect(hashedPassword)) {
				loginUser(userName);
				return true;
			}
			return false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static AdminData getAdmin(String userName) {
		return adminData.get(userName);
	}

	public static boolean hasAdmin(String userName) {
		return adminData.containsKey(userName);
	}

	public static boolean hasUser(String name) {
		return loggedInUsers.containsKey(name);
	}

	public static UserData getUser(String name) {
		return loggedInUsers.get(name);
	}

	public static boolean logoutUser(String name) {
		if (!hasUser(name)) {
			return false;
		} else {
			getUser(name).invalidateToken();
			return (loggedInUsers.remove(name) != null);
		}
	}

	public static void loginUser(String name) {
		if (!hasUser(name)) {
			loggedInUsers.put(name, new UserData(name));
		} else {
			getUser(name).updateToken();
		}
	}

	public static boolean isUserLoginValid(String name, String token) {
		if (!hasUser(name)) {
			return false;
		}
		return getUser(name).isValid(token);
	}

	public static void refreshUserToken(String name) {
		if (!hasUser(name)) {
			return;
		}
		getUser(name).refreshToken();
	}

}