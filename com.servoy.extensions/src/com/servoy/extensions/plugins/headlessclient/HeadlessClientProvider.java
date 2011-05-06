/*
 This file belongs to the Servoy development and deployment environment, Copyright (C) 1997-2010 Servoy BV

 This program is free software; you can redistribute it and/or modify it under
 the terms of the GNU Affero General Public License as published by the Free
 Software Foundation; either version 3 of the License, or (at your option) any
 later version.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License along
 with this program; if not, see http://www.gnu.org/licenses or write to the Free
 Software Foundation,Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 */
package com.servoy.extensions.plugins.headlessclient;

import com.servoy.j2db.plugins.IClientPluginAccess;
import com.servoy.j2db.scripting.IScriptObject;
import com.servoy.j2db.util.Debug;

public class HeadlessClientProvider implements IScriptObject
{
	private final HeadlessClientPlugin plugin;
	private IHeadlessServer headlessServer = null;

	HeadlessClientProvider(HeadlessClientPlugin plugin)
	{
		this.plugin = plugin;
	}

	private void createService()
	{
		if (headlessServer == null)
		{
			try
			{
				IClientPluginAccess access = plugin.getPluginAccess();
				headlessServer = (IHeadlessServer)access.getServerService(IHeadlessServer.SERVICE_NAME);
			}
			catch (Exception ex)
			{
				Debug.error(ex);
			}
		}
	}

	/**
	 * Creates a headless client on the server that will open the given solution
	 * 
	 * @param solutionname The solution to load
	 * @param username The user name that is used to login to the solution
	 * @param password The password for the user
	 * @param solutionOpenMethodArgs The arguments that will be passed to the solution open method.
	 * 
	 * @return the JSClient that is created.
	 */
	public JSClient js_createClient(String solutionname, String username, String password, Object[] solutionOpenMethodArgs)
	{
		//create if not yet created
		createService();

		try
		{
			String clientID = headlessServer.createClient(solutionname, username, password, solutionOpenMethodArgs);
			if (clientID != null)
			{
				return new JSClient(clientID, headlessServer, plugin);
			}
		}
		catch (Exception ex)
		{
			Debug.error(ex);
		}
		return null;
	}

	public JSClient js_getClient(String clientID)
	{
		//create if not yet created
		createService();

		try
		{
			if (headlessServer.isValid(clientID))
			{
				return new JSClient(clientID, headlessServer, plugin);
			}
		}
		catch (Exception ex)
		{
			Debug.error(ex);
		}
		return null;
	}

	@SuppressWarnings("nls")
	public String[] getParameterNames(String methodName)
	{
		if ("createClient".equals(methodName))
		{
			return new String[] { "solutionName", "username", "password", "solutionOpenMethodArgs" };
		}
		if ("getClient".equals(methodName))
		{
			return new String[] { "clientID" };
		}
		return null;
	}

	@SuppressWarnings("nls")
	public String getSample(String methodName)
	{
		StringBuilder sample = new StringBuilder();
		if ("createClient".equals(methodName))
		{
			sample.append("// " + getToolTip(methodName) + "\n");
			sample.append("var headlessClient = plugins.headlessclient.createClient(\"someSolution\", \"user\", \"pass\", null);\n");
			sample.append("if (headlessClient != null && headlessClient.isValid()) { \n");
			sample.append("\t var x = new Object();\n");
			sample.append("\t x.name = 'remote1';\n");
			sample.append("\t x.number = 10;\n");
			sample.append("headlessClient.queueMethod(null, \"remoteMethod\", [x], callback);\n");
			sample.append("}\n");
		}
		else if ("getClient".equals(methodName))
		{
			sample.append("// " + getToolTip(methodName) + "\n");
			sample.append("var headlessClient = plugins.headlessclient.getClient(\"clientID\");\n");
			sample.append("if (headlessClient != null && headlessClient.isValid()) {\n");
			sample.append("\t headlessClient.queueMethod(null, \"someRemoteMethod\", null, callback);\n");
			sample.append("}\n");
		}
		else
		{
			return null;
		}
		return sample.toString();
	}

	@SuppressWarnings("nls")
	public String getToolTip(String methodName)
	{
		if ("createClient".equals(methodName))
		{
			return "Creates a headless client that will open the given solution.";
		}
		if ("getClient".equals(methodName))
		{
			return "Gets an existing headless client for the given client uuid.";
		}
		return null;
	}

	public boolean isDeprecated(String methodName)
	{
		return false;
	}

	public Class< ? >[] getAllReturnedTypes()
	{
		return new Class[] { JSClient.class };
	}
}
