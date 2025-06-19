package edu.springboot.organizer.data.utils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseStringUtils {

    public static String replaceLastDigitsIncreasedByOne(String inputString) {

        Dto dto = doReplaceLastDigitsIncreasedByOne(Dto.builder().inputString(inputString).digitsQuantity(1).build());
        return inputString.substring(0, inputString.length() - dto.digitsQuantity) + (dto.replacement);
    }

    private static Dto doReplaceLastDigitsIncreasedByOne(Dto dto) {
        if (dto.inputString == null || dto.inputString.isEmpty()) {
            return dto;
        }
        String lastChar = dto.inputString.substring(dto.inputString.length() - dto.digitsQuantity);
        int deco = (int) Math.pow(10, dto.digitsQuantity);
        Integer lastDigits = parseInt(lastChar);
        if (lastDigits != null) {
            dto.replacement = lastDigits + 1;
            if (dto.replacement > deco - 1) {
                dto.digitsQuantity = dto.digitsQuantity + 1;
                doReplaceLastDigitsIncreasedByOne(dto);
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

