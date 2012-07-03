package info.realjin.weibo.weibostat.analyzer.freq;

import info.realjin.weibo.weibostat.WeiboContext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TweetFreqAnalyzer {
	private WeiboContext ctx;

	public TweetFreqAnalyzer(WeiboContext c) {
		this.ctx = c;
	}

	public WeiboContext getCtx() {
		return ctx;
	}

	public void setCtx(WeiboContext ctx) {
		this.ctx = ctx;
	}

	public List<TweetTimestamp> analyze(String strUid) {
		Mongo m = ctx.getMongo();
		DB db = m.getDB(ctx.getMongo_dbname());
		DBCollection coll = db.getCollection(ctx.getMongo_collname_tweets());

		BasicDBObject query = new BasicDBObject();
		query.put("uidstr", strUid);
		DBCursor cur = coll.find(query);
		DateStrLoader dsl = new DateStrLoader();
		List<TweetTimestamp> ttsList = new ArrayList<TweetTimestamp>();
		while (cur.hasNext()) {
			DBObject dboTweet = cur.next();
			String strDate = dboTweet.get("created_at").toString();
			System.out.println(strDate);
			Date date = dsl.Load(strDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			TweetTimestamp tts = new TweetTimestamp();
			tts.setYear(cal.get(Calendar.YEAR));
			tts.setDoy(cal.get(Calendar.DAY_OF_YEAR));
			tts.setHour(cal.get(Calendar.HOUR_OF_DAY));
			ttsList.add(tts);
		}

		return ttsList;

	}

	// -----------
	public void save(File f, List<TweetTimestamp> ttsList) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			for (TweetTimestamp tts : ttsList) {
				System.out.println(tts);
				bos.write(tts.toString().getBytes());
				bos.write("\r\n".getBytes());
			}
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class TweetTimestamp {
		private int year;
		private int doy;// day of year
		private int hour;

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getDoy() {
			return doy;
		}

		public void setDoy(int doy) {
			this.doy = doy;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public String toString() {
			return year + " " + doy + " " + hour;
		}
	}
}
