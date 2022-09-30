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

package com.servoy.extensions.plugins.http;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.message.BasicHeaderValueParser;
import org.apache.hc.core5.http.message.ParserCursor;

import com.servoy.j2db.documentation.ServoyDocumented;
import com.servoy.j2db.scripting.IJavaScriptType;
import com.servoy.j2db.scripting.IScriptable;
import com.servoy.j2db.scripting.JSMap;
import com.servoy.j2db.util.Debug;
import com.servoy.j2db.util.Utils;

/**
 * @author pbakker
 *
 */
@ServoyDocumented
public class Response implements IScriptable, IJavaScriptType
{
	private SimpleHttpResponse res;
	private Object response_body = null;
	private HttpUriRequest request;
	private String exceptionMessage;

	public Response()
	{

	}

	public Response(String exceptionMessage)
	{
		this.exceptionMessage = exceptionMessage;
	}

	public Response(SimpleHttpResponse response, HttpUriRequest request)
	{
		this.res = response;
		this.request = request;
	}

	public String[] getAllowedMethods()
	{
		if (this.res == null)
		{
			Debug.error("getAllowedMethods API was called while response is null due to request exception: " + exceptionMessage);
			return new String[0];
		}
		Iterator<Header> it = res.headerIterator(OptionsRequest.OPTIONS_HEADER);
		Set<String> methods = new HashSet<String>();
		while (it.hasNext())
		{
			Header header = it.next();
			ParserCursor cursor = new ParserCursor(0, header.getValue().length());
			HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(header.getValue(), cursor);
			for (HeaderElement element : elements)
			{
				methods.add(element.getName());
			}
		}
		return methods.toArray(new String[0]);
	}

	/**
	 * Gets the status code of the response, the list of the possible values is in HTTP_STATUS constants.<br/><br/>
	 *
	 * In case there was an exception executing the request, please ignore/do not use this value (it will be 0).
	 * You can check that situation using response.getException().
	 *
	 * @sample
	 * var status = response.getStatusCode();// compare with HTTP_STATUS constants
	 */
	public int js_getStatusCode()
	{
		if (res != null)
		{
			return res.getCode();
		}
		else
		{
			Debug.error("response.getStatusCode API was called while response is null due to request exception: " + exceptionMessage);
		}
		return 0;
	}

	/**
	 * Gets the status code's reason phrase. For example if a response contains status code 403 (Forbidden) it might be useful to know why.
	 *
	 * For example a Jenkins API req. could answer with "403 No valid crumb was included in the request" which will let you know
	 * that you simply have to reques a crumb and then put that in the request headers as "Jenkins-Crumb". But you could not know that from 403 status alone...
	 *
	 * @sample
	 * var statusReasonPhrase = response.getStatusReasonPhrase();
	 */
	public String js_getStatusReasonPhrase()
	{
		if (res != null)
		{
			return res.getReasonPhrase();
		}
		else
		{
			Debug.error("response.getStatusReasonPhrase API was called while response is null due to request exception: " + exceptionMessage);
		}
		return null;
	}

	/**
	 * Get the content of the response as String.
	 *
	 * @sample
	 * var pageData = response.getResponseBody();
	 */
	public String js_getResponseBody()
	{
		if (response_body == null)
		{
			try
			{
				if (this.res != null)
				{
					response_body = res.getBodyText();
				}
				else
				{
					Debug.error("response.getResponseBody API was called while response is null due to request exception: " + exceptionMessage);
				}
			}
			catch (Exception e)
			{
				Debug.error("Error when getting response body for: " + (request != null ? request.getRequestUri() : "unknown request"), e); //$NON-NLS-1$
				response_body = "";
			}
		}
		return response_body instanceof String ? (String)response_body : "";

	}

	/**
	 * Get the content of response as binary data. It also supports gzip-ed content.
	 *
	 * @sample
	 * var mediaData = response.getMediaData();
	 */
	public byte[] js_getMediaData()
	{
		if (response_body == null)
		{
			if (this.res != null)
			{
				response_body = res.getBodyBytes();
			}
			else
			{
				Debug.error("response.getMediaData API was called while response is null due to request exception: " + exceptionMessage);
			}
		}
		return response_body instanceof byte[] ? (byte[])response_body : null;
	}

	/**
	 * Gets the headers of the response as name/value arrays.
	 *
	 * @sample
	 * var allHeaders = response.getResponseHeaders();
	 * var header;
	 *
	 * for (header in allHeaders) application.output(header + ': ' + allHeaders[header]);
	 */
	public JSMap js_getResponseHeaders()
	{
		return js_getResponseHeaders(null);
	}

	/**
	 * @clonedesc js_getResponseHeaders()
	 * @sample
	 * var contentLength = response.getResponseHeaders("Content-Length");
	 *
	 * @param headerName
	 */
	public JSMap js_getResponseHeaders(String headerName)
	{
		try
		{
			Header[] ha;
			JSMap sa = new JSMap();
			if (this.res != null)
			{
				if (headerName == null)
				{
					ha = res.getHeaders();
				}
				else
				{
					ha = res.getHeaders(headerName);
				}
				for (Header element : ha)
				{
					if (sa.containsKey(element.getName()))
					{
						sa.put(element.getName(), Utils.arrayAdd((String[])sa.get(element.getName()), element.getValue(), true));
					}
					else
					{
						sa.put(element.getName(), new String[] { element.getValue() });
					}
				}
			}
			else
			{
				Debug.error("response.getResponseHeaders API was called while response is null due to request exception: " + exceptionMessage);
			}
			return sa;
		}
		catch (Exception e)
		{
			Debug.error("Error when getting response headers for: " + (request != null ? request.getRequestUri() : "unknown request"), e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Get the charset of the response body.
	 *
	 * @sample
	 * var charset = response.getCharset();
	 */
	public String js_getCharset()
	{
		if (this.res != null)
		{
			ContentType contentType = res.getContentType();
			if (contentType != null)
			{
				return contentType.getCharset().displayName();
			}
		}
		else
		{
			Debug.error("response.getCharset API was called while response is null due to request exception: " + exceptionMessage);
		}
		return null;
	}

	/**
	 * Needs to be called when not reading content via getResponseBody or getMediaData
	 * to be able to reuse the client.
	 * @return true if the entity content is consumed and content stream (if exists) is closed
	 */
	public boolean js_close()
	{
		// no longer needed
		return true;
	}

	/**
	 * Getter for the exception message.
	 *
	 * @sample
	 * var exception = response.getException();
	 * @return the exception message
	 */
	public String js_getException()
	{
		return this.exceptionMessage;
	}
}
