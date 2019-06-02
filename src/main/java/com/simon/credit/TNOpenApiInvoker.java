package com.simon.credit;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.simon.credit.util.HttpUtil;
import com.simon.credit.util.MD5Util;
import com.simon.credit.util.SignUtil;

/**
 * TN开放接口调用
 * <pre>
 * 调用链: 58金融-->TN开放服务
 * </pre>
 * @author XUZIMING 2019-06-02
 */
public class TNOpenApiInvoker {

	private static final String APPID 	   = "AAA";
	private static final String FORMAT 	   = "json";
	private static final String PRODUCTID  = "30000";
	private static final String SECRET_KEY = "HJeKKKL6Y3ytvtzO";

	public static void main(String[] args) throws Exception {
		// 1.机构申请准入接口
		testGj58CreditCheckUser();
	}

	/**
	 * 1.机构申请准入接口
	 * @throws Exception
	 */
	public static void testGj58CreditCheckUser() throws Exception {
		// 请求参数
		Map<String, String> params = new HashMap<String, String>(8);

		// 公共参数
		params.put("appId" , APPID);
		params.put("_t"	   , String.valueOf(System.currentTimeMillis()));
		params.put("format", FORMAT);

		// 业务参数
		Map<String, String> bizParams = new HashMap<String, String>(8);
		bizParams.put("productId"  , PRODUCTID);
		bizParams.put("userName"   , "刘彬彬");
		bizParams.put("phoneIdCard", MD5Util.md5(""));

		params.put("params", JSON.toJSONString(bizParams));
		params.put("sign"  , SignUtil.getSign(params, SECRET_KEY));

		// 1.机构申请准入接口(开放平台必须配置开放服务method: bj58.credit.checkUser, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/bj58/bj58.credit.checkUser";

		String response = HttpUtil.doPost(parseCorrectUrl(url), params, 10000, 10000);
		System.out.println("response: " + response);
	}

	private static String parseCorrectUrl(String url) {
		return url.replaceAll("$$", "");
	}

}
