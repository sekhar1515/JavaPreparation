import java.util.ArrayList;
import java.util.List;

public class GeneratePalindromes {

    private List<Long> generatePalindomes(int maxLength) {
        List<Long> result = new ArrayList<>();
        for (int length = 1; length <= maxLength; length++) {
            int start = (int) Math.pow(10, (length - 1) / 2);
            int end = (int) Math.pow(10, (length + 1) / 2);
            for(int half = start; half < end ; half++){
                String halfString = Integer.toString(half);
                String reverese = new StringBuilder(halfString).reverse().toString();
                String output = length% 2 == 0 ? halfString + reverese : halfString + reverese.substring(1);
                result.add(Long.parseLong(output));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        GeneratePalindromes generatePalindromes = new GeneratePalindromes();
        List<Long> palindromes = generatePalindromes.generatePalindomes(3);
        for (Long palindrome : palindromes) {
            System.out.println(palindrome);
        }
    }

}
