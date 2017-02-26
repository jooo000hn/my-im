package com.goku.oauth.util;

import com.goku.oauth.userdetails.GkUserDetails;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: user Date: 15/11/11 Version: 1.0
 */
public class TokenCodeGeneratorUtils {

	private static final long seed = 10;

	public static String randomUUID(GkUserDetails gkUserDetails) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(gkUserDetails.getUserId());
		stringBuilder.append(createRandomWord());
		stringBuilder.append(createSnowflakeUUID());
		return stringBuilder.toString();
		// return EncryptUtil.encryptString(stringBuilder.toString());
	}

	/**
	 * 生成随机数
	 * 
	 * @return
	 */
	public static long createSnowflakeUUID() {
		// new SecureRandom().getProvider().
		return ThreadLocalRandom.current().nextLong(1000);
		// IdProvider idProvider = new SnowflakeIdProvider(1);
		// long seqNum = 0;
		// try {
		// seqNum = idProvider.getId();
		// } catch (InvalidSystemClockException e) {
		// e.printStackTrace();
		// throw new BizServiceException("生成token失效!");
		// } catch (SequenceExhaustedException e) {
		// e.printStackTrace();
		// throw new BizServiceException("生成token失效!");
		// }
		//
		// return String.valueOf(seqNum);
	}

	/**
	 * 生成随机字母
	 * 
	 * @return
	 */
	public static char createRandomWord() {
		Random random = new Random();
		int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写
		return (char) (choice + random.nextInt(26));
	}

	public static void main(String[] args) {

		String aa = "123B1234";
		String[] s = aa.split("[a-zA-Z]");
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}	
		
		//
		// Set<String> ss = new HashSet<>();
		// for (int i=0;i<4;i++) {
		// System.out.println(TokenCodeGeneratorUtils.createSnowflakeUUID());
		// // ss.add(TokenCodeGeneratorUtils.createSnowflakeUUID());
		// }
		// System.out.println(ss+"ss~"+ss.size());
	}
}
