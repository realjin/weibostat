package info.realjin.weibo.weibostat.user;

import info.realjin.weibo.weibostat.WeiboContext;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Weibo;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class UserSaver {
	WeiboContext ctx;

	public UserSaver(WeiboContext c) {
		this.ctx = c;
	}

	public void save(String uid) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_user());

		try {
			JSONObject jsoUser = Weibo.client.get(
					WeiboConfig.getValue("baseURL") + "users/show.json",
					new PostParameter[] { new PostParameter("uid", uid) })
					.asJSONObject();

			BasicDBObject query = new BasicDBObject();
			query.put("id", uid);
			// check if dup
			DBCursor cur = coll.find(query);
			DBObject dbo;
			if (cur.hasNext()) {
				dbo = cur.next();
				dbo.put("followers_count", jsoUser.get("followers_count"));
				dbo.put("friends_count", jsoUser.get("friends_count"));
				dbo.put("statuses_count", jsoUser.get("statuses_count"));
				dbo.put("favourites_count", jsoUser.get("favourites_count"));
				dbo.put("description", jsoUser.get("description"));

				coll.save(dbo);
			} else {
				dbo = (BasicDBObject) com.mongodb.util.JSON.parse(jsoUser
						.toString());
				dbo.put("idstr", uid);
				// dbo.put("tag", com.mongodb.util.JSON.parse("[]"));
				BasicDBList dbl = new BasicDBList();
				dbo.put("tag", dbl);

				coll.insert(dbo);
			}

		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addTag(String uid, String tag) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_user());

		BasicDBObject query = new BasicDBObject();
		query.put("idstr", uid);
		// check if dup
		DBCursor cur = coll.find(query);
		if (cur.hasNext()) {
			DBObject dbo = cur.next();
			BasicDBList dbl = (BasicDBList) dbo.get("tag");
			dbl.add(tag);
			dbo.put("tag", dbl);
			coll.save(dbo);
		} else {
			System.out.println("UserSaver::addTag: user \"" + uid
					+ "\" not exist!!!");
		}

	}

	public void removeTag(String uid, String tag) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_user());

		BasicDBObject query = new BasicDBObject();
		query.put("idstr", uid);
		// check if dup
		DBCursor cur = coll.find(query);
		if (cur.hasNext()) {
			DBObject dbo = cur.next();
			BasicDBList dbl = (BasicDBList) dbo.get("tag");
			if (dbl.contains(tag)) {
				dbl.remove(tag);
				dbo.put("tag", dbl);
				coll.save(dbo);
			} else {
				System.out.println("UserSaver::removeTag: tag \"" + tag
						+ "\" not exist!!!");
			}
		} else {
			System.out.println("UserSaver::removeTag: user \"" + uid
					+ "\" not exist!!!");
		}

	}

	public List<String> getSavedUids() {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_user_ex());

		List<String> uids = new ArrayList<String>();

		
		DBCursor cur = coll.find();
		while (cur.hasNext()) {
//			System.out.println("add");
			DBObject dboTweet = cur.next();

			String uid = dboTweet.get("uidstr").toString();
			uids.add(uid);
		}
		
		return uids;
	}

}
