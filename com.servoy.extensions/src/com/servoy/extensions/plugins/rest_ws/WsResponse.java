/*
 This file belongs to the Servoy development and deployment environment, Copyright (C) 1997-2018 Servoy BV

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

package com.servoy.extensions.plugins.rest_ws;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

import com.servoy.j2db.documentation.ServoyDocumented;
import com.servoy.j2db.scripting.IJavaScriptType;
import com.servoy.j2db.scripting.IScriptable;

/**
 * The representation of a rest-ws request, only valid while running in a REST-WS request.
 *
 * @author rgansevles
 *
 */
@ServoyDocumented(scriptingName = "WsResponse")
public class WsResponse implements IScriptable, IJavaScriptType
{
	private final RestWSClientPlugin plugin;
	private int status;

	public WsResponse(RestWSClientPlugin plugin)
	{
		this.plugin = plugin;
	}

	/**
	* Adds the specified cookie to the response.  This method can be called
	* multiple times to set more than one cookie.
	*
	* @param cookie the Cookie to return to the client
	*
	*/
	@JSFunction
	public void addCookie(WsCookie cookie)
	{
		getResponse().addCookie(cookie.getCookie());
	}

	/**
	* Sets the status code for this response.
	*
	* <p>This method is used to set the return status code when there is
	* no error (for example, for the SC_OK or SC_MOVED_TEMPORARILY status
	* codes).
	*
	* <p>If this method is used to set an error code, then the container's
	* error page mechanism will not be triggered. If there is an error and
	* the caller wishes to invoke an error page defined in the web
	* application, then {@link #sendError} must be used instead.
	*
	* <p>This method preserves any cookies and other response headers.
	*
	* <p>Valid status codes are those in the 2XX, 3XX, 4XX, and 5XX ranges.
	* Other status codes are treated as container specific.
	*
	* @param	sc	the status code
	*
	* @see #sendError
	*/
	@JSGetter
	public int getStatus()
	{
		return this.status;
	}

	@JSSetter
	public void setStatus(int sc)
	{
		this.status = sc;
		getResponse().setStatus(sc);
	}

	@JSFunction
	public void sendError(int sc) throws IOException
	{
		getResponse().sendError(sc);
	}

	@JSFunction
	public void sendError(int sc, String msg) throws IOException
	{
		getResponse().sendError(sc, msg);
	}

	private HttpServletResponse getResponse()
	{
		return plugin.getResponse();
	}

	/**
	 * Sets the character encoding (MIME charset) of the response
	 * being sent to the client, for example, to UTF-8.
	 * If the character encoding has already been set by
	 * {@link #setContentType} or {@link #setLocale},
	 * this method overrides it.
	 * Calling {@link #setContentType} with the <code>String</code>
	 * of <code>text/html</code> and calling
	 * this method with the <code>String</code> of <code>UTF-8</code>
	 * is equivalent with calling
	 * <code>setContentType</code> with the <code>String</code> of
	 * <code>text/html; charset=UTF-8</code>.
	 * <p>This method can be called repeatedly to change the character
	 * encoding.
	 * This method has no effect if it is called after
	 * <code>getWriter</code> has been
	 * called or after the response has been committed.
	 * <p>Containers must communicate the character encoding used for
	 * the servlet response's writer to the client if the protocol
	 * provides a way for doing so. In the case of HTTP, the character
	 * encoding is communicated as part of the <code>Content-Type</code>
	 * header for text media types. Note that the character encoding
	 * cannot be communicated via HTTP headers if the servlet does not
	 * specify a content type; however, it is still used to encode text
	 * written via the servlet response's writer.
	 *
	 * @param charset a String specifying only the character set
	 * defined by IANA Character Sets
	 * (http://www.iana.org/assignments/character-sets)
	 *
	 * @see #setContentType
	 * @see #setLocale
	 *
	 */
	@JSGetter
	public String getCharacterEncoding()
	{
		return getResponse().getCharacterEncoding();
	}

	@JSSetter
	public void setCharacterEncoding(String charset)
	{
		getResponse().setCharacterEncoding(charset);
	}

