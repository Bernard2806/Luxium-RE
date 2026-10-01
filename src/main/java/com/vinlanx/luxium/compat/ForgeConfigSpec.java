package com.vinlanx.luxium.compat;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Small source-compatibility facade for Luxium's existing Forge config declarations.
 * The backing config and registered spec are NeoForge's ModConfigSpec.
 */
public final class ForgeConfigSpec {
    private final ModConfigSpec delegate;

    private ForgeConfigSpec(ModConfigSpec delegate) {
        this.delegate = delegate;
    }

    public ModConfigSpec toNeoForgeSpec() {
        return this.delegate;
    }

    public static final class Builder {
        private final ModConfigSpec.Builder delegate = new ModConfigSpec.Builder();

        public Builder comment(String... comments) {
            this.delegate.comment(comments);
            return this;
        }

        public Builder push(String path) {
            this.delegate.push(path);
            return this;
        }

        public Builder pop() {
            this.delegate.pop();
            return this;
        }

        public BooleanValue define(String path, boolean defaultValue) {
            return new BooleanValue(this.delegate.define(path, defaultValue));
        }

        public IntValue defineInRange(String path, int defaultValue, int minimum, int maximum) {
            return new IntValue(this.delegate.defineInRange(path, defaultValue, minimum, maximum));
        }

        public DoubleValue defineInRange(String path, double defaultValue, double minimum, double maximum) {
            return new DoubleValue(this.delegate.defineInRange(path, defaultValue, minimum, maximum));
        }

        public <E extends Enum<E>> EnumValue<E> defineEnum(String path, E defaultValue) {
            return new EnumValue<>(this.delegate.defineEnum(path, defaultValue));
        }

        public <T> Pair<T, ForgeConfigSpec> configure(Function<Builder, T> factory) {
            T config = factory.apply(this);
            return Pair.of(config, new ForgeConfigSpec(this.delegate.build()));
        }
    }

    public static class ConfigValue<T> {
        private final ModConfigSpec.ConfigValue<T> delegate;

        private ConfigValue(ModConfigSpec.ConfigValue<T> delegate) {
            this.delegate = delegate;
        }

        public T get() {
            return this.delegate.get();
        }

        public T getDefault() {
            return this.delegate.getDefault();
        }

        public void set(T value) {
            this.delegate.set(value);
        }

        public void save() {
            this.delegate.save();
        }
    }

    public static final class BooleanValue extends ConfigValue<Boolean> {
        private BooleanValue(ModConfigSpec.BooleanValue delegate) {
            super(delegate);
        }
    }

    public static final class IntValue extends ConfigValue<Integer> {
        private IntValue(ModConfigSpec.IntValue delegate) {
            super(delegate);
        }
    }

    public static final class DoubleValue extends ConfigValue<Double> {
        private DoubleValue(ModConfigSpec.DoubleValue delegate) {
            super(delegate);
        }
    }

    public static final class EnumValue<E extends Enum<E>> extends ConfigValue<E> {
        private EnumValue(ModConfigSpec.EnumValue<E> delegate) {
            super(delegate);
        }
    }
}
