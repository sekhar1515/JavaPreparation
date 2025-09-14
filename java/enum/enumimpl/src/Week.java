// Enum is a special class in Java used to represent a fixed set of constants
public enum Week {
    // Enum constants (must be the first items in an enum)
    // Each constant calls the constructor with arguments
    MONDAY("monday", "weekday"),
    TUESDAY("tuesday", "weekday"),
    WEDNESDAY("wednesday", "weekday"),
    THURSDAY("thursday", "weekday"),
    FRIDAY("friday", "weekday"),

    // Enum constants can override methods individually
    SATURDAY("saturday", "weekend") {
        @Override
        public String isHappyDay() {
            return "happy day since it is a weekend";
        }
    },
    SUNDAY("sunday", "weekend") {
        @Override
        public String isHappyDay() {
            return "happy day since it is a weekend";
        }
    };

    // Fields (each constant will store these values)
    private final String day;
    private final String type;

    // Constructor (always private in enums; can’t call it directly)
    Week(String day, String type) {
        this.day = day;
        this.type = type;
    }

    // Getter methods
    public String getDay() {
        return this.day;
    }

    public String getType() {
        return this.type;
    }

    // A default method that can be overridden by specific constants
    public String isHappyDay() {
        return "Not a happy day";
    }
}
