/*
 *  Copyright 2008-2011 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.kaleidofoundry.core.util;

import static org.kaleidofoundry.core.i18n.InternalBundleHelper.UtilMessageBundle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.kaleidofoundry.core.lang.annotation.Task;
import org.kaleidofoundry.core.lang.annotation.TaskLabel;
import org.kaleidofoundry.core.lang.annotation.ThreadSafe;

/**
 * Primitives types (java.lang type) toString serializer
 * 
 * @author Jerome RADUGET
 */
@ThreadSafe
public class PrimitiveTypeToStringSerializer implements ToStringSerializer {

   /** Multiple value separator */
   public static final String DefaultMultiValuesSeparator = " ";
   /** String Date Formatter */
   public static final String DefaultDateFormat = "yyyy-MM-dd'T'hh:mm:ss"; // yyyy-MM-ddThh:mm:ss
   /** String Number Formatter */
   public static final String DefaultNumberFormat = "##0.0####";

   /** Multiple value separator */
   protected final String MultiValuesSeparator;
   /** Date Formatter */
   protected final String DateFormat;
   /** Number Formatter */
   protected final String NumberFormat;

   /**
    * 
    */
   public PrimitiveTypeToStringSerializer() {
	this(DefaultMultiValuesSeparator, DefaultDateFormat, DefaultNumberFormat);
   }

   /**
    * @param multiValuesSeparator
    * @param dateFormat
    * @param numberFormat
    */
   public PrimitiveTypeToStringSerializer(final String multiValuesSeparator, final String dateFormat, final String numberFormat) {
	MultiValuesSeparator = (multiValuesSeparator != null ? multiValuesSeparator : DefaultMultiValuesSeparator);
	DateFormat = dateFormat != null ? dateFormat : DefaultDateFormat;
	NumberFormat = numberFormat != null ? numberFormat : DefaultNumberFormat;
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.util.ToStringSerializer#deserialize(java.lang.String, java.lang.Class)
    */
   @Override
   @SuppressWarnings("unchecked")
   @Task(comment = "use SimpleDateFormat has a thread local", labels = TaskLabel.Enhancement)
   public <T extends Serializable> T deserialize(final String value, final Class<T> type) throws IllegalStateException {

	if (value == null) { return null; }

	if (Boolean.class.isAssignableFrom(type)) { return (T) Boolean.valueOf(value); }

	if (Number.class.isAssignableFrom(type)) {
	   try {
		if (Byte.class == type) { return (T) Byte.valueOf(value); }
		if (Short.class == type) { return (T) Short.valueOf(value); }
		if (Integer.class == type) { return (T) Integer.valueOf(value); }
		if (Long.class == type) { return (T) Long.valueOf(value); }
		if (Float.class == type) { return (T) Float.valueOf(value); }
		if (Double.class == type) { return (T) Double.valueOf(value); }
		if (BigInteger.class == type) { return (T) new BigInteger(value); }
		if (BigDecimal.class == type) { return (T) new BigDecimal(value); }
	   } catch (final NumberFormatException nfe) {
		throw new IllegalStateException(UtilMessageBundle.getMessage("serializer.number.format.error", value), nfe);
	   }
	}

	if (Date.class.isAssignableFrom(type)) {
	   try {
		return (T) new SimpleDateFormat(DateFormat).parse(value);
	   } catch (final ParseException pe) {
		throw new IllegalStateException(UtilMessageBundle.getMessage("serializer.date.format.error", value, DateFormat), pe);
	   }
	}

	if (String.class.isAssignableFrom(type)) { return (T) (StringHelper.isEmpty(value) ? "" : value); }

	throw new IllegalStateException(UtilMessageBundle.getMessage("serializer.illegal.class", type.getName()));

   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.util.ToStringSerializer#deserializeToList(java.lang.String, java.lang.Class)
    */
   @Override
   public <T extends Serializable> List<T> deserializeToList(final String values, final Class<T> type) {

	if (values == null) { return null; }

	List<T> result = null;

	if (!StringHelper.isEmpty(values)) {
	   result = new LinkedList<T>();
	   final StringTokenizer strToken = new StringTokenizer(values, MultiValuesSeparator);
	   while (strToken.hasMoreTokens()) {
		result.add(deserialize(strToken.nextToken(), type));
	   }
	}

	return result;
   }

   /*
    * (non-Javadoc)
    * @see org.kaleidofoundry.core.util.ToStringSerializer#serialize(T, java.lang.Class)
    */
   @Override
   @Task(comment = "use SimpleDateFormat has a thread local", labels = TaskLabel.Enhancement)
   public <T extends Serializable> String serialize(final T value, final Class<T> type) {

	if (value == null) { return null; }

	if (Boolean.class.isAssignableFrom(type)) { return value.toString(); }

	if (Number.class.isAssignableFrom(type)) { return value.toString(); }

	if (Date.class.isAssignableFrom(type)) { return new SimpleDateFormat(DateFormat).format((Date) value); }

	if (String.class.isAssignableFrom(type)) { return (String) value; }

	throw new IllegalStateException(UtilMessageBundle.getMessage("serializer.illegal.class", type.getName()));

   }

   /**
    * @param <T>
    * @param value
    * @param type
    * @return if the value parameter is a String, this method will return {@link #deserialize(String, Class)}, otherwise if class of the
    *         value parameter is already of type T it will return it, otherwise if will throw a {@link IllegalArgumentException}
    * @throws IllegalArgumentException
    */
   @SuppressWarnings("unchecked")
   protected <T extends Serializable> T _deserialize(final Serializable value, final Class<T> type) {

	if (value == null) { return null; }

	if (value instanceof String) {
	   return deserialize((String) value, type);
	} else if (value.getClass().isAssignableFrom(type)) {
	   return (T) value;
	} else {
	   throw new IllegalArgumentException(UtilMessageBundle.getMessage("serializer.illegal.argument", value.getClass().getName(), type.getName()));
	}

   }

   /**
    * @param <T>
    * @param values
    * @param type
    * @return if the values parameter is a String, this method will return {@link #deserializeToList(String, Class)}, otherwise if class of
    *         the values parameter is already of type List<T> it will return it, otherwise if will throw a {@link IllegalArgumentException}
    * @throws IllegalArgumentException
    */
   @SuppressWarnings("unchecked")
   protected <T extends Serializable> List<T> _deserializeToList(final Serializable values, final Class<T> type) {

	if (values == null) { return null; }

	if (values instanceof String) {
	   return deserializeToList((String) values, type);
	} else if (values.getClass().isAssignableFrom(List.class)) {
	   List<? extends Serializable> current = (List<? extends Serializable>) values;
	   List<T> result = new ArrayList<T>();
	   for (Serializable item : current) {
		result.add(_deserialize(item, type));
	   }
	   return result;
	} else {
	   throw new IllegalArgumentException(UtilMessageBundle.getMessage("serializer.illegal.argument", values.getClass().getName(), type.getName()));
	}
   }
}