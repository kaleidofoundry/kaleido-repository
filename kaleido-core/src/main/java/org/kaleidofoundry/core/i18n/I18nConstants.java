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
package org.kaleidofoundry.core.i18n;

/**
 * @author Jerome RADUGET
 */
public interface I18nConstants {

   /** Enable or disable jpa entity manager resolution for resourceBundle */
   String ENABLE_JPA_PROPERTY = "kaleidofoundry.i18n.jpa.enabled";

   /** i18n message bundle interface plugin name */
   String I18nMessageBundlePluginName = "i18n";
   /** i18n message bundle default implementation plugin name */
   String I18nDefaultMessageBundlePluginName = "i18n.default";

}
