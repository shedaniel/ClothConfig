package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.LongListListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class LongListBuilder extends FieldBuilder<List<Long>, LongListListEntry> {
    
    protected Function<Long, Optional<Component>> cellErrorSupplier;
    private Consumer<List<Long>> saveConsumer = null;
    private Function<List<Long>, Optional<Component[]>> tooltipSupplier = list -> Optional.empty();
    private final List<Long> value;
    private boolean expanded = false;
    private Long min = null, max = null;
    private Function<LongListListEntry, LongListListEntry.LongListCell> createNewInstance;
    private Component addTooltip = new TranslatableComponent("text.cloth-config.list.add"), removeTooltip = new TranslatableComponent("text.cloth-config.list.remove");
    private boolean deleteButtonEnabled = true, insertInFront = true;
    
    public LongListBuilder(Component resetButtonKey, Component fieldNameKey, List<Long> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public Function<Long, Optional<Component>> getCellErrorSupplier() {
        return cellErrorSupplier;
    }
    
    public LongListBuilder setCellErrorSupplier(Function<Long, Optional<Component>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
        return this;
    }
    
    public LongListBuilder setErrorSupplier(Function<List<Long>, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public LongListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
        return this;
    }
    
    public LongListBuilder setInsertInFront(boolean insertInFront) {
        this.insertInFront = insertInFront;
        return this;
    }
    
    public LongListBuilder setAddButtonTooltip(Component addTooltip) {
        this.addTooltip = addTooltip;
        return this;
    }
    
    public LongListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        this.removeTooltip = removeTooltip;
        return this;
    }
    
    public LongListBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public LongListBuilder setCreateNewInstance(Function<LongListListEntry, LongListListEntry.LongListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }
    
    public LongListBuilder setExpanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public LongListBuilder setExpended(boolean expanded) {
        return setExpanded(expanded);
    }
    
    public LongListBuilder setSaveConsumer(Consumer<List<Long>> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public LongListBuilder setDefaultValue(Supplier<List<Long>> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public LongListBuilder setMin(long min) {
        this.min = min;
        return this;
    }
    
    public LongListBuilder setMax(long max) {
        this.max = max;
        return this;
    }
    
    public LongListBuilder removeMin() {
        this.min = null;
        return this;
    }
    
    public LongListBuilder removeMax() {
        this.max = null;
        return this;
    }
    
    public LongListBuilder setDefaultValue(List<Long> defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public LongListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = list -> tooltipSupplier.get();
        return this;
    }
    
    public LongListBuilder setTooltipSupplier(Function<List<Long>, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public LongListBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = list -> tooltip;
        return this;
    }
    
    public LongListBuilder setTooltip(Component... tooltip) {
        this.tooltipSupplier = list -> Optional.ofNullable(tooltip);
        return this;
    }
    
    @NotNull
    @Override
    public LongListListEntry build() {
        LongListListEntry entry = new LongListListEntry(getFieldNameKey(), value, expanded, null, saveConsumer, defaultValue, getResetButtonKey(), isRequireRestart(), deleteButtonEnabled, insertInFront);
        if (min != null)
            entry.setMinimum(min);
        if (max != null)
            entry.setMaximum(max);
        if (createNewInstance != null)
            entry.setCreateNewInstance(createNewInstance);
        entry.setCellErrorSupplier(cellErrorSupplier);
        entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
        entry.setAddTooltip(addTooltip);
        entry.setRemoveTooltip(removeTooltip);
        if (errorSupplier != null)
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        return entry;
    }
    
}
