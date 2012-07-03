package info.realjin.weibo.weibostat;

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

public class TestBDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database db = null;
		Environment env = null;

		try {

			EnvironmentConfig envConfig = new EnvironmentConfig();
			// envConfig.setAllowCreate(true);
			envConfig.setInitializeCache(true);
			env = new Environment(new File("I:\\bdb"), envConfig);

			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbConfig = new DatabaseConfig();
			// dbConfig.setAllowCreate(true);
			// dbConfig.setType(DatabaseType.BTREE);
			// dbConfig.setCreateDir(new File("I:\\bdb\\"));
			// db = env.openDatabase(null, "sampleDatabase.db", null,
			// dbConfig);
			db = env.openDatabase(null, "first.db", null, dbConfig);

			String aKey = "uid";
			String aData = "eniu9350";

			try {
				// DatabaseEntry theKey = new
				// DatabaseEntry(aKey.getBytes("UTF-8"));
				// DatabaseEntry theData = new DatabaseEntry(
				// aData.getBytes("UTF-8"));
				// db.put(null, theKey, theData);

				DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
				DatabaseEntry theData = new DatabaseEntry();

				// Perform the get.
				if (db.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

					// Recreate the data String.
					byte[] retData = theData.getData();
					String foundData = new String(retData, "UTF-8");
					System.out.println("For key: '" + aKey + "' found data: '"
							+ foundData + "'.");
				} else {
					System.out.println("No record found for key '" + aKey
							+ "'.");
				}

			} catch (Exception e) {
				// Exception handling goes here
			}

			db.close();

		} catch (DatabaseException dbe) {
			// Exception handling goes here
		} catch (FileNotFoundException fnfe) {
			// Exception handling goes here
		}
	}

}
