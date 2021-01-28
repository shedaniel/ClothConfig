package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.LongListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class LongFieldBuilder extends FieldBuilder<Long, LongListEntry> {
    
    private Consumer<Long> saveConsumer = null;
    private Function<Long, Optional<Component[]>> tooltipSupplier = l -> Optional.empty();
    private final long value;
    private Long min = null, max = null;
    
    public LongFieldBuilder(Component resetButtonKey, Component fieldNameKey, long value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public LongFieldBuilder setErrorSupplier(Function<Long, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public LongFieldBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public LongFieldBuilder setSaveConsumer(Consumer<Long> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public LongFieldBuilder setDefaultValue(Supplier<Long> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public LongFieldBuilder setDefaultValue(long defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public LongFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = l -> tooltipSupplier.get();
        return this;
    }
    
    public LongFieldBuilder setTooltipSupplier(Function<Long, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public LongFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = l -> tooltip;
        return this;
    }
    
    public LongFieldBuilder setTooltip(Component... tooltip) {
        this.tooltipSupplier = l -> Optional.ofNullable(tooltip);
        return this;
    }
    
    public LongFieldBuilder setMin(long min) {
        this.min = min;
        return this;
    }
    
    public LongFieldBuilder setMax(long max) {
        this.max = max;
        return this;
    }
    
    public LongFieldBuilder removeMin() {
        this.min = null;
        return this;
    }
    
    public LongFieldBuilder removeMax() {
        this.max = null;
        return this;
    }
    
    @NotNull
    @Override
    public LongListEntry build() {
        LongListEntry entry = new LongListEntry(getFieldNameKey(), value, getResetButtonKey(), defaultValue, saveConsumer, null, isRequireRestart());
        if (min != null)
            entry.setMinimum(min);
        if (max != null)
            entry.setMaximum(max);
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}
