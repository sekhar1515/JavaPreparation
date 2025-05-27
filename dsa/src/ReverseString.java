public class ReverseString {
    /**
     * This method reverses a given string.
     *
     * @param str the string to be reversed
     * @return the reversed string
     */
    public static String reverse(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder reversed = new StringBuilder(str);
        return reversed.reverse().toString();
    }

    /**
     *Other method using chars to reverse a string.
     */
    public static String reverseString(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }
    public static void main(String[] args) {
        String s = "kcndskjnds";
        String reversed = reverse(s);
        System.out.println(reversed);
        String reversed2 = reverseString(s);
        System.out.println(reversed2);
    }
}
