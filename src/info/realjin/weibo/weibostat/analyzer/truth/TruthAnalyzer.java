package info.realjin.weibo.weibostat.analyzer.truth;

import info.realjin.weibo.weibostat.WeiboContext;

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

//			System.out.println(line);

			pos = line.indexOf("真");


			if (pos != -1) {
				String around = line.substring(pos - 2, pos + 3);
				System.out.println("======[" + pos + "] " + around);
			}

			// String strDate = dboTweet.get("created_at").toString();
			// System.out.println(strDate);
			// Date date = dsl.Load(strDate);

			// Calendar cal = Calendar.getInstance();
			// cal.setTime(date);
			//
			// TweetTimestamp tts = new TweetTimestamp();
			// tts.setYear(cal.get(Calendar.YEAR));
			// tts.setDoy(cal.get(Calendar.DAY_OF_YEAR));
			// tts.setHour(cal.get(Calendar.HOUR_OF_DAY));
			// ttsList.add(tts);
		}

		// return ttsList;
	}
}
