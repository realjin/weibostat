package info.realjin.weibo.weibostat.analyzer.freq;

import info.realjin.weibo.weibostat.Test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStrLoader {

	Date Load(String line) {
		int firstSpace = line.indexOf(' ');
		int beforeRegion = line.indexOf('+') - 1;
		String valid = line.substring(firstSpace + 1, beforeRegion);
		int afterMonth = valid.indexOf(' ');
		String month = valid.substring(0, afterMonth);
		int nMonth = Test.Month.valueOf(month).getV();
		DecimalFormat df = new DecimalFormat("##00");
		valid = valid.replace(month, df.format(nMonth));
		// append year
		valid = valid + line.substring(line.length() - 4 - 1, line.length());
		// System.out.println("modified str: " + valid);

		SimpleDateFormat smf = new SimpleDateFormat("MM dd HH:mm:ss yyyy");
		Date d = null;
		try {
			d = smf.parse(valid);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("formatted: " + d);
		return d;
	}
}
