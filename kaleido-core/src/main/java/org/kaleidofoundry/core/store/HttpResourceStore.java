/*  
 * Copyright 2008-2010 the original author or authors 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaleidofoundry.core.store;

import static org.kaleidofoundry.core.store.ResourceStoreConstants.HttpStorePluginName;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.i18n.InternalBundleHelper;
import org.kaleidofoundry.core.lang.annotation.NotImplemented;
import org.kaleidofoundry.core.lang.annotation.NotNull;
import org.kaleidofoundry.core.plugin.Declare;
import org.kaleidofoundry.core.util.StringHelper;

/**
 * Simple http & https resource store implementation<br/>
 * This implementation is only for read only use<br/>
 * You can extends it and override {@link #doRemove(URI)} and {@link #doStore(URI, ResourceHandler)} to your need, and {@link Declare} your
 * implementation to use it
 * 
 * @author Jerome RADUGET
 */
@Declare(HttpStorePluginName)
public class HttpResourceStore extends AbstractResourceStore implements ResourceStore {

   /**
    * enumeration of local context property name
    */
   public static enum ContextProperty {
	/** GET, POST method */
	method,
	/** mime type */
	contentType;
   }

   // **** instance datas **********************************************************************************************

   /**
    * @param context
    */
   public HttpResourceStore(@NotNull final RuntimeContext<ResourceStore> context) {
	super(context);
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.store.AbstractResourceStore#getStoreType()
    */
   @Override
   public ResourceStoreType[] getStoreType() {
	return new ResourceStoreType[] { ResourceStoreTypeEnum.http, ResourceStoreTypeEnum.https };
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.store.AbstractResourceStore#doLoad(java.net.URI)
    */
   @Override
   protected ResourceHandler doGet(final URI resourceUri) throws ResourceException {
	if (resourceUri.getHost() == null) { throw new IllegalStateException(InternalBundleHelper.ResourceStoreMessageBundle.getMessage(
		"store.resource.uri.http.illegal", resourceUri.toString())); }
	try {

	   /*
	    * # java env. variable to defined proxy globally
	    * # http://download.oracle.com/docs/cd/E17409_01/javase/6/docs/technotes/guides/net/properties.html
	    * http.proxyHost (default: <none>)
	    * http.proxyPort (default: 80 if http.proxyHost specified)
	    * http.nonProxyHosts (default: <none>)
	    */

	   final URL configUrl = resourceUri.toURL();
	   final URLConnection urlConnection;
	   Proxy httpProxy = null;

	   // if a proxy is set & active
	   if (!StringHelper.isEmpty(context.getProperty(ResourceContextBuilder.proxySet))) {
		if (Boolean.parseBoolean(context.getProperty(ResourceContextBuilder.proxySet))) {

		   final String proxyHost = context.getProperty(ResourceContextBuilder.proxyHost);
		   final String proxyPort = context.getProperty(ResourceContextBuilder.proxyPort);

		   if (!StringHelper.isEmpty(proxyHost)) {
			httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, !StringHelper.isEmpty(proxyPort) ? Integer.parseInt(proxyPort) : 80));

			if (!StringHelper.isEmpty(context.getProperty(ResourceContextBuilder.nonProxyHosts))) {
			   // :( global...
			   System.getProperties().put("http.nonProxyHosts", context.getProperty(ResourceContextBuilder.nonProxyHosts));
			}

			if (!StringHelper.isEmpty(context.getProperty(ResourceContextBuilder.proxyUser))
				&& !StringHelper.isEmpty(context.getProperty(ResourceContextBuilder.proxyPassword))) {

			   // Authenticator is global... :(
			   // other way : urlConnection.setRequestProperty("Proxy-Authorization", Base64.encodeObject(username));
			   // http://en.wikipedia.org/wiki/Base64
			   Authenticator.setDefault(new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
				   return new PasswordAuthentication(context.getProperty(ResourceContextBuilder.proxyUser), context.getProperty(
					   ResourceContextBuilder.proxyPassword).toCharArray());
				}
			   });

			}
		   }
		}
	   }

	   if (httpProxy == null) {
		// open connection with proxy settings
		urlConnection = configUrl.openConnection();
	   } else {
		// open connection with default proxy settings
		urlConnection = configUrl.openConnection(httpProxy);
	   }

	   // set commons connection settings
	   setUrlConnectionSettings(urlConnection);
	   // connection
	   urlConnection.connect();

	   try {
		return new ResourceHandlerBean(urlConnection.getInputStream());
	   } catch (final FileNotFoundException fnfe) {
		throw new ResourceNotFoundException(resourceUri.toString());
	   }

	} catch (final MalformedURLException mure) {
	   throw new IllegalStateException(InternalBundleHelper.ResourceStoreMessageBundle.getMessage("store.resource.uri.malformed", resourceUri.toString()));
	} catch (final IOException ioe) {
	   throw new ResourceException(ioe);
	}
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.store.AbstractResourceStore#doRemove(java.net.URI)
    */
   @Override
   @NotImplemented("remove method is not implemented in HttpResourceStore. Please consult java api doc")
   protected void doRemove(final URI resourceBinding) throws ResourceException {
	return; // !! exception will be throws due to @NotImplemented !!
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.store.AbstractResourceStore#doStore(java.net.URI, java.io.InputStream)
    */
   @Override
   @NotImplemented("store method is not implemented in HttpResourceStore. Please consult java api doc")
   protected void doStore(final URI resourceBinding, final ResourceHandler resource) throws ResourceException {
	// http://java.sun.com/docs/books/tutorial/networking/urls/readingWriting.html
	// or http://www.javaworld.com/javaworld/javatips/javatip42/jw-Example.java.html

	// OutputStream out = null;
	// try {
	// final URL configUrl = resourceBinding.toURL();
	// final URLConnection urlConnection = configUrl.openConnection();
	// urlConnection.connect();
	// out = urlConnection.getOutputStream();
	// int b;
	// while ((b = resource.getInputStream().read()) != -1) {
	// out.write(b);
	// }
	// } catch (MalformedURLException mure) {
	// throw new IllegalStateException(resourceBinding.toString() + " is not an http:// or https:// uri");
	// } catch (IOException ioe) {
	// throw new StoreException(ioe);
	// } finally {
	// if (out != null) {
	// try {
	// out.close();
	// } catch (IOException closeioe) {
	// throw new StoreException(closeioe);
	// }
	// }
	// }

	return; // !! exception will be throws due to @NotImplemented !!
   }

}