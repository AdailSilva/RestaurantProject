import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class t {

	// @NumberFormat(pattern = "#,##0.00")
	// BigDecimal c = new BigDecimal("1,1");

	java.text.DecimalFormat format = new java.text.DecimalFormat("#,###,##0.00");

	public static void main(String[] args) {

		Date d = new Date();
		Date t = new Date();
		d  = DateUtils.truncate(d, Calendar.DAY_OF_MONTH);
		System.out.println(DateUtils.truncate(d, Calendar.DAY_OF_MONTH).equals(t));
		System.out.println(d);

	}
}
