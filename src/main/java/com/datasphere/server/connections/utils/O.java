package com.datasphere.server.connections.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 快捷输出类；
 * 1.输出到System.out对象
 *
 */
public class O {
	
	public static void log(Object obj) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " Thread(" + String.valueOf(Thread.currentThread().getId()) + ")--" + obj);
	}
	
	public static void json(Object o) {
		try {
			System.out.println(new ObjectMapper().writeValueAsString(o));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
