public class CharacterFill {

    public String[] divideString(String s, int k, char fill) {
        int length = s.length();
        int resultLength = (length % k == 0 ? length / k : length / k + 1);
        String[] result = new String[resultLength];
        for (int i = 0; i < result.length; i++) {
            result[i] = "";
        }
        int i = 0;
        for (char ch : s.toCharArray()) {
            result[i / k] += ch;
            i++;
        }
        // Arrays.stream(result).forEach(System.out::println);
        String lastValue = result[resultLength - 1];
        System.out.println(k - lastValue.length() + lastValue);
        for (int j = 0; j < (k - lastValue.length()); j++) {
            System.out.println("fil" + " " + j + " " + (k - lastValue.length()));
            lastValue += fill;
        }
        result[resultLength - 1] = lastValue;
        return result;
    }

    public static void main(String[] args) {
        CharacterFill characterFill = new CharacterFill();
        String[] result = characterFill.divideString("abcdefghij", 3, 'x');
        for (String str : result) {
            System.out.println(str);
        }
    }

}
