package info.realjin.weibo.weibostat.tweet;

import info.realjin.weibo.weibostat.WeiboContext;
import info.realjin.weibo.weibostat.analyzer.freq.DateStrLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import weibo4j.Weibo;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TweetSaver {
	WeiboContext ctx;

	public TweetSaver(WeiboContext c) {
		this.ctx = c;
	}

	public WeiboContext getCfg() {
		return ctx;
	}

	public void setCfg(WeiboContext cfg) {
		this.ctx = cfg;
	}

	public void save(String uid) {
		saveByDate(uid);
	}

	private void saveByDate(String uid) {
		// Weibo weibo = new Weibo();
		// weibo.setToken(ctx.getAccessToken());

		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());

		JSONObject jsoTweets = null;
		// int flagDup = 0;
		int flagEnd = 0;

		Set<String> savedTweetIds = getUserSavedTweetIds(uid);

		try {
			// get all tweets
			for (int i = 0; i < 100; i++) {
				// for (int i = 0; i < 10&&flagDup==0; i++) {
				try {
					jsoTweets = Weibo.client.get(
							WeiboConfig.getValue("baseURL")
									+ "statuses/user_timeline.json",
							new PostParameter[] {
									new PostParameter("uid", uid),
									new PostParameter("count", ctx
											.getTweetSaverBatchSize()),
									new PostParameter("page", "" + (i + 1)) })
							.asJSONObject();
				} catch (WeiboException e) {
					i--;
					// e.printStackTrace();
					continue;
				}

				JSONArray jsa = jsoTweets.getJSONArray("statuses");

				// save to db

				int len = jsa.length();
				if (len == 0) {
					System.out
							.println("-------------------------------------len0");
					break;
				} else {
					System.out.println("===== round " + i + " ============");
				}
				// System.out.println("len=" + len);
				DateStrLoader dsl = new DateStrLoader();
				// flagDup = 0;

				for (int j = 0; j < len; j++) {
					JSONObject jsoTweet = (JSONObject) jsa.get(j);

					// check if dup
					// BasicDBObject query = new BasicDBObject();
					// query.put("id", jsoTweet.get("id"));
					// if (coll.find(query).hasNext()) {
					// continue;
					// }

					// check if dup(method2)
					String idstr = jsoTweet.get("idstr").toString();
					// System.out.println("-----idstr-----" + idstr);
					if (savedTweetIds.contains(idstr)) {
						// System.out.println("**exist!=======================");
						continue;
					}

					jsoTweet.put("uid",
							((JSONObject) jsoTweet.get("user")).get("id"));
					jsoTweet.put("uidstr",
							((JSONObject) jsoTweet.get("user")).get("idstr"));

					BasicDBObject dboTweet = (BasicDBObject) com.mongodb.util.JSON
							.parse(jsoTweet.toString());

					coll.insert(dboTweet);

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public Set<String> getUserSavedTweetIds(String uid) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());

		BasicDBObject query = new BasicDBObject();
		query.put("uidstr", uid);
		DBCursor cur = coll.find(query);

		Set<String> savedTweetIds = new HashSet<String>();

		// DateStrLoader dsl = new DateStrLoader();
		// List<TweetTimestamp> ttsList = new ArrayList<TweetTimestamp>();
		while (cur.hasNext()) {
			DBObject dboTweet = cur.next();

			// line = dboTweet.get("text").toString();

			// System.out.println(line);

			// pos = line.indexOf("真");

			savedTweetIds.add(dboTweet.get("idstr").toString());

		}

		return savedTweetIds;
	}
}
