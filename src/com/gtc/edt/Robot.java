/*
 * Robot.java Mar 12, 2013
 * 
 * Copyright 2013 General TECH , Inc. All rights reserved.
 */
package com.gtc.edt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.gtc.edt.util.PropertiestUtil;

/**
 * @description
 * @author AndyHun
 * 
 * @Version 1.0
 */
public class Robot extends Thread {

	private static final int SLEEP_INTERVAL = 240000;

	private static final String MENU = "http://218.85.137.213:6010/xmll/examList.do";

	private HttpClient httpClient = null;

	private Date logonDate = null;
	
	private String cmd = null;
	
	public boolean isLogin = false;
	
	private String userName;
	
	private String pwd;
	
	public Robot(){
		
	}
	
	public Robot(String userName, String pwd) {
		this.userName = userName;
		this.pwd = pwd;
	}

	public void run() {
		logon();
		if(!isLogin){
			return;
		}
		while (PropertiestUtil.isEnable() && !Cmd.EXIT.equalsIgnoreCase(cmd)) {
			try {
				sleep(SLEEP_INTERVAL);
				refresh(httpClient, logonDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			logout(httpClient, logonDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logon() {
		if (httpClient != null) {
			return;
		}
		logonDate = new Date();
		httpClient = new DefaultHttpClient();
		HttpPost logonPost = new HttpPost("http://218.85.137.213:6010/xmll/loginSubmit.do");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("area", "xm"));
		params.add(new BasicNameValuePair("cardType", "sfz"));
		params.add(new BasicNameValuePair("cardNumber", userName));
		params.add(new BasicNameValuePair("password", pwd));

		try {
			logonPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(logonPost);
			System.out.println("Logon...");
			String logonMessage = new String(transformInputstream(httpResponse.getEntity().getContent()), "UTF-8");
			System.out.println(logonMessage);
			if(logonMessage.indexOf("message")>0){
				isLogin = false;
				System.out.println("Input Wrong UserName or Pwd!Please Exit The Programe!");
				Thread.currentThread().stop();
				return;
			}else{
				isLogin = true;
			}
			logonPost.releaseConnection();
			logonPost.abort();

			HttpPost mainPost = new HttpPost("http://218.85.137.213:6010/xmll/beginTrain.do");
			HttpResponse httpResponseMain = httpClient.execute(mainPost);
			System.out.println("Access beginTrain.do ...");
			//System.out.println(new String(transformInputstream(httpResponseMain.getEntity().getContent()), "UTF-8"));
			mainPost.releaseConnection();
			mainPost.abort();

			HttpPost mainPost2 = new HttpPost("http://218.85.137.213:6010/xmll/note.do");
			HttpResponse httpResponseMain2 = httpClient.execute(mainPost2);
			System.out.println("Access note.do ...");
			//System.out.println(new String(transformInputstream(httpResponseMain2.getEntity().getContent()), "UTF-8"));
			mainPost2.releaseConnection();
			mainPost2.abort();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void logout(HttpClient httpClient, Date logonDate) throws Exception {
		HttpPost logoutPost = new HttpPost("http://218.85.137.213:6010/xmll/endTrain.do");
		HttpResponse logoutResponse = httpClient.execute(logoutPost);
		System.out.println("Exit...");
		Date currentDate = new Date();
		System.out.println("Timer:" + (currentDate.getTime() - logonDate.getTime()) / (1000*60) + "Minutes...");
		System.out.println(new String(transformInputstream(logoutResponse.getEntity().getContent()), "UTF-8"));
		httpClient.getConnectionManager().shutdown();
	}

	private static void refresh(HttpClient httpClient, Date logonDate) throws Exception {
		HttpPost refreshPost = new HttpPost(MENU);
		try {
			HttpResponse refreshResponse = httpClient.execute(refreshPost);
			System.out.println("Refresh...");
			Date currentDate = new Date();
			System.out.println("Timer:" + (currentDate.getTime() - logonDate.getTime()) / (1000*60) + "Minutes...");
			// System.out.println(new
			// String(transformInputstream(refreshResponse.getEntity().getContent()),
			// "UTF-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			refreshPost.releaseConnection();
			refreshPost.abort();
		}
	}

	private static byte[] transformInputstream(InputStream input) throws Exception {
		byte[] byt = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b = 0;
		b = input.read();
		while (b != -1) {
			baos.write(b);
			b = input.read();
		}
		byt = baos.toByteArray();
		return byt;
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
}