	/**
	* Sets the content type of the response being sent to
	* the client, if the response has not been committed yet.
	* The given content type may include a character encoding
	* specification, for example, <code>text/html;charset=UTF-8</code>.
	* The response's character encoding is only set from the given
	* content type if this method is called before <code>getWriter</code>
	* is called.
	* <p>This method may be called repeatedly to change content type and
	* character encoding.
	* This method has no effect if called after the response
	* has been committed. It does not set the response's character
	* encoding if it is called after <code>getWriter</code>
	* has been called or after the response has been committed.
	* <p>Containers must communicate the content type and the character
	* encoding used for the servlet response's writer to the client if
	* the protocol provides a way for doing so. In the case of HTTP,
	* the <code>Content-Type</code> header is used.
	*
	* @param type a <code>String</code> specifying the MIME
	* type of the content
	*
	* @see #setLocale
	* @see #setCharacterEncoding
	* @see #getOutputStream
	* @see #getWriter
	*
	*/
	@JSGetter
	public String getContentType()
	{
		return getResponse().getContentType();
	}

	@JSSetter
	public void setContentType(String type)
	{
		getResponse().setContentType(type);
	}

	/**
	* Returns a boolean indicating whether the named response header
	* has already been set.
	*
	* @param	name	the header name
	* @return		<code>true</code> if the named response header
	*			has already been set;
	* 			<code>false</code> otherwise
	*/
	@JSFunction
	public boolean containsHeader(String name)
	{
		return getResponse().containsHeader(name);
	}

	/**
	*
	* Sets a response header with the given name and
	* date-value.  The date is specified in terms of
	* milliseconds since the epoch.  If the header had already
	* been set, the new value overwrites the previous one.  The
	* <code>containsHeader</code> method can be used to test for the
	* presence of a header before setting its value.
	*
	* @param	name	the name of the header to set
	* @param	date	the assigned date value
	*
	* @see #containsHeader
	* @see #addDateHeader
	*/
	@JSFunction
	public void setDateHeader(String name, long date)
	{
		getResponse().setDateHeader(name, date);
	}

	/**
	*
	* Adds a response header with the given name and
	* date-value.  The date is specified in terms of
	* milliseconds since the epoch.  This method allows response headers
	* to have multiple values.
	*
	* @param	name	the name of the header to set
	* @param	date	the additional date value
	*
	* @see #setDateHeader
	*/
	@JSFunction
	public void addDateHeader(String name, long date)
	{
		getResponse().addDateHeader(name, date);
	}

	/**
	*
	* Sets a response header with the given name and value.
	* If the header had already been set, the new value overwrites the
	* previous one.  The <code>containsHeader</code> method can be
	* used to test for the presence of a header before setting its
	* value.
	*
	* @param	name	the name of the header
	* @param	value	the header value  If it contains octet string,
	*		it should be encoded according to RFC 2047
	*		(http://www.ietf.org/rfc/rfc2047.txt)
	*
	* @see #containsHeader
	* @see #addHeader
	*/
	@JSFunction
	public void setHeader(String name, String value)
	{
		getResponse().setHeader(name, value);
	}

	/**
	 * Adds a response header with the given name and value.
	 * This method allows response headers to have multiple values.
	 *
	 * @param	name	the name of the header
	 * @param	value	the additional header value   If it contains
	 *		octet string, it should be encoded
	 *		according to RFC 2047
	 *		(http://www.ietf.org/rfc/rfc2047.txt)
	 *
	 * @see #setHeader
	 */
	@JSFunction
	public void addHeader(String name, String value)
	{
		getResponse().addHeader(name, value);
	}

	/**
	* Sets a response header with the given name and
	* integer value.  If the header had already been set, the new value
	* overwrites the previous one.  The <code>containsHeader</code>
	* method can be used to test for the presence of a header before
	* setting its value.
	*
	* @param	name	the name of the header
	* @param	value	the assigned integer value
	*
	* @see #containsHeader
	* @see #addIntHeader
	*/
	@JSFunction
	public void setIntHeader(String name, int value)
	{
		getResponse().setIntHeader(name, value);
	}

	/**
	* Adds a response header with the given name and
	* integer value.  This method allows response headers to have multiple
	* values.
	*
	* @param	name	the name of the header
	* @param	value	the assigned integer value
	*
	* @see #setIntHeader
	*/
	@JSFunction
	public void addIntHeader(String name, int value)
	{
		getResponse().addIntHeader(name, value);
	}
}
