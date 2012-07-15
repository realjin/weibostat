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

	public List<DBObject> analyzeTruthFreq(String uid) {
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
				// System.out.println("======#" + dboTweet.get("idstr") + "["
				//		+ pos + "] @" + dboTweet.get("uidstr") + ": " + around);
				result.add(dboTweet);
			}
		}

		// remove the duplicate through n steps:
		// 1. remove tweets that have same rewteet idstr:
		return result;

		// return ttsList;
	}

	public double calcTruthDegree(String uid) {
		int nTweetSaved;

		// 1. calc tweet saved
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());
		BasicDBObject query = new BasicDBObject();
		query.put("uidstr", uid);
		DBCursor cur = coll.find(query);
		nTweetSaved = 0;
		while (cur.hasNext()) {
			cur.next();
			nTweetSaved++;
		}

		// 2. calc score
		double score = 0.0;
		List<DBObject> tweets = analyzeTruthFreq(uid);
		for (DBObject t : tweets) {
			String line = t.get("text").toString();
			int pos = line.indexOf("真");
			String around = line.substring(pos - 2 >= 0 ? pos - 2 : 0,
					pos + 3 > line.length() ? line.length() : pos + 3);

			if (around.contains("真相")) {
				score += 1.0;
			} else if (around.contains("真理")) {
				score += 0.9;
			} else if (around.contains("真话")) {
				score += 0.9;
			} else if (around.contains("真实")) {
				score += 0.8;
			} else if (around.contains("真知")) {
				score += 0.7;
			} else if (around.contains("真正")) {
				score += 0.6;
			} else if (around.contains("纯真")) {
				score += 0.3;
			} else if (around.contains("真诚")) {
				score += 0.3;
			}
		}

		// 3. normalization
		score = score * 100.0 / nTweetSaved;

		return score;
	}
}
