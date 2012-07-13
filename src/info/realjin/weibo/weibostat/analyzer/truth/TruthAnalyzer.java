package info.realjin.weibo.weibostat.analyzer.truth;

import info.realjin.weibo.weibostat.WeiboContext;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TruthAnalyzer {
	private WeiboContext ctx;

	public TruthAnalyzer(WeiboContext c) {
		this.ctx = c;
	}

	public void analyzeTruthFreq(String uid) {
		String line;
		int pos;
		List<DBObject> result = new ArrayList<DBObject>();

		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());

		BasicDBObject query = new BasicDBObject();
		query.put("uidstr", uid);
		DBCursor cur = coll.find(query);

		// DateStrLoader dsl = new DateStrLoader();
		// List<TweetTimestamp> ttsList = new ArrayList<TweetTimestamp>();
		while (cur.hasNext()) {
			DBObject dboTweet = cur.next();

			line = dboTweet.get("text").toString();

			// System.out.println(line);

			pos = line.indexOf("真");

			if (pos != -1) {
				String around = line.substring(pos - 2 >= 0 ? pos - 2 : 0,
						pos + 3 > line.length() ? line.length() : pos + 3);
				System.out.println("======#" + dboTweet.get("idstr") + "["
						+ pos + "] @" + dboTweet.get("uidstr") + ": " + around);
				result.add(dboTweet);
			}
		}
		
		
		
		//remove the duplicate through n steps:
		//1. remove tweets that have same rewteet idstr:
		

		// return ttsList;
	}
}
