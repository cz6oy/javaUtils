package com.kayakwise.gray.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FundPublic {
	/**wangzhenxing
	 * 比较日期
	 * 
	 * @param nowtime
	 *            现在时间
	 * @param fixtime
	 *            指定固定时间
	 * @return boolean
	 */
	public static boolean CompileDate(String nowTime, String fixTime) {
		DateFormat df = new SimpleDateFormat("hhmmss");
		try {
			Date dt1 = df.parse(nowTime);
			Date dt2 = df.parse(fixTime);
			if (dt1.before(dt2)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

}
