package me.test;

import java.util.Arrays;

public class Test {

	public static void main(String[] args) {

		String test = "5;a;test ;% hi;la";
		System.out.println(Arrays.toString(test.split(";(?!%)")));

	}

}
