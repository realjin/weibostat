package info.realjin.weibo.weibostat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.PostParameter;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.BareBonesBrowserLaunch;
import weibo4j.util.WeiboConfig;

public class Test {
	static final String access_token = "2.00snTWXB0kYBzwa0ebedcea50C8QCL";
	static final String uid = "1411492984";

	public enum Month {
		Jan(1), Feb(2), Mar(3), Apr(4), May(5), Jun(6), Jul(7), Aug(8), Sep(9), Oct(
				10), Nov(11), Dec(12);
		int v;

		public int getV() {
			return v;
		}

		public void setV(int v) {
			this.v = v;
		}

		Month(int v) {
			this.v = v;
		}
	}

	public static void main9(String[] args) throws IOException, WeiboException,
			JSONException {
		// 1586260771

		class DateStrLoader {

			Date Load(String line) {
				int firstSpace = line.indexOf(' ');
				int beforeRegion = line.indexOf('+') - 1;
				String valid = line.substring(firstSpace + 1, beforeRegion);
				int afterMonth = valid.indexOf(' ');
				String month = valid.substring(0, afterMonth);
				int nMonth = Test.Month.valueOf(month).v;
				DecimalFormat df = new DecimalFormat("##00");
				valid = valid.replace(month, df.format(nMonth));
				// append year
				valid = valid
						+ line.substring(line.length() - 4 - 1, line.length());
				// System.out.println("modified str: " + valid);

				SimpleDateFormat smf = new SimpleDateFormat(
						"MM dd HH:mm:ss yyyy");
				Date d = null;
				try {
					d = smf.parse(valid);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// System.out.println("formatted: " + d);
				return d;
			}
		}

		FileInputStream fis = new FileInputStream(new File(
				"C:\\weibo_post_zxa_p1.txt"));
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buf = new byte[1024 * 1024];
		bis.read(buf);
		String s = new String(buf);
		JSONObject jso = new JSONObject(s);
		JSONArray jsa = jso.getJSONArray("statuses");
		int len = jsa.length();
		// System.out.println("len=" + len);
		DateStrLoader dsl = new DateStrLoader();
		for (int i = 0; i < len; i++) {
			JSONObject jsoPost = (JSONObject) jsa.get(i);
			Date d = dsl.Load(jsoPost.get("created_at").toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			System.out.println(cal.get(Calendar.DAY_OF_YEAR));
			// System.out.println(jsoPost.get("created_at"));
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws WeiboException
	 * @throws JSONException
	 */
	public static void main7(String[] args) throws IOException, WeiboException,
			JSONException {
		class DateStrLoader {

			Date Load(String line) {
				int firstSpace = line.indexOf(' ');
				int beforeRegion = line.indexOf('+') - 1;
				String valid = line.substring(firstSpace + 1, beforeRegion);
				int afterMonth = valid.indexOf(' ');
				String month = valid.substring(0, afterMonth);
				int nMonth = Test.Month.valueOf(month).v;
				DecimalFormat df = new DecimalFormat("##00");
				valid = valid.replace(month, df.format(nMonth));
				// append year
				valid = valid
						+ line.substring(line.length() - 4 - 1, line.length());
				// System.out.println("modified str: " + valid);

				SimpleDateFormat smf = new SimpleDateFormat(
						"MM dd HH:mm:ss yyyy");
				Date d = null;
				try {
					d = smf.parse(valid);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// System.out.println("formatted: " + d);
				return d;
			}
		}

		FileInputStream fis = new FileInputStream(new File(
				"C:\\weibo_post_eniu9350_p1.txt"));
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buf = new byte[1024 * 1024];
		bis.read(buf);
		String s = new String(buf);
		JSONObject jso = new JSONObject(s);
		JSONArray jsa = jso.getJSONArray("statuses");
		int len = jsa.length();
		// System.out.println("len=" + len);
		DateStrLoader dsl = new DateStrLoader();
		for (int i = 0; i < len; i++) {
			JSONObject jsoPost = (JSONObject) jsa.get(i);
			Date d = dsl.Load(jsoPost.get("created_at").toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			System.out.println(cal.get(Calendar.DAY_OF_YEAR));
			// System.out.println(jsoPost.get("created_at"));
		}

	}

	public static void mainGetPosts(String[] args) throws IOException,
			WeiboException, JSONException {
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);

		JSONObject jsoFollowings = Weibo.client
				.get(WeiboConfig.getValue("baseURL")
						+ "statuses/user_timeline.json",
						new PostParameter[] { new PostParameter("uid", uid),
								new PostParameter("count", 50),
								new PostParameter("page", 1) }).asJSONObject();

		FileOutputStream fos = new FileOutputStream(new File(
				"C:\\weibo_post_eniu9350_p1.txt"));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(jsoFollowings.toString().getBytes());
		bos.close();
		fos.close();
	}

	public static void mainGetFollowings(String[] args) throws IOException,
			WeiboException, JSONException {
		FileInputStream fis = new FileInputStream(new File(
				"C:\\weibofollowinglist.txt"));
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buf = new byte[1024 * 1024];
		bis.read(buf);
		String s = new String(buf);
		JSONObject jso = new JSONObject(s);
		JSONArray jsa = jso.getJSONArray("users");
		int len = jsa.length();
		System.out.println("len=" + len);
		for (int i = 0; i < len; i++) {
			JSONObject jsoUser = (JSONObject) jsa.get(i);
			System.out.println(jsoUser.get("name"));
		}

	}

	public static void main4(String[] args) throws IOException, WeiboException {
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);

		JSONObject jsoFollowings = Weibo.client.get(
				WeiboConfig.getValue("baseURL") + "friendships/friends.json",
				new PostParameter[] { new PostParameter("uid", uid),
						new PostParameter("count", 150) }).asJSONObject();

		FileOutputStream fos = new FileOutputStream(new File(
				"C:\\weibofollowinglist.txt"));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(jsoFollowings.toString().getBytes());
		bos.close();
		fos.close();
	}

	public static void main3(String[] args) throws IOException, WeiboException {

		Weibo weibo = new Weibo();
		weibo.setToken(access_token);
		String uid = "1411492984";
		Users um = new Users();
		try {
			User user = um.showUserById(uid);
			Log.logInfo(user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, WeiboException {
		Oauth oauth = new Oauth();
		BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
		System.out.print("Hit enter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String code = br.readLine();
		// Log.logInfo("code: " + code);
		try {
			System.out.println(oauth.getAccessTokenByCode(code));
		} catch (WeiboException e) {
			if (401 == e.getStatusCode()) {
				// Log.logInfo("Unable to get the access token.");
			} else {
				e.printStackTrace();
			}
		}
	}

}
