/*
 This file belongs to the Servoy development and deployment environment, Copyright (C) 1997-2011 Servoy BV

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

package com.servoy.extensions.plugins.workflows;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.mozilla.javascript.annotations.JSFunction;

import com.servoy.j2db.documentation.ServoyDocumented;
import com.servoy.j2db.scripting.IReturnedTypesProvider;
import com.servoy.j2db.scripting.IScriptable;

@ServoyDocumented(publicName = WorkflowsPlugin.PLUGIN_NAME, scriptingName = "plugins." + WorkflowsPlugin.PLUGIN_NAME)
public class WorkflowsProvider implements IScriptable, IReturnedTypesProvider
{
	private final WorkflowsPlugin plugin;

	public WorkflowsProvider(WorkflowsPlugin workflowPlugin)
	{
		plugin = workflowPlugin;
	}

	@JSFunction
	public KieServicesClient createClient(String deploymentUrl, String user, String password)
	{
		KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(
			deploymentUrl, user, password);
		config.setMarshallingFormat(MarshallingFormat.JAXB);
		KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
		return client;
	}

	public Class< ? >[] getAllReturnedTypes()
	{
		return new Class[] { KieServicesClient.class };
	}
}

