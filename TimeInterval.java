import java.time.LocalTime;

public class TimeInterval {
	LocalTime startingTime;
	LocalTime endingTime;
	public TimeInterval (LocalTime startTime, LocalTime endTime) {
		this.startingTime = startTime;
		this.endingTime = endTime;
	}

	public boolean conflictsWith (TimeInterval t) {
		return (isBetween(t.startingTime, startingTime, endingTime)) ||
				(isBetween(t.endingTime, startingTime, endingTime));
	}
	
	private static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
		return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
	}
	
}
