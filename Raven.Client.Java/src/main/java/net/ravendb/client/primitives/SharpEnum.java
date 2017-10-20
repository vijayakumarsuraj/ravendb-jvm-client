package net.ravendb.client.primitives;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for inter-language enum conversion
 */
public class SharpEnum {

    public static String value(Enum<?> enumValue) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, enumValue.name());
    }

    public static String[] values(Enum<?>[] enumValues) {
        return Arrays.stream(enumValues).map(x -> value(x)).toArray(String[]::new);
    }
}
