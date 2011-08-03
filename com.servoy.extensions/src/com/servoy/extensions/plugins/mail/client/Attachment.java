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
package com.servoy.extensions.plugins.mail.client;

import java.io.Serializable;

import com.servoy.j2db.scripting.IScriptObject;
import com.servoy.j2db.util.ImageLoader;

/**
 * @author jblok
 */
@SuppressWarnings("nls")
public class Attachment implements Serializable, IScriptObject
{
	private final String name;
	private String mimeType;
	private final byte[] data;
	private boolean embedded;

	public Attachment()
	{
		//for developer script introspection only
		this(null, null, null);
	}

	public Attachment(String name, byte[] data, String mimeType)
	{
		this.name = name;
		this.mimeType = mimeType;
		this.data = data;
		this.embedded = false;

		if (mimeType == null && data != null && data.length != 0)
		{
			this.mimeType = ImageLoader.getContentType(data, name);
		}
	}

	public Attachment(String name, byte[] data)
	{
		this(name, data, null);
	}

	/**
	 * Returns a byte array with the content of this attachment.
	 *
	 * @sample
	 * var logo = plugins.mail.createBinaryAttachment('logo.jpg', plugins.file.readFile('d:/logo.jpg'));
	 * var invoice = plugins.mail.createTextAttachment('invoice.txt', plugins.file.readTXTFile('d:/invoice.txt'));
	 * var attachments = new Array(logo, invoice);
	 * var success = plugins.mail.sendMail(toAddress, fromAddress, 'subject line', 'message text', null, null, attachments, properties);
	 * if (!success)
	 * {
	 * 	plugins.dialogs.showWarningDialog('Alert', 'Failed to send mail', 'OK');
	 * }
	 * else
	 * {
	 * 	plugins.dialogs.showInfoDialog('Success', 'Mail sent', 'OK');
	 * 	application.output('logo attachment name: ' + logo.getName());
	 * 	application.output('logo attachment mime type: ' + logo.getMimeType());
	 * 	application.output('logo attachment size: ' + logo.getData().length);
	 * 	application.output('logo attachment embedded state: ' + logo.isEmbedded());
	 * 	application.output('invoice attachment name: ' + invoice.getName());
	 * 	application.output('invoice attachment mime type: ' + invoice.getMimeType());
	 * 	application.output('invoice attachment size: ' + invoice.getData().length);
	 * 	application.output('invoice attachment embedded state: ' + invoice.isEmbedded());
	 * }
	 */
	public byte[] js_getData()
	{
		return getData();
	}

	/**
	 * Returns the name of this attachment.
	 *
	 * @sampleas js_getData()
	 */
	public String js_getName()
	{
		return getName();
	}

	/**
	 * Returns the Mime type of this attachment.
	 *
	 * @sampleas js_getData()
	 */
	public String js_getMimeType()
	{
		return getMimeType();
	}

	/**
	 * Returns true if this attachment is embedded, false otherwise. Attachments become embedded 
	 * if they are references through tags from the body text of the message.
	 *
	 * @sampleas js_getData()
	 */
	public boolean js_isEmbedded()
	{
		return isEmbedded();
	}

	public byte[] getData()
	{
		return data;
	}

	public String getName()
	{
		return name;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setEmbedded(boolean embedded)
	{
		this.embedded = embedded;
	}

	public boolean isEmbedded()
	{
		return embedded;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("name: "); //$NON-NLS-1$
		sb.append(getName());
		sb.append("\n"); //$NON-NLS-1$
		sb.append("mime type: "); //$NON-NLS-1$
		sb.append(getMimeType());
		sb.append("\n"); //$NON-NLS-1$
		sb.append("datalength: "); //$NON-NLS-1$
		sb.append((getData() != null ? getData().length : 0));
		sb.append("\n"); //$NON-NLS-1$
		sb.append("embedded: ");
		sb.append(embedded);
		sb.append("\n");
		return sb.toString();
	}

	public String[] getParameterNames(String methodName)
	{
		return null;
	}

	public String getSample(String methodName)
	{
		return null;
	}

	public String getToolTip(String methodName)
	{
		return null;
	}

	public boolean isDeprecated(String methodName)
	{
		return false;
	}

	public Class< ? >[] getAllReturnedTypes()
	{
		return null;
	}
}