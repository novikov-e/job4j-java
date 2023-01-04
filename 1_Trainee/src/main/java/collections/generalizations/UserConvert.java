package collections.generalizations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class UserConvert.
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1
 * @since 1
 */
public class UserConvert {
    /**
     * The method converts the list to map.
     * @param list - list;
     * @return - map.
     */
    public Map<Integer, User> convert(List<User> list) {
        return list.stream().collect(Collectors.toMap(User :: getId, Function.identity()));
    }
}
