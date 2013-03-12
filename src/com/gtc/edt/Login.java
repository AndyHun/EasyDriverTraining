/*
 * Login.java Mar 12, 2013
 * 
 * Copyright 2013 General TECH , Inc. All rights reserved.
 */
package com.gtc.edt;

import java.util.Scanner;

/**
 * @description
 * @author AndyHun
 * 
 * @Version 1.0
 */
public class Login {

	public static void main(String args[]) {

		if (args.length < 2) {
			System.out.println("Error: Please add userName and pwd! \n For Example: java -jar EDT.jar userName pwd");
			return;
		}

		String userName = args[0];
		String pwd = args[1];

		System.out.println("UserName:" + userName + " Pwd:" + pwd);

		Robot robot = new Robot(userName, pwd);
		robot.start();

		Scanner sc = new Scanner(System.in);
		String cmd = null;

		while (!Cmd.EXIT.equalsIgnoreCase(cmd)) {
			System.out.println("Input 'exit' to exit the programe:");
			cmd = sc.nextLine();
		}
		robot.setCmd(cmd);
		if (robot != null) {
			robot.interrupt();
		}
	}
}
