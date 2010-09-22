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
package org.kaleidofoundry.core.context;

/**
 * Static parameter that can be join to a {@link InjectContext}, in order to inject it to {@link RuntimeContext} <br/>
 * <br/>
 * It represents a static parameter, that could no be changed after {@link RuntimeContext} injection. <br/>
 * <br/>
 * By this way underling instance using {@link RuntimeContext} will be able to use this static parameter
 * 
 * @author Jerome RADUGET
 */
public @interface Parameter {

   /** context parameter name */
   String name();

   /** context parameter value */
   String value();

   /** context parameter class */
   Class<?> type() default String.class;
}
