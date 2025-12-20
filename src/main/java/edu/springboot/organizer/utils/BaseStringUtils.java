package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseStringUtils {

    public static String replaceLastDigitsIncreasedByOne(String inputString) {
        Dto dto = doReplaceLastDigitsIncreasedByDelta(Dto.builder().inputString(inputString).digitsQuantity(1).build(), 1);
        return inputString.substring(0, inputString.length() - dto.digitsQuantity) + (dto.replacement);
    }

    public static String replaceLastDigitsIncreasedByThree(String inputString) {
        Dto dto = doReplaceLastDigitsIncreasedByDelta(Dto.builder().inputString(inputString).digitsQuantity(1).build(), 3);
        return inputString.substring(0, inputString.length() - dto.digitsQuantity) + (dto.replacement);
    }

    private static Dto doReplaceLastDigitsIncreasedByDelta(Dto dto, int delta) {
        if (dto.inputString == null || dto.inputString.isEmpty()) {
            return dto;
        }
        String lastChar = dto.inputString.substring(dto.inputString.length() - dto.digitsQuantity);
        int deco = (int) Math.pow(10, dto.digitsQuantity);
        Integer lastDigits = parseInt(lastChar);
        if (lastDigits != null) {
            dto.replacement = lastDigits + delta;
            if (dto.replacement > deco - delta) {
                dto.digitsQuantity = dto.digitsQuantity + delta;
                doReplaceLastDigitsIncreasedByDelta(dto, delta);
            }
        }
        return dto;
    }

    private static Integer parseInt(String lastChar) {
        try {
            return Integer.parseInt(lastChar);
        } catch (Exception e) {
            return null;
        }
    }

    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}

@Builder
class Dto {
    String inputString;
    int digitsQuantity;
    int replacement;
}

