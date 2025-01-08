package org.example;

import java.util.Map;

public class MyClass {

    private final Map<String, ThreadLocal<Integer>> threadLocalMap;

    MyClass(final Map<String, ThreadLocal<Integer>> threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }

    int usingTryWithResources() {
        final ThreadLocal<Integer> threadLocal = threadLocalMap.get("Value1");
        try (final AutoClosableResource myAutoClosable = new AutoClosableResource()) {
            return threadLocal.get();
        } finally {
            threadLocal.remove();
        }
    }

    int explicitlyClosingResource() {
        final AutoClosableResource myAutoClosable = new AutoClosableResource();

        try {
            return threadLocalMap.get("Value1").get();
        } finally {
            threadLocalMap.get("Value1").remove();
            myAutoClosable.close();
        }
    }
}
