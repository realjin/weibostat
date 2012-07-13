package info.realjin.weibo.weibostat.tweet;

import info.realjin.weibo.weibostat.WeiboContext;

import java.util.Date;

import weibo4j.Weibo;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class TweetSavingProgressChecker {
	WeiboContext ctx;

	public TweetSavingProgressChecker(WeiboContext c) {
		this.ctx = c;
	}

	public void checkProgress(String strUid) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());
		DBCollection collUserEx = db.getCollection(ctx
				.getMongo_collname_user_ex());

		try {
			JSONObject jsoUser = Weibo.client.get(
					WeiboConfig.getValue("baseURL") + "users/show.json",
					new PostParameter[] { new PostParameter("uid", strUid) })
					.asJSONObject();
			int nTweets = jsoUser.getInt("statuses_count");

			BasicDBObject query;
			DBCursor cur;
			query = new BasicDBObject();
			query.put("uidstr", strUid);
			cur = coll.find(query);
			int nTweetsSaved = 0;
			while (cur.hasNext()) {
				cur.next();
				nTweetsSaved++;
			}

			double progress = nTweetsSaved * 1.0 / nTweets;
			System.out.println("progress=" + progress);

			JSONObject jso = new JSONObject();
			jso.put("uidstr", strUid);
			jso.put("screen_name", jsoUser.get("screen_name"));
			jso.put("name", jsoUser.get("name"));
			jso.put("saved", nTweetsSaved);
			jso.put("total", nTweets);
			jso.put("progress", progress);
			jso.put("updatetime", new Date().toString());
			BasicDBObject dbo = (BasicDBObject) com.mongodb.util.JSON.parse(jso
					.toString());

			query = new BasicDBObject();
			query.put("uidstr", strUid);
			collUserEx.update(query, dbo, true, false);

		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public WeiboContext getCfg() {
		return ctx;
	}

	public void setCfg(WeiboContext cfg) {
		this.ctx = cfg;
	}
}
