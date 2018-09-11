import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {
	private String name;
	private LocalDate date;
	List<DayOfWeek> days;
	private TimeInterval t_interval;
	private boolean repeating;
	
	Event(String name, LocalDate date, TimeInterval t_interval) {
		this.name = name;
		this.date = date;
		this.t_interval = t_interval;
		repeating = false;
		days = null;
	}
	
	Event(String name, List<String> days, TimeInterval t_interval) {
		this.name = name;
		this.t_interval = t_interval;
		repeating = true;
		date = null;
		
		this.days = new ArrayList();
		for (String day : days) {
			if (day.equals("S")) {
				this.days.add(DayOfWeek.SUNDAY);
			} else if (day.equals("M")) {
				this.days.add(DayOfWeek.MONDAY);
			} else if (days.equals("T")) {
				this.days.add(DayOfWeek.TUESDAY);
			} else if (days.equals("W")) {
				this.days.add(DayOfWeek.WEDNESDAY);
			} else if (days.equals("R")) {
				this.days.add(DayOfWeek.THURSDAY);
			} else if (days.equals("F")) {
				this.days.add(DayOfWeek.FRIDAY);
			} else if (days.equals("A")) {
				this.days.add(DayOfWeek.SATURDAY);
			}
		}
	}
	
	public boolean matchesDate(LocalDate date) {
		if (this.date != null) {
			return this.date.getDayOfMonth() == date.getDayOfMonth() &&
				this.date.getMonth() == date.getMonth() &&
				this.date.getYear() == date.getYear();
		} else if (days.contains(date.getDayOfWeek())) {
			return true;
		}
		
		return false;
	}
	
	public boolean conflictsWith(Event event) {
		// If this is on the same date and time
		if (event.date == null) {
			boolean sameDay = false;
			for (DayOfWeek day : event.days) {
				if (days != null && days.contains(day)) sameDay = true;
			}

			return sameDay && event.t_interval.conflictsWith(t_interval);
		}
	
		return matchesDate(event.date) && event.t_interval.conflictsWith(t_interval);
	}
	
	public String getName() { return name; }
	public LocalTime getStartTime() { return t_interval.startingTime; }
	public LocalTime getEndTime() { return t_interval.endingTime; }
	
	public String toString() {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm");
		return String.format("%s %s-%s %s",
					name,
					timeFormatter.format(getStartTime()),
					timeFormatter.format(getEndTime()), 
					date != null ? date.toString() : "Repeating");
	}
}
