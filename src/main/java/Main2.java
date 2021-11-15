import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main2 {

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -365);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(format.format(c.getTime()));

	}

}
