package collections.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamsExampleDemo {
    public static void main(String[] args) {
        List<String> input = List.of("Hi", "hello", "render", "rocks", "hi");
        List<String> trasnformedList = input.stream().
                filter(x -> x.length() > 3).
                map(String::toUpperCase).toList();
        trasnformedList.forEach(System.out::println);
        List<Name> namesList = List.of(new Name("Ramesh", "tendulkar"), new Name("sachin", "god"));
        List<String> firstNamesList = namesList.stream().
                map(Name::getFirstName).
                filter(firstName -> firstName.startsWith("s"))
                .toList();

        firstNamesList.forEach(System.out::print);

        String s = "a b c d a c d e";
        Map<String, Long> characterIntegerMap = Arrays.stream(s.split("\\s+")).collect
                (Collectors.groupingBy(x -> x, Collectors.counting()));
    }
}

class Name {
    String firstName;
    String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
