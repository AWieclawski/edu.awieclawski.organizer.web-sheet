package edu.awieclawski.organizer.data.decriptor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cryptor {

    private static final int PRIME_FACTOR = 73;

    public static Integer decryptWord(String word) {
        if (word == null) {
            return null;
        }
        int result = 0;
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            int tmp = Integer.parseInt(String.valueOf(c + i)) * PRIME_FACTOR;
            result = result + tmp;
        }
        return result;
    }

}
