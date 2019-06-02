package com.simon.credit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SignUtil {

	private SignUtil() {}

	/**
	 * 过滤掉sign参数以及空值参数
	 * @param params
	 * @return
	 */
	private static Map<String, String> filterOutSignAndEmptyParams(Map<String, String> params) {
		Map<String, String> paramMap = new HashMap<String, String>(8);
		if (params == null || params.isEmpty()) {
			return paramMap;
		}

		for (Entry<String, String> entry : params.entrySet()) {
			if (!entry.getKey().equalsIgnoreCase("sign") && entry.getValue() != null && !entry.getValue().equals("")) {
				paramMap.put(entry.getKey(), entry.getValue());
			}
		}

		return paramMap;
	}

	private static String createLinkString(Map<String, String> params) {
		// TODO 过滤掉sign参数以及空值参数
		Map<String, String> newParams = filterOutSignAndEmptyParams(params);

		List<String> keys = new ArrayList<String>(newParams.keySet());

		// 参数按照字典(ASCII码)进行排序
		Collections.sort(keys);

		String paramString = "";

		for (int i = 0; i < keys.size(); ++i) {
			String key = keys.get(i);
			String value = newParams.get(key);
			if (i == keys.size() - 1) {
				paramString = paramString + key + "=" + value;
			} else {
				paramString = paramString + key + "=" + value + "&";
			}
		}

		return paramString;
	}

	public static String getSign(Map<String, String> paramMap, String secretKey) {
		String prestr = createLinkString(paramMap);
		String md5Code = EncryptUtil.getInstance().md5Encode(prestr.replaceAll("!", ""), secretKey);
		if (md5Code == null) {
			md5Code = "";
		}
		return md5Code.replaceAll("/", "").replaceAll("\\\\", "").replaceAll("\\+", "").replaceAll("=", "");
	}

	public static boolean veryfy(Map<String, String> params, String secretKey) {
		String sign = "";
		if (params != null && params.get("sign") != null) {
			sign = params.get("sign");
		}

		String newSign = getSign(params, secretKey);
		return sign.equals(newSign);
	}

}
