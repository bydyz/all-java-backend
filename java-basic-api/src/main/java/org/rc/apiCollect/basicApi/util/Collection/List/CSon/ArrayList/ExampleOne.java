package org.rc.apiCollect.basicApi.util.Collection.List.CSon.ArrayList;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExampleOne {
    @Test
    public void testListRemove() {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        updateList(list);
        System.out.println(list);//[1,2]
    }
    private static void updateList(List list) {
        // 此处是通过 index 来remove
        list.remove(2);
    }
}
