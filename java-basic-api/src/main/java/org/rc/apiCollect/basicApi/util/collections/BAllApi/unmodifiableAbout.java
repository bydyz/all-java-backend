package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class unmodifiableAbout {
    // unmodifiableXXX(Collection<T> c)
    // 返回一个不可变（只读）版本的给定集合。任何修改操作都会抛出 UnsupportedOperationException。

    public static void main(String[] args) {
        List<String> original = Arrays.asList("Apple", "Banana");
        List<String> unmodifiable = Collections.unmodifiableList(original);

        try {
            unmodifiable.add("Orange"); // 这行会抛出 UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list.");
        }
    }
}
