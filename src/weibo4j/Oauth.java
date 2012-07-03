package weibo4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import weibo4j.http.AccessToken;
import weibo4j.http.BASE64Encoder;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

public class Oauth {
	// ----------------------------閽堝绔欏唴搴旂敤澶勭悊SignedRequest鑾峰彇accesstoken----------------------------------------
	public String access_token;
	public String user_id;

	public String getToken() {
		return access_token;
	}

	/*
	 * 瑙ｆ瀽绔欏唴搴旂敤post鐨凷ignedRequest split涓簆art1鍜宲art2涓ら儴鍒�
	 */
	public String parseSignedRequest(String signed_request) throws IOException,
			InvalidKeyException, NoSuchAlgorithmException {
		String[] t = signed_request.split("\\.", 2);
		// 涓轰簡鍜�url encode/decode 涓嶅啿绐侊紝base64url 缂栫爜鏂瑰紡浼氬皢
		// '+'锛�/'杞崲鎴�-'锛�_'锛屽苟涓斿幓鎺夌粨灏剧殑'='銆�鍥犳瑙ｇ爜涔嬪墠闇�杩樺師鍒伴粯璁ょ殑base64缂栫爜锛岀粨灏剧殑'='鍙互鐢ㄤ互涓嬬畻娉曡繕鍘�
		int padding = (4 - t[0].length() % 4);
		for (int i = 0; i < padding; i++)
			t[0] += "=";
		String part1 = t[0].replace("-", "+").replace("_", "/");

		SecretKey key = new SecretKeySpec(WeiboConfig
				.getValue("client_SERCRET").getBytes(), "hmacSHA256");
		Mac m;
		m = Mac.getInstance("hmacSHA256");
		m.init(key);
		m.update(t[1].getBytes());
		String part1Expect = BASE64Encoder.encode(m.doFinal());

		sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();
		String s = new String(decode.decodeBuffer(t[1]));
		if (part1.equals(part1Expect)) {
			return ts(s);
		} else {
			return null;
		}
	}

	/*
	 * 澶勭悊瑙ｆ瀽鍚庣殑json瑙ｆ瀽
	 */
	public String ts(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			access_token = jsonObject.getString("oauth_token");
			user_id = jsonObject.getString("user_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return access_token;

	}

	/*----------------------------Oauth鎺ュ彛--------------------------------------*/

	public AccessToken getAccessTokenByCode(String code) throws WeiboException {
		return new AccessToken(Weibo.client.post(
				WeiboConfig.getValue("accessTokenURL"),
				new PostParameter[] {
						new PostParameter("client_id", WeiboConfig
								.getValue("client_ID")),
						new PostParameter("client_secret", WeiboConfig
								.getValue("client_SECRET")),
						new PostParameter("grant_type", "authorization_code"),
						new PostParameter("code", code),
						new PostParameter("redirect_uri", WeiboConfig
								.getValue("redirect_URI")) }, false));
	}

	public String authorize(String response_type) throws WeiboException {
		return WeiboConfig.getValue("authorizeURL").trim() + "?client_id="
				+ WeiboConfig.getValue("client_ID").trim() + "&redirect_uri="
				+ WeiboConfig.getValue("redirect_URI").trim()
				+ "&response_type=" + response_type;
	}
}
