package cn.yiyang.common.utils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class MessageUtils {
	/**
	 * 发送短信
	 * 
	 * @param phone
	 * @param promessage
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> sendSms(String phone, String promessage)
			throws Exception {
		Map<String, String> result = Maps.newHashMap();
		if ((phone != null) && (!"".equals(phone))) {
			String x_id = "ZBT";
			String x_pwd = "246810";
			String content = IOUtils.toString(new URL(
					"http://service.winic.org/sys_port/gateway/?id=" + x_id
							+ "&pwd=" + x_pwd + "&to=" + phone + "&content="
							+ URLEncoder.encode(promessage, "gb2312")),
					"gb2312");
			System.out.println(content);
			if (StringUtils.isNotBlank(content)) {
				String[] sp = StringUtils.split(content, "/");
				if ("000".equalsIgnoreCase(sp[0])) {
					result.put("status", "短信发送成功！");
				} else if ("-01".equalsIgnoreCase(sp[0])) {
					result.put("status", "当前账号余额不足！");
				} else if ("-02".equalsIgnoreCase(sp[0])) {
					result.put("status", "当前用户ID错误！");
				} else if ("-03".equalsIgnoreCase(sp[0])) {
					result.put("status", "当前密码错误！");
				} else if ("-04".equalsIgnoreCase(sp[0])) {
					result.put("status", "参数不够或参数内容的类型错误！");
				} else if ("-12".equalsIgnoreCase(sp[0])) {
					result.put("status", "其它错误！");
				}
			} else {
				result.put("status", "无接收数据！");
			}
		} else {
			result.put("status", "手机号phone不能为空！");
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			Map<String, String> map = sendSms("18500345322", "您的验证码为：2063");
			System.out.println((String) map.get("status"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
