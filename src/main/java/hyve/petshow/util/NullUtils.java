package hyve.petshow.util;

import java.util.Objects;
import java.util.function.Predicate;

public class NullUtils {
    private static <T> Predicate<T> isNull(){
        return Objects::isNull;
    }

    public static Boolean isNotNull(Object object){
        var predicate = NullUtils.isNull();

        return ! predicate.test(object);
    }
}
