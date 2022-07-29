package kafka.demo.kafkademo;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

public class WalmartWeekNumber {
	public  Integer getWMWeekNumber(LocalDate date) {
		int year = date.getYear();
		Locale locale = new Locale("ar");
		return (date.getMonth().getValue() == 1)
				? (LocalDate.of(year, date.getMonth().getValue(), date.getDayOfMonth()).get(WeekFields.of(locale)
						.weekOfYear()) == LocalDate.of(year, 2, 1).get(WeekFields.of(locale).weekOfYear()))
								? 1
								: LocalDate.of(year - 1, 12, 31).get(WeekFields.of(locale).weekOfYear())
										- LocalDate.of(year - 1, 2, 1).get(WeekFields.of(locale).weekOfYear())
										+ LocalDate.of(year, date.getMonth().getValue(), date.getDayOfMonth())
												.get(WeekFields.of(locale).weekOfYear())
										+ (LocalDate.of(year, 1, 1).getDayOfWeek().getValue() == 6 ? 0 : -1) + 1
				: LocalDate.of(year, date.getMonth().getValue(), date.getDayOfMonth())
						.get(WeekFields.of(locale).weekOfYear())
						- LocalDate.of(year, 2, 1).get(WeekFields.of(locale).weekOfYear()) + 1;
	}
}
