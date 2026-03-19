/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package xar.internal.rest;

import jakarta.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.inject.Named;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import org.xwiki.context.Execution;
import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.rest.XWikiResource;

import java.net.URI;
import xar.RedirectAction;
import xar.internal.logging.*;
import org.slf4j.Logger;
import org.xwiki.rest.XWikiRestComponent;

@Component
@Named("xar.internal.rest.RedirectActionImpl")
@Singleton
public class RedirectActionImpl implements RedirectAction, XWikiRestComponent {
	@Inject
	private Execution execution;

	@Inject
	private Logger log;

	@Inject
	@Named("current")
	private DocumentReferenceResolver<String> resolver;

	@Context
	private HttpServletRequest httpServletRequest;

	@Context
	private UriInfo uri;

	@Override
	@GET
	@Path("/redirect")
	public Response redirect(@QueryParam("page") String page) {

		if (page == null || page.isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Missing Required Query Parameter: page")
					.build();
		}

		//log.info("###PROCESSING REDIRECT###");
		//log.debug("Parameter Passing Info\n 'Page': {}", page);
		LogHelper.debug(log, "Parameter Passing Info",
				"Page: " + page,
				"End of Parms");
		try {
			DocumentReference docRef = resolver.resolve(page.trim());
			XWikiContext xwikiContext = getXWikiContext();
			
			XWiki xwiki = xwikiContext.getWiki();
			String relativeUrl = xwiki.getURL(
					docRef,
					"view", // XWiki action
					null, // query string (extra params) — null for none
					null, // anchor fragment — null for none
					xwikiContext);

			URI baseUri = uri.getBaseUri();
			//log.debug("base uri: {}", baseUri);
			LogHelper.debug(log,"uri info",
					"base uri: {}"+ baseUri,
					"relative url: " + relativeUrl);
			String baseUrl = baseUri.getScheme()
					+ "://"
					+ baseUri.getHost()
					+ (baseUri.getPort() != -1 ? ":" + baseUri.getPort() : "");

			log.debug("base urL: {},relative url: {}", baseUrl,relativeUrl);
			URI redirectUri = URI.create(relativeUrl);
			LogHelper.debug(log,"Redirect",
					"Redirecting to: " + redirectUri);

			// ── 5. Return 302 redirect ─────────────────────────────────────────
			return Response.temporaryRedirect(redirectUri).build();

		} catch (Exception e) {

			log.error("catastrophic error beyond our wildest dreamzzz");
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Failed to resolve page: " + e.getMessage())
					.build();
		}
	}

	private XWikiContext getXWikiContext() {
		return (XWikiContext) execution
				.getContext()
				.getProperty(XWikiContext.EXECUTIONCONTEXT_KEY);
	}

}
