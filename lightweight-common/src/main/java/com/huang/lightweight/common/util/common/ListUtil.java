package com.huang.lightweight.common.util.common;

import java.util.List;

/**
 * @Author lightweight
 * @Date 2023/5/24 15:56
 */
public class ListUtil {

    /**
     * Checks if the given list is empty.
     *
     * @param list The list to check
     * @return true if the list is empty, false otherwise
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }


}
