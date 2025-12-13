package edu.springboot.organizer.data.decriptor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cryptor {

    private static final int PRIME_FACTOR = 73;

    public static Integer encryptWord(String word) {
        if (word == null) {
            return 0;
        }
        int result = 0;
        int length = word.length();
        for (int i = 0; i < length; ++i) {
            char c = word.charAt(i);
            int tmp = (int) (Math.pow(Double.parseDouble(String.valueOf(c)), (length - i + 1)) * PRIME_FACTOR);
            result += tmp;
        }
        return result;
    }

}
