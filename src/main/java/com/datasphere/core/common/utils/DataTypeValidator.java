/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.core.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataTypeValidator {

	public static List<String> BOOLEAN_TAG_ARRAY = Arrays.asList("是", "否", "f", "t", "true", "false", "真", "假", "yes", "no");
	public static List<Boolean> BOOLEAN_TAG_VALUE_ARRAY = Arrays.asList(true, false, false, true, true, false, true, false, true, false);
	
	/**
	 * 判断一个字符串是否整型
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if(str == null) return true;
		if(isBlank(str)) return false;
		try {
			Long.valueOf(str);
			return true;
		} catch(Throwable t) {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否数值型
	 * @param str
	 * @return
	 */
	public static boolean isDecimal(String str) {
		if(str == null) return true;
		if(isBlank(str)) return false;
		try {
			new BigDecimal(str);
			return true;
		} catch(Throwable t) {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否布尔型
	 * @param str
	 * @return
	 */
	public static boolean isBoolean(String str) {
		if(str == null) return true;
		if(isBlank(str)) return false;
		str = str.toLowerCase();
		return BOOLEAN_TAG_ARRAY.contains(str);
	}
	
	/**
	 * 判断一个字符串是否日期时间类型
	 * @param str
	 * @return
	 */
	public static boolean isDateTime(String str) {
		if(str == null) return true;
		if(isBlank(str)) return false;
		try {
			InternalDateUtils.convert(str);
			return true;
		} catch(Throwable t) {
			return false;
		}
	}
	
	/**
	 * 把一个字符串转换成整型
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static Long toInteger(String str) {
		if(str == null) return null;
		assertIsNotBlank(str);
		return Long.valueOf(str);
	}
	
	/**
	 * 把一个字符串转换成数值型
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static BigDecimal toDecimal(String str) {
		if(str == null) return null;
		assertIsNotBlank(str);
		return new BigDecimal(str);
	}
	
	/**
	 * 把一个字符串转换成布尔型
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static Boolean toBoolean(String str) {
		if(str == null) return null;
		assertIsTrue(isBoolean(str), "The string is not boolean!");
		str = str.toLowerCase();
		return BOOLEAN_TAG_VALUE_ARRAY.get(BOOLEAN_TAG_ARRAY.indexOf(str));
	}
	
	/**
	 * 把一个字符串转换成日期时间型
	 * @param str
	 * @return
	 */
	public static Date toDateTime(String str) {
		if(str == null) return null;
		assertIsNotBlank(str);
		return InternalDateUtils.convert(str);
	}
	
	protected static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	protected static void assertIsTrue(boolean b) {
		assertIsTrue(b, "The expression is not true!");
	}
	
	protected static void assertIsTrue(boolean b, String message) {
		if(!b) throw new RuntimeException(message);
	}
	
	protected static void assertIsNotBlank(String str) {
		assertIsTrue(!isBlank(str), "The string is blank!");
	}
	
	protected static String replaceEach(String text, String[] searchList, String[] replacementList) {

		if (text == null || text.length() == 0 || searchList == null || searchList.length == 0
				|| replacementList == null || replacementList.length == 0) {
			return text;
		}

		int searchLength = searchList.length;
		int replacementLength = replacementList.length;

		if (searchLength != replacementLength) {
			throw new IllegalArgumentException(
					"Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
		}

		boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

		int textIndex = -1;
		int replaceIndex = -1;
		int tempIndex = -1;

		for (int i = 0; i < searchLength; i++) {
			if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
					|| replacementList[i] == null) {
				continue;
			}
			tempIndex = text.indexOf(searchList[i]);

			if (tempIndex == -1) {
				noMoreMatchesForReplIndex[i] = true;
			} else {
				if (textIndex == -1 || tempIndex < textIndex) {
					textIndex = tempIndex;
					replaceIndex = i;
				}
			}
		}
		
		if (textIndex == -1) {
			return text;
		}

		int start = 0;

		int increase = 0;

		for (int i = 0; i < searchList.length; i++) {
			if (searchList[i] == null || replacementList[i] == null) {
				continue;
			}
			int greater = replacementList[i].length() - searchList[i].length();
			if (greater > 0) {
				increase += 3 * greater; // assume 3 matches
			}
		}
		increase = Math.min(increase, text.length() / 5);

		StringBuffer buf = new StringBuffer(text.length() + increase);

		while (textIndex != -1) {

			for (int i = start; i < textIndex; i++) {
				buf.append(text.charAt(i));
			}
			buf.append(replacementList[replaceIndex]);

			start = textIndex + searchList[replaceIndex].length();

			textIndex = -1;
			replaceIndex = -1;
			tempIndex = -1;
			for (int i = 0; i < searchLength; i++) {
				if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
						|| replacementList[i] == null) {
					continue;
				}
				tempIndex = text.indexOf(searchList[i], start);

				if (tempIndex == -1) {
					noMoreMatchesForReplIndex[i] = true;
				} else {
					if (textIndex == -1 || tempIndex < textIndex) {
						textIndex = tempIndex;
						replaceIndex = i;
					}
				}
			}
		}
		int textLength = text.length();
		for (int i = start; i < textLength; i++) {
			buf.append(text.charAt(i));
		}
		return buf.toString();
	}
	
	public static class InternalDateUtils {
		public static SimpleDateFormat SDF_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public static SimpleDateFormat SDF_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		public static SimpleDateFormat SDF_3 = new SimpleDateFormat("yyyy-MM-dd HH");
		public static SimpleDateFormat SDF_4 = new SimpleDateFormat("yyyy-MM-dd");
		public static SimpleDateFormat SDF_5 = new SimpleDateFormat("yyyy-MM");
		public static SimpleDateFormat SDF_6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		public static SimpleDateFormat SDF_7 = new SimpleDateFormat("HH:mm:ss");
		
		public static SimpleDateFormat[] sdfs = {SDF_1, SDF_2, SDF_3, SDF_4, SDF_5, SDF_6, SDF_7};
		
		@SuppressWarnings("deprecation")
		public static Date convert(String source) {
			if(isBlank(source)) {
				return null;
			}
			
			source = replaceEach(source, new String[]{"/", "年", "月", "日", "T"}, new String[]{"-", "-", "-", " ", " "});
			
			for(SimpleDateFormat sdf: sdfs) {
				try {
					return sdf.parse(source);
				} catch (Throwable e) {}
			}
			// long型时间戳
			try {
				return new Date(Long.valueOf(source));
			} catch (Throwable e) {}
			// EEE dd MMM yyyy HH:mm:ss
			try {
				return new Date(source);
			} catch (Throwable e) {}

			throw new RuntimeException("Unsupported type!");
		}
	}
 }