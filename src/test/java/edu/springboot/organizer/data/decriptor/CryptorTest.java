package edu.springboot.organizer.data.decriptor;

import edu.springboot.organizer.utils.BaseDateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CryptorTest {

    @Test
    void cratedDifferentNumbersFromGeneratedStringFromDates() {
        List<Integer> testIntList = new ArrayList<>();
        List<String> testStrList = new ArrayList<>();
        for (int i = 0; i < 9999; i++) {
            String timeStampId = BaseDateUtils.getBaseTimestampId();
            int hashId = assignHashId(testIntList, Cryptor.encryptWord(timeStampId), i, timeStampId);
            Assertions.assertFalse(testStrList.contains(timeStampId), (String.format("%4d - found: %s", i, timeStampId)));
            testIntList.add(hashId);
            testStrList.add(timeStampId);
        }
    }

    private int assignHashId(List<Integer> testIntList, int hashId, int i, String timeStampId) {
        try {
            Assertions.assertFalse(testIntList.contains(hashId));
        } catch (Throwable th) {
            String counter = String.format("%3d", i);
            System.out.println((counter + "[" + timeStampId + "] - found: " + hashId + " replaced by: " + ++hashId));
            assignHashId(testIntList, hashId, i, timeStampId);
        }
        return hashId;
    }

}
