import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCalendarTester {
	private MyCalendar calendar;
	
	
	MyCalendarTester() {
		calendar = new MyCalendar();
	}

	static void printMainMenu() {
		System.out.println("\n\nSelect one of the following options:");
		System.out.println("[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
	}
	
	
	void printCalendar(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        System.out.println(" " + formatter.format(date));

        int daysInMonth = date.getMonth().length(date.isLeapYear());
        LocalDate first = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        int previousDays = first.getDayOfWeek().getValue() - 1;
        int daysInWeek = 7;
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(" dd ");
        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("dd");
        
        Arrays.asList(new String[]{
        	" Su ", " Mo ", " Tu ", " We ", " Th ", " Fr ", " Sa "
        }).stream().forEach(day -> System.out.print(day));

        System.out.println("");
        for (int i = -previousDays; i <= daysInMonth; i++) {
        	String output = "    ";
        	if (i > 0) {
        		LocalDate currentDate = LocalDate.of(date.getYear(), date.getMonthValue(), i);
        		if (i == date.getDayOfMonth() || calendar.hasEventOnDay(currentDate)) {
            		output= String.format("[%s]", todayFormatter.format(currentDate));
        		} else {
        			output = dateFormatter.format(currentDate);
        		}
			}

        	System.out.print(output);
        	
        	// If it is the end of the week, new line
        	int daysPrinted = i + previousDays + 1;
        	if (daysPrinted % daysInWeek == 0 && daysPrinted != 0) {
        		System.out.println(); 
        	}
        }
	}
	
	static LocalTime stringToLocalTime(String str) {
		String[] portions = str.split(":");
		return LocalTime.of(
				Integer.parseInt(portions[0]),
				Integer.parseInt(portions[1]));
	}
	
	void loadEvents() {
		File file = new File("events.txt");
		Scanner fin = null;
		try {
			fin = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		if (fin == null) return;
		
		String name = null;
		while (fin.hasNextLine()) {
			String line = fin.nextLine();
			if (name == null) {
				name = line;
			} else {
				String[] portions = line.split(" ");
				if (portions.length != 3) {
					System.out.println("Invalid Date");
					return;
				}

				LocalTime startTime = stringToLocalTime(portions[1]);
				LocalTime endTime = stringToLocalTime(portions[2]);
				TimeInterval interval = new TimeInterval(startTime, endTime);
				
				Pattern pattern = Pattern.compile("(\\d{1,2})/(\\d{1,2})/(\\d{1,4})");
				Matcher matcher = pattern.matcher(portions[0]);
				if (matcher.matches()) {
					LocalDate date = LocalDate.of(
							2000 + Integer.parseInt(matcher.group(3)),
							Integer.parseInt(matcher.group(1)),
							Integer.parseInt(matcher.group(2)));
					calendar.addEvent(new Event(name, date, interval));
				} else {
					calendar.addEvent(new Event(name, Arrays.asList(portions[0].split("")), interval));
				}
				
				name = null;
			}
		}
	}
	
	void printEvents(LocalDate d) {
		for(Event e : calendar.getEvent(d)) {
			System.out.println(e.toString());
		}
	}
	
	void printByDay (LocalDate date) {
		LocalDate currentDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
		Scanner in = new Scanner(System.in);
		String option = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d yyyy");
	
		while (!option.equals("G")) {
			System.out.println(formatter.format(currentDate));
			printEvents(currentDate);
			if (option.equals("N")) {
				currentDate = currentDate.plusDays(1);
			} else if (option.equals("P")) {
				currentDate = currentDate.minusDays(1);
			}
			System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
			option = in.nextLine().toUpperCase();
		}
	}
	
	public static void main(String[] args) {
		MyCalendarTester tester = new MyCalendarTester();
		Scanner keyin = new Scanner(System.in);

		String opt = "";
		String choice = "";
		tester.printCalendar(LocalDate.now());
	
		while (!opt.equals("Q")) {
			printMainMenu();
			opt = keyin.nextLine().toUpperCase();

			if (opt.equals("L")) {
				tester.loadEvents();
				tester.calendar.print();
			} else if (opt.equals("V")) {
				System.out.println("[D]ay view or [M]onth view ? ");
				choice = keyin.nextLine().toUpperCase();
				if (choice.equals("D")) {
					tester.printByDay(LocalDate.now());
				}
			}
		}
	}
}
