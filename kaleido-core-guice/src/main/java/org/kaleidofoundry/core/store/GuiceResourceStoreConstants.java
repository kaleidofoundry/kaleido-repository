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

/**
 * @author Jerome RADUGET
 */
public interface GuiceResourceStoreConstants {

   /** interface store declare plugin name */
   String ResourceStorePluginName = "guice-resourceStore";

   /** classpath implementation store plugin name */
   String ClasspathStorePluginName = "guice-classpathStore";
   /** file system implementation store plugin name */
   String FileSystemStorePluginName = "guice-fileStore";
   /** http implementation store plugin name */
   String HttpStorePluginName = "guice-httpStore";
   /** ftp implementation store plugin name */
   String FtpStorePluginName = "guice-ftpStore";
   /** webapp implementation store plugin name */
   String WebappStorePluginName = "guice-webappStore";
   /** jdbc implementation clob store plugin name */
   String ClobJdbcStorePluginName = "guice-jdbcStore";
   /** jpa implementation clob store plugin name */
   String ClobJpaStorePluginName = "guice-jpaStore";
   /** mocked in memory implementation plugin name */
   String MemoryStorePluginName = "guice-memoryStore";

}
