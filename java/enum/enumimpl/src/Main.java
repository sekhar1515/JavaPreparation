public class Main {
    public static void main(String[] args) {
        // Loop through all days
        for (Week week : Week.values()) {
            System.out.println(week.ordinal() + " " + week.getDay()
                    + " " + week.getType());
        }

        // Direct access
        Week sundayWeek = Week.SUNDAY;
        System.out.println(sundayWeek.isHappyDay()); // overridden
        System.out.println(sundayWeek.name());       // "SUNDAY"

        // valueOf
        Week friday = Week.valueOf("FRIDAY");
        System.out.println(friday.getDay()); // friday

        // switch
        switch (friday) {
            case SATURDAY, SUNDAY -> System.out.println("Hurray weekend");
            case FRIDAY -> System.out.println("Almost weekend!");
            default -> System.out.println("Just another day...");
        }
    }
}
