import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Employee {
    private final int id;
    private final String name;
    private final String dept;
    private final String city;
    private final int age;
    private final BigDecimal salary;
    private final boolean active;
    private final LocalDate joined;
    private final Integer managerId; // nullable
    private final Set<String> skills;

    public Employee(int id, String name, String dept, String city, int age,
                    BigDecimal salary, boolean active, LocalDate joined, Integer managerId, Set<String> skills) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.city = city;
        this.age = age;
        this.salary = salary;
        this.active = active;
        this.joined = joined;
        this.managerId = managerId;
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getJoined() {
        return joined;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public Set<String> getSkills() {
        return skills;
    }

    @Override
    public String toString() {
        return "Emp{" + id + "," + name + "," + dept + "," + city + ",₹" + salary + "}";
    }
}

public class Streams {
    static List<Employee> sample() {
        return List.of(
                new Employee(1, "Asha", "Eng", "Hyderabad", 28, new BigDecimal("18.5"), true, LocalDate.of(2022, 3, 1), 10, Set.of("Java", "Spring", "SQL")),
                new Employee(2, "Vikram", "Sales", "Mumbai", 34, new BigDecimal("22.0"), true, LocalDate.of(2021, 7, 15), 11, Set.of("Negotiation", "Excel")),
                new Employee(3, "Meera", "Eng", "Bengaluru", 31, new BigDecimal("27.0"), true, LocalDate.of(2020, 11, 20), 10, Set.of("Java", "K8s", "Docker")),
                new Employee(4, "Ravi", "HR", "Hyderabad", 29, new BigDecimal("14.0"), false, LocalDate.of(2023, 1, 5), 12, Set.of("PeopleOps", "Excel")),
                new Employee(5, "Noah", "Eng", "Hyderabad", 26, new BigDecimal("16.0"), true, LocalDate.of(2024, 6, 1), 10, Set.of("Python", "SQL")),
                new Employee(6, "Sara", "Sales", "Mumbai", 38, new BigDecimal("30.0"), true, LocalDate.of(2019, 9, 1), 11, Set.of("Negotiation", "CRM")),
                new Employee(7, "Isha", "Eng", "Bengaluru", 33, new BigDecimal("25.0"), true, LocalDate.of(2021, 2, 14), 10, Set.of("Java", "AWS")),
                new Employee(8, "Kiran", "Finance", "Mumbai", 41, new BigDecimal("35.0"), true, LocalDate.of(2018, 4, 1), 13, Set.of("Accounting", "Excel")),
                new Employee(9, "Rohit", "Eng", "Hyderabad", 28, new BigDecimal("19.0"), false, LocalDate.of(2022, 5, 10), 10, Set.of("Python", "Spark")),
                new Employee(10, "Lea", "HR", "Bengaluru", 30, new BigDecimal("15.5"), true, LocalDate.of(2020, 8, 21), 12, Set.of("PeopleOps", "Hiring"))
        );
    }

    public static void main(String[] args) {
        List<Employee> employeeList = sample();

        List<String> inputValues = List.of("aa", "bbbb", "ccc", "dddddd");
//        Top N by comparator
        List<String> topNValues = inputValues.stream()
                .sorted(Comparator.comparing(String::length).reversed()).
                limit(2).toList();
        topNValues.forEach(System.out::println);

//        Word count (frequency map)
        String input = "a b c d e a a b";
        Map<String, Long> mapCount = Arrays.stream(input.split("\\s+")).
                collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        mapCount.forEach((x, y) -> System.out.println(x + " " + y));

//        Given List<String> words, produce a list of words in uppercase with length ≥ 4,
//        sorted alphabetically, without duplicates.
        List<String> words = List.of("hi", "hello", "world", "mate");
        List<String> modifiedWords = words.
                stream().filter(x -> x.length() >= 4).
                map(String::toUpperCase).
                sorted().distinct().toList();
        modifiedWords.forEach(System.out::println);

//        From int[] arr, compute the sum of squares of even numbers.
        int[] arr = {1, 2, 3, 4, 5};
        long sum = Arrays.stream(arr).filter(x -> x % 2 == 0).map(x -> x * x).sum();

        // 1) Basic grouping: dept -> List<Employee>
        Map<String, List<Employee>> employeesByDept = employeeList.stream().
                collect(Collectors.groupingBy(Employee::getDept));
        employeesByDept.forEach((x, y) -> {
            System.out.print("Dept : " + x);
            y.forEach(System.out::print);
            System.out.println();
        });

        // 2) Count per department
        Map<String, Long> employeeCountByDept = employeeList.stream().collect
                (Collectors.groupingBy(Employee::getDept, Collectors.counting()));
        employeeCountByDept.forEach((x, y) -> {
            System.out.println("count of employees for department " + x + " is " + y);
        });

        // 3) Sum salary per department (BigDecimal with reducing)
        Map<String, BigDecimal> employeeSalaryByDept =
                employeeList.stream().collect(Collectors.groupingBy(Employee::getDept, Collectors.reducing(BigDecimal.ZERO, Employee::getSalary, BigDecimal::add)));
        employeeSalaryByDept.forEach((x, y) -> {
            System.out.println("Employee salary for dept : " + x + " is " + y);
        });

        Map<String, Map<String, List<String>>> employeesFromCityAndDept =
                employeeList.stream().collect(Collectors.groupingBy(Employee::getCity,
                        Collectors.groupingBy(Employee::getDept,
                                Collectors.mapping(Employee::getName, Collectors.toList()))));
        System.out.println(employeesFromCityAndDept);
        // 6) Distinct skills per department (Java 9+: flatMapping)
        // If you're on Java 8, collect to List<Set<String>> then flatten afterward.
        Map<String, Set<String>> employeesWithDistinctSkills =
                employeeList.stream().collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.flatMapping(x -> x.getSkills().stream(), Collectors.toSet())));
        System.out.println("6) skillsByDept = " + employeesWithDistinctSkills);

//        Top 3 earning members in dept
        Map<String, List<String>> employeesTop3Paid =
                employeeList.stream().collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.collectingAndThen(Collectors.toList(), list ->
                                list.stream().
                                        sorted(Comparator.comparing(Employee::getSalary, Comparator.reverseOrder())).
                                        map(Employee::getName).
                                        limit(3).
                                        toList()
                        )));
        System.out.println("Top 3 paid employees " + employeesTop3Paid);
    }
}
