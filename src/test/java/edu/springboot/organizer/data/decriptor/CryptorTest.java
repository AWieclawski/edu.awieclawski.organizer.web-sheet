package edu.springboot.organizer.data.decriptor;

import edu.springboot.organizer.data.utils.BaseDateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CryptorTest {

    @Test
    void cratedDifferentNumbersFromGeneratedStringFromDates() {
        List<Integer> testIntList = new ArrayList<>();
        List<String> testList = new ArrayList<>();
        for (int i = 0; i < 999; i++) {
            String timeStampId = BaseDateUtils.getBaseTimestampId();
            int hashId = assignHashId(testIntList, Cryptor.encryptWord(timeStampId), i);
            Assertions.assertFalse(testList.contains(timeStampId), (i + " - found: " + timeStampId));
            testIntList.add(hashId);
            testList.add(timeStampId);
        }
    }

    private int assignHashId(List<Integer> testIntList, int hashId, int i) {
        try {
            Assertions.assertFalse(testIntList.contains(hashId));
        } catch (Throwable th) {
            System.out.println((i + " - found: " + hashId + " replaced by: " + ++hashId));
            assignHashId(testIntList, hashId, i);
        }
        return hashId;
    }

}