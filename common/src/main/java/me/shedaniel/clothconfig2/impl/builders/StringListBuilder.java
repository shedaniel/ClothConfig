package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
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
public class StringListBuilder extends FieldBuilder<List<String>, StringListListEntry> {
    
    private Function<String, Optional<Component>> cellErrorSupplier;
    private Consumer<List<String>> saveConsumer = null;
    private Function<List<String>, Optional<Component[]>> tooltipSupplier = list -> Optional.empty();
    private final List<String> value;
    private boolean expanded = false;
    private Function<StringListListEntry, StringListListEntry.StringListCell> createNewInstance;
    private Component addTooltip = new TranslatableComponent("text.cloth-config.list.add"), removeTooltip = new TranslatableComponent("text.cloth-config.list.remove");
    private boolean deleteButtonEnabled = true, insertInFront = true;
    
    public StringListBuilder(Component resetButtonKey, Component fieldNameKey, List<String> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }
    
    public Function<String, Optional<Component>> getCellErrorSupplier() {
        return cellErrorSupplier;
    }
    
    public StringListBuilder setCellErrorSupplier(Function<String, Optional<Component>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
        return this;
    }
    
    public StringListBuilder setErrorSupplier(Function<List<String>, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }
    
    public StringListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
        return this;
    }
    
    public StringListBuilder setInsertInFront(boolean insertInFront) {
        this.insertInFront = insertInFront;
        return this;
    }
    
    public StringListBuilder setAddButtonTooltip(Component addTooltip) {
        this.addTooltip = addTooltip;
        return this;
    }
    
    public StringListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        this.removeTooltip = removeTooltip;
        return this;
    }
    
    public StringListBuilder requireRestart() {
        requireRestart(true);
        return this;
    }
    
    public StringListBuilder setCreateNewInstance(Function<StringListListEntry, StringListListEntry.StringListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }
    
    public StringListBuilder setExpanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public StringListBuilder setExpended(boolean expanded) {
        return setExpanded(expanded);
    }
    
    public StringListBuilder setSaveConsumer(Consumer<List<String>> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }
    
    public StringListBuilder setDefaultValue(Supplier<List<String>> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public StringListBuilder setDefaultValue(List<String> defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }
    
    public StringListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = list -> tooltipSupplier.get();
        return this;
    }
    
    public StringListBuilder setTooltipSupplier(Function<List<String>, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public StringListBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = list -> tooltip;
        return this;
    }
    
    public StringListBuilder setTooltip(Component... tooltip) {
        this.tooltipSupplier = list -> Optional.ofNullable(tooltip);
        return this;
    }
    
    @NotNull
    @Override
    public StringListListEntry build() {
        StringListListEntry entry = new StringListListEntry(getFieldNameKey(), value, expanded, null, saveConsumer, defaultValue, getResetButtonKey(), isRequireRestart(), deleteButtonEnabled, insertInFront);
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
