package com.adailsilva.test;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -365);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(format.format(c.getTime()));

	}

}
