package info.realjin.weibo.weibostat;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestMongoDB {
	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo m = new Mongo();

		DB db = m.getDB( "mydb" );
		DBCollection coll = db.getCollection("testCollection");
		System.out.println("doc---> " +coll.findOne());
		
//		BasicDBObject doc = new BasicDBObject();
//
//        doc.put("name", "MongoDB");
//        doc.put("type", "database");
//        doc.put("count", 1);
//
//        BasicDBObject info = new BasicDBObject();
//
//        info.put("x", 203);
//        info.put("y", 102);
//
//        doc.put("info", info);
//
//        coll.insert(doc);
        
	}
}
