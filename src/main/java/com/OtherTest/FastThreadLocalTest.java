package com.OtherTest;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class FastThreadLocalTest {
    static FastThreadLocal<String> THREAD_NAME_LOCAL = new FastThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String threadName = "thread-" + i;
            new FastThreadLocalThread(() -> {
                THREAD_NAME_LOCAL.set(threadName);
                System.out.println(THREAD_NAME_LOCAL.get());
            }, threadName).start();
        }
    }
}
