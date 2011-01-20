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
package org.kaleidofoundry.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaleidofoundry.core.lang.NotNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jerome RADUGET
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
public class SpringAopContextTest extends Assert {

   @Autowired
   private MySpringBean mySpringBean; 

   @Test
   public void notNullAnnotationOnParameter() {
	assertNotNull(mySpringBean);
	assertEquals("foo", mySpringBean.myMethodToTestNotNullAnnotation("foo", null));
	try {
	   mySpringBean.myMethodToTestNotNullAnnotation(null, "foo");
	   fail("NotNullException expected");
	} catch (NotNullException nne) {
	}
   }

   @Test
   public void storeInjection() {
	assertNotNull(mySpringBean);
	assertNotNull(mySpringBean.getStore());
   }

   @Test
   public void cacheInjection() {
	assertNotNull(mySpringBean);
	assertNotNull(mySpringBean.getCache());
   }

   @Test
   public void cacheManagerInjection() {
	assertNotNull(mySpringBean);
	assertNotNull(mySpringBean.getCacheManager());
   }

   @Test
   public void storeConfiguration() {
	assertNotNull(mySpringBean);
	assertNotNull(mySpringBean.getConfiguration());
   }

   @Test
   public void i18nInjection() {
	assertNotNull(mySpringBean);
	assertNotNull(mySpringBean.getMessages());
   }

}
