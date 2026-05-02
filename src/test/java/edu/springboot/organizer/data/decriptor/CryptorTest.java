package edu.springboot.organizer.data.decriptor;

import edu.springboot.organizer.utils.BaseDateUtils;
import edu.springboot.organizer.utils.BaseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class CryptorTest {

    @Test
    void cratedDifferentNumbersFromGeneratedStringFromDates() {
        List<Integer> testHashIdList = new ArrayList<>();
        List<String> testTimestampIdList = new ArrayList<>();
        for (int i = 0; i < 9999; i++) {
            String baseTimestampId = assignTimestampId(testHashIdList, testTimestampIdList, BaseDateUtils.getBaseTimestampId(), i);
            String counter = getConterAsString(i);
            log.info("{} => assigned baseTimestampId: {}", counter, baseTimestampId);
        }
    }

    private int assignHashId(List<Integer> testIntList, int hashId, int i, String timeStampId) {
        while (testIntList.contains(i)) {
            String counter = getConterAsString(i);
            log.warn("{}. for [{}] - found HashId [{}] is duplicated , but replaced by: {}", counter, timeStampId, hashId, ++hashId);
        }
        return hashId;
    }

    private String assignTimestampId(List<Integer> testIntList, List<String> testStrList, String timeStampId, int i) {
        while (testStrList.contains(timeStampId)) {
            String counter = getConterAsString(i);
            String incrementedTimestampId = BaseStringUtils.replaceLastDigitsIncreasedByOne(timeStampId);
            log.warn("{}. found TimeStampId [{}] is duplicated, but replaced by: {}", counter, timeStampId, incrementedTimestampId);
            timeStampId = incrementedTimestampId;
        }
        int hashId = assignHashId(testIntList, Cryptor.encryptWord(timeStampId), i, timeStampId);
        testIntList.add(hashId);
        testStrList.add(timeStampId);
        return timeStampId;
    }

    private String getConterAsString(int i) {
        return String.format("%04d", i);
    }

}
