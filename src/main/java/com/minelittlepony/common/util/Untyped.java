package com.minelittlepony.common.util;

public interface Untyped {
    /**
     * Performs an unchecked type cast.
     * <p>
     * Will throw a type cast exception if the types are not compatible.
     */
    @SuppressWarnings("unchecked")
    static <A, B> B cast(A a) {
        return (B)a;
    }
}
