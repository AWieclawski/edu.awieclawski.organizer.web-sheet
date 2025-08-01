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
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            int tmp = (int) (Math.pow(Double.parseDouble(String.valueOf(c + i)), 3) * PRIME_FACTOR);
            result = result + tmp;
        }
        return result;
    }

}
