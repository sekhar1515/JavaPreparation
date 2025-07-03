import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// A class to encode and decode strings, because life isn’t confusing enough already
public class EncodeAndDecodeStrings {

    // Encodes a list of strings into one neat package 📦
    public static String encode(List<String> words) {
        StringBuilder resultString = new StringBuilder();
        for (String word : words) {
            // Adds word length and '#' because letters love hashtags too 😎
            resultString.append(word.length());
            resultString.append("#");
            resultString.append(word);
        }
        return resultString.toString();
    }

    // Decodes our cryptic package back into a list of words 🕵️‍♂️
    public static List<String> decode(String words) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < words.length()) {
            int j = i;
            // Let's hunt down the sneaky '#'
            while (words.charAt(j) != '#') {
                j++;
            }
            // Found it! Now let's figure out how long our mystery word is 🧩
            int wordLength = Integer.parseInt(words.substring(i, j));
            i = j + 1;
            j = j + wordLength;
            // Decoding the word, one hashtag at a time
            result.add(words.substring(i, j + 1));
            i = j + 1;
        }
        return result;
    }

    public static void main(String[] args) {
        // Let's test this bad boy! (But hey, no crazy UTF-8 emojis please! 🚫😜)
        List<String> words = Arrays.asList("sekhar", "loves", "codingkfvnkns");
        String encodedValue = encode(words);
        System.out.println("Encoded String: " + encodedValue);

        List<String> wordsDecoded = decode(encodedValue);
        System.out.println("Decoded List: " + wordsDecoded);
    }
}
