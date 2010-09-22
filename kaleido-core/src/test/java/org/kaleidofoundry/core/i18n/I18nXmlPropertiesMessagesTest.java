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

import org.junit.After;
import org.junit.Before;

/**
 * @author Jerome RADUGET
 */
public class I18nXmlPropertiesMessagesTest extends AbstractI18nMessagesTest {

   @Override
   String getResourceRoot() {
	return "org/kaleidofoundry/core/i18n/properties-xml/";
   }

   @Before
   public void setup() {
	I18nMessagesFactory.disableJpaControl();
   }

   @After
   @Override
   public void cleanup() {
	super.cleanup();
	I18nMessagesFactory.enableJpaControl();
   }

}
