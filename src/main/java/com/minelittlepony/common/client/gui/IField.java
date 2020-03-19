package com.minelittlepony.common.client.gui;

import javax.annotation.Nonnull;

/**
 * Represents an input field that holds a value.
 *
 * @author     Sollace
 *
 * @param <T> The type of value this field contains.
 * @param <V> The implementor of this class, used for chaining.
 */
public interface IField<T, V extends IField<T, V>> {

    /**
     * Registered an on-change event, fired when the value of this field is changed.
     *
     * @see IField.IChangeCallback<T>
     * @param action The callback function.
     *
     * @return {@code this} for chaining purposes.
     */
    V onChange(@Nonnull IChangeCallback<T> action);

    /**
     * Sets the value of this field.
     *
     * @param value The value.
     *
     * @return {@code this} for chaining purposes.
     */
    V setValue(T value);

    T getValue();

    /**
     * A callback listener for when a IField's value changes.
     *
     * @param <T> The type of the value.
     */
    @FunctionalInterface
    public interface IChangeCallback<T> {

        /**
         * The empty change listener. Does nothing and keeps the initial value it was given.
         *
         * @param <T>
         * @param t
         * @return
         */
        static <T> T none(T t) {
            return t;
        }

        /**
         * Performs this action now.
         *
         * @param  value New Value of the field being changed
         * @return       Adjusted value the field must take on
         */
        T perform(T value);
    }
}
