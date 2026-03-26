package com.minelittlepony.common.util.registry;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * A holder that allows for proxying to another holder.
 * <p>
 * Implementors should create an instance of this interface and selectively override methods they want to change.
 * @param <T>
 */
public interface ForwardingHolder<T> extends Holder<T> {

    /**
     * Returns a new holder that uses the specified components.
     *
     * @param <T> The wrapped object type
     * @param holder The holder to proxy
     * @param components The components to use
     * @return A new holder that behaves like the provided one but with the given components.
     */
    static <T> Holder<T> withComponents(Holder<T> holder, DataComponentMap components) {
        return new ForwardingHolder<>() {
            @Override
            public Holder<T> holder() {
                return holder;
            }

            @Override
            public boolean areComponentsBound() {
                return true;
            }

            @Override
            public DataComponentMap components() {
                return components;
            }
        };
    }

    Holder<T> holder();

    @Override
    default T value() {
        return holder().value();
    }

    @Override
    default boolean isBound() {
        return holder().isBound();
    }

    @Override
    default boolean areComponentsBound() {
        return holder().areComponentsBound();
    }

    @Override
    default DataComponentMap components() {
        return holder().components();
    }

    @Override
    default boolean is(Identifier key) {
        return holder().is(key);
    }

    @Override
    default boolean is(ResourceKey<T> key) {
        return holder().is(key);
    }

    @Override
    default boolean is(Predicate<ResourceKey<T>> predicate) {
        return holder().is(predicate);
    }

    @Override
    default boolean is(TagKey<T> tag) {
        return holder().is(tag);
    }

    @Deprecated
    @Override
    default boolean is(Holder<T> holder) {
        return holder == this || holder().is(holder);
    }

    @Override
    default Stream<TagKey<T>> tags() {
        return holder().tags();
    }

    @Override
    default Either<ResourceKey<T>, T> unwrap() {
        return holder().unwrap();
    }

    @Override
    default Optional<ResourceKey<T>> unwrapKey() {
        return holder().unwrapKey();
    }

    @Override
    default Kind kind() {
        return holder().kind();
    }

    @Override
    default boolean canSerializeIn(HolderOwner<T> registry) {
        return false;
    }
}
