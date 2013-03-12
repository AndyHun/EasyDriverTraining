/*
 * PropertiestUtil.java Mar 12, 2013
 * 
 * Copyright 2013 General TECH , Inc. All rights reserved.
 */
package com.gtc.edt.util;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @description
 * @author AndyHun
 * 
 * @Version 1.0
 */
public class PropertiestUtil {
	private static final String FILE_NAME = "/com/gtc/edt/config/config.xml";

	public static boolean isEnable() {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(PropertiestUtil.class.getResourceAsStream(FILE_NAME));
			Node node = document.selectSingleNode("//enable");
			System.out.println("enable config:"+node.getText());
			return Boolean.valueOf(node.getText());
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
	}
	
	public static void main(String[] args){
		System.out.println(PropertiestUtil.isEnable());
	}
}
