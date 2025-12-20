package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static boolean notBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
