package edu.unlv.cs.rebelhotel.file;

import java.util.Random;

public class RandomPasswordGenerator {
	private static final int MAX_PASSWORD_LENGTH = 8;
	public String generateRandomPassword(){
		String charset = "12345ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&abcdefghijklmnopqrstuvwxyz67890";
		String firstcharset = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		Integer pos;
		pos = random.nextInt(firstcharset.length());
		sb.append(firstcharset.charAt(pos));
		for (int i = 1; i < MAX_PASSWORD_LENGTH; i++) {
			pos = random.nextInt(charset.length());
        	sb.append(charset.charAt(pos));
		}

		return sb.toString();
	}
}