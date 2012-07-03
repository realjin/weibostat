package info.realjin.weibo.weibostat;

import com.mongodb.Mongo;

public class WeiboContext {
	private String accessToken;

	// tweet saver settings
	private int tweetSaverBatchSize;

	// persistence
	private Mongo mongo;
	private String mongo_dbname;
	private String mongo_collname_tweets;
	private String mongo_collname_user;
	public String getMongo_collname_user() {
		return mongo_collname_user;
	}

	public void setMongo_collname_user(String mongo_collname_user) {
		this.mongo_collname_user = mongo_collname_user;
	}

	private String mongo_collname_user_ex;

	public String getMongo_collname_user_ex() {
		return mongo_collname_user_ex;
	}

	public void setMongo_collname_user_ex(String mongo_collname_user_ex) {
		this.mongo_collname_user_ex = mongo_collname_user_ex;
	}

	public String getMongo_dbname() {
		return mongo_dbname;
	}

	public void setMongo_dbname(String mongo_dbname) {
		this.mongo_dbname = mongo_dbname;
	}

	public String getMongo_collname_tweets() {
		return mongo_collname_tweets;
	}

	public void setMongo_collname_tweets(String mongo_collname_tweets) {
		this.mongo_collname_tweets = mongo_collname_tweets;
	}

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getTweetSaverBatchSize() {
		return tweetSaverBatchSize;
	}

	public void setTweetSaverBatchSize(int tweetSaverBatchSize) {
		this.tweetSaverBatchSize = tweetSaverBatchSize;
	}
}
