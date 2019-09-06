package cn.yiyang.common.utils;


import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

/**
* @author Mxy
* @time 2018年7月9日 下午4:20:26
* @description: ID生成
*/
public class IdGen implements SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * 使用SecureRandom随机生成Long.
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * Activiti ID 生成
	 */
	public String getNextId() {
		return uuid();
	}

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}

	/**
	 * 生成验证码
	 * 
	 * @param length
	 *            获取验证码的长度
	 * @return
	 */
	public static String getCode(int length) {
		double result = 0;
		if (6 == length) {
			result = Math.random() * 900000 + 100000;
		} else {
			result = Math.random() * 9000 + 1000;
		}
		return String.valueOf(result).substring(0,
				String.valueOf(result).lastIndexOf("."));
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			System.out.println(IdGen.getCode(4));
		}
	}

}

