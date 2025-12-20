package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * some useful tools fo java 8
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> coll) {
        return (coll == null || coll.isEmpty());
    }

    public static <T> boolean isNotEmpty(Collection<T> coll) {
        return !CollectionUtils.isEmpty(coll);
    }

}
