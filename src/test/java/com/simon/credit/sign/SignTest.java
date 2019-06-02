package com.simon.credit.sign;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.SignUtil;

/**
 * Created by xs on 2019/2/25.
 */
public class SignTest {

	private static final String APPID 	   = "AAA";
	private static final String FORMAT 	   = "json";
	private static final String PRODUCTID  = "30000";
	private static final String SECRET_KEY = "HJeKKKL6Y3ytvtzO";

	public static void main(String[] args) {
		// 发送方加签
		Map<String, String> params = new HashMap<String, String>(8);

		// 公共参数
		params.put("appId"	  , APPID);
		params.put("_t"		  , String.valueOf(System.currentTimeMillis()));// 时间戳
		params.put("format"	  , FORMAT);
		params.put("productId", PRODUCTID);

		// 业务参数
		Map<String, Object> bizParams = new HashMap<>();
		bizParams.put("oldUrl", "http://58.com");
		System.out.println("业务参数:::" + JSONObject.toJSONString(bizParams));

		params.put("params", JSONObject.toJSONString(bizParams));// 加入业务参数

		String sign = SignUtil.getSign(params, SECRET_KEY);// 获取签名
		System.out.println("生成签名:::" + sign);

		params.put("sign", sign);
		System.out.println("全量参数:::" + JSONObject.toJSONString(params));

		// 2.接收方
		// 验签
		boolean isOK = SignUtil.veryfy(params, SECRET_KEY);
		System.out.println("验签结果:::" + isOK);
	}

}
