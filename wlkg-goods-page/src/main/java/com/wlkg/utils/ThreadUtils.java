package com.wlkg.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: Colin
 * @Date: 2019/11/8 16:01
 * @Description:
 */
public class ThreadUtils {
    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        es.submit(runnable);
    }
}
