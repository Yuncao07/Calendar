import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyCalendar {
	private final List<Event> events = new ArrayList();
	private LocalDate currentDate;
	public List<Event> getEvents() {
		return events;
	}
	public void print() {
		for(Event e : events) {
			System.out.println(e.toString());
		}
	}
	public List<Event> getEvent(LocalDate date) {
		List<Event> events = new ArrayList<Event>();
		for (Event event : this.events) {
			if (event.matchesDate(date)) {
				events.add(event);
			}
		}
		
		return events;
	}

	public boolean addEvent(Event event) {
		for (Event e : events) {
			if (event.conflictsWith(e)) {
				return false;
			}
		}

		events.add(event);
		return true;
	}
	
	public boolean hasEventOnDay(LocalDate date) {
		return getEvent(date).size() > 0;
	}
}
