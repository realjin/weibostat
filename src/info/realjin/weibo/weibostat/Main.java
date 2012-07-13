package info.realjin.weibo.weibostat;

import info.realjin.weibo.weibostat.analyzer.freq.TweetFreqAnalyzer;
import info.realjin.weibo.weibostat.analyzer.freq.TweetFreqAnalyzer.TweetTimestamp;
import info.realjin.weibo.weibostat.analyzer.truth.TruthAnalyzer;
import info.realjin.weibo.weibostat.tweet.TweetSaver;
import info.realjin.weibo.weibostat.tweet.TweetSavingProgressChecker;
import info.realjin.weibo.weibostat.user.UserSaver;

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import weibo4j.Weibo;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Main {

	private static WeiboContext ctx;

	public static void initContext() {
		ctx = new WeiboContext();

		ctx.setAccessToken("2.00snTWXB0kYBzw1d49aed9c0PYh7vB");

		ctx.setTweetSaverBatchSize(100);

		Mongo mongo = null;
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		ctx.setMongo(mongo);
		String mongo_dbname = "weibostat";
		String mongo_collname_tweets = "tweets";
		String mongo_collname_user = "user";
		String mongo_collname_user_ex = "user_ex";
		ctx.setMongo_dbname(mongo_dbname);
		ctx.setMongo_collname_tweets(mongo_collname_tweets);
		ctx.setMongo_collname_user(mongo_collname_user);
		ctx.setMongo_collname_user_ex(mongo_collname_user_ex);

		// init weibo config
		Weibo.client.setToken(ctx.getAccessToken());
	}

	static {
		initContext();
	}

	public static void main(String[] args) throws UnknownHostException,
			MongoException {
		TruthAnalyzer ta = new TruthAnalyzer(ctx);
		UserSaver us = new UserSaver(ctx);
		List<String> uids = us.getSavedUids();
		for (String uid : uids) {
			ta.analyzeTruthFreq(uid);
			// System.out.println(uid);
		}

	}

	/**
	 * @param args
	 * @throws MongoException
	 * @throws UnknownHostException
	 */
	public static void main_(String[] args) throws UnknownHostException,
			MongoException {
		// saveTweets("1267114595");
		// analyzeTweetFreq("1267114595");
		checkTweetSavingStatus("1267114595");
		// checkTweetSavingStatus("1586260771");
		// checkTweetSavingStatus("1722594714");
		// testSaveUser("1765731810");

		// TweetSaver ps = new TweetSaver(ctx);
		// Set<String> tids = ps.getUserSavedTweetIds("1267114595");
		//
		// for (String s : tids) {
		// System.out.println("tid: " + s);
		// }
	}

	public static void testSaveUser(String uid) {
		UserSaver us = new UserSaver(ctx);
		us.save(uid);
		// us.removeTag(uid, "abroad");
	}

	public static void checkTweetSavingStatus(String uid) {
		TweetSavingProgressChecker tspc = new TweetSavingProgressChecker(ctx);
		tspc.checkProgress(uid);
	}

	public static void analyzeTweetFreq(String uid)
			throws UnknownHostException, MongoException {
		TweetFreqAnalyzer tfa = new TweetFreqAnalyzer(ctx);
		List<TweetTimestamp> ttsList = tfa.analyze(uid);
		tfa.save(
				new File("I:\\mongodb205\\ext_temp\\tweetfreq_" + uid + ".txt"),
				ttsList);
	}

	public static void saveTweets(String uid) throws UnknownHostException,
			MongoException {

		TweetSaver ps = new TweetSaver(ctx);
		ps.save(uid);
	}

}
