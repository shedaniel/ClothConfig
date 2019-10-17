package me.shedaniel.clothconfig2.impl.builders;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SelectorBuilder<T> extends FieldBuilder<T, SelectionListEntry<T>> {
    
    private Consumer<T> saveConsumer = null;
    private Function<T, Optional<String[]>> tooltipSupplier = e -> Optional.empty();
    private T value;
    private T[] valuesArray;
    @Nullable private Function<T, String> nameProvider = null;
    
    public SelectorBuilder(String resetButtonKey, String fieldNameKey, @Nullable T[] valuesArray, @NotNull T value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.valuesArray = valuesArray;
        this.value = value;
    }
    
    public SelectorBuilder<T> setErrorSupplier(Function<T, Optional<String>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public SelectorBuilder<T> requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public SelectorBuilder setSaveConsumer(Consumer<T> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public SelectorBuilder setDefaultValue(Supplier<T> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public SelectorBuilder setDefaultValue(T defaultValue) {
        Objects.requireNonNull(defaultValue);
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public SelectorBuilder setTooltipSupplier(Function<T, Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public SelectorBuilder setTooltipSupplier(Supplier<Optional<String[]>> tooltipSupplier) {
        this.tooltipSupplier = e -> tooltipSupplier.get();
        return this;
    }
    
    public SelectorBuilder setTooltip(Optional<String[]> tooltip) {
        this.tooltipSupplier = e -> tooltip;
        return this;
    }
    
    public SelectorBuilder setTooltip(String... tooltip) {
        this.tooltipSupplier = e -> Optional.ofNullable(tooltip);
        return this;
    }
    
    public SelectorBuilder setNameProvider(@Nullable Function<T, String> enumNameProvider) {
        this.nameProvider = enumNameProvider;
        return this;
    }
    
    @Override
    public SelectionListEntry<T> build() {
        SelectionListEntry<T> entry = new SelectionListEntry<>(getFieldNameKey(), valuesArray, value, getResetButtonKey(), defaultValue, saveConsumer, nameProvider, null, isRequireRestart());
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}