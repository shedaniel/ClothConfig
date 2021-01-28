package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.clothconfig2.gui.widget.DynamicEntryListWidget;
import me.shedaniel.math.Rectangle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SubCategoryListEntry extends TooltipListEntry<List<AbstractConfigListEntry>> implements Expandable {
    
    private static final ResourceLocation CONFIG_TEX = new ResourceLocation("cloth-config2", "textures/gui/cloth_config.png");
    private final List<AbstractConfigListEntry> entries;
    private final CategoryLabelWidget widget;
    private final List<GuiEventListener> children;
    private boolean expanded;
    
    @Deprecated
    public SubCategoryListEntry(Component categoryName, List<AbstractConfigListEntry> entries, boolean defaultExpanded) {
        super(categoryName, null);
        this.entries = entries;
        this.expanded = defaultExpanded;
        this.widget = new CategoryLabelWidget();
        this.children = Lists.newArrayList(widget);
        this.children.addAll(entries);
        this.setReferenceProviderEntries((List) entries);
    }
    
    @Override
    public boolean isExpanded() {
        return expanded;
    }
    
    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public boolean isRequiresRestart() {
        for (AbstractConfigListEntry entry : entries)
            if (entry.isRequiresRestart())
                return true;
        return false;
    }
    
    @Override
    public void setRequiresRestart(boolean requiresRestart) {
        
    }
    
    public Component getCategoryName() {
        return getFieldName();
    }
    
    @Override
    public List<AbstractConfigListEntry> getValue() {
        return entries;
    }
    
    @Override
    public Optional<List<AbstractConfigListEntry>> getDefaultValue() {
        return Optional.empty();
    }
    
    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Minecraft.getInstance().getTextureManager().bind(CONFIG_TEX);
        Lighting.turnOff();
        RenderSystem.color4f(1, 1, 1, 1);
        blit(matrices, x - 15, y + 5, 24, (widget.rectangle.contains(mouseX, mouseY) ? 18 : 0) + (expanded ? 9 : 0), 9, 9);
        Minecraft.getInstance().font.drawShadow(matrices, getDisplayedFieldName().getVisualOrderText(), x, y + 6, widget.rectangle.contains(mouseX, mouseY) ? 0xffe6fe16 : -1);
        for (AbstractConfigListEntry<?> entry : entries) {
            entry.setParent((DynamicEntryListWidget) getParent());
            entry.setScreen(getConfigScreen());
        }
        if (expanded) {
            int yy = y + 24;
            for (AbstractConfigListEntry<?> entry : entries) {
                entry.render(matrices, -1, yy, x + 14, entryWidth - 14, entry.getItemHeight(), mouseX, mouseY, isHovered && getFocused() == entry, delta);
                yy += entry.getItemHeight();
            }
        }
    }
    
    @Override
    public void updateSelected(boolean isSelected) {
        for (AbstractConfigListEntry<?> entry : entries) {
            entry.updateSelected(expanded && isSelected && getFocused() == entry);
        }
    }
    
    @Override
    public boolean isEdited() {
        for (AbstractConfigListEntry<?> entry : entries) {
            if (entry.isEdited()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void lateRender(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (expanded) {
            for (AbstractConfigListEntry<?> entry : entries) {
                entry.lateRender(matrices, mouseX, mouseY, delta);
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getMorePossibleHeight() {
        if (!expanded) return -1;
        List<Integer> list = new ArrayList<>();
        int i = 24;
        for (AbstractConfigListEntry<?> entry : entries) {
            i += entry.getItemHeight();
            if (entry.getMorePossibleHeight() >= 0) {
                list.add(i + entry.getMorePossibleHeight());
            }
        }
        list.add(i);
        return list.stream().max(Integer::compare).orElse(0) - getItemHeight();
    }
    
    @Override
    public Rectangle getEntryArea(int x, int y, int entryWidth, int entryHeight) {
        widget.rectangle.x = x - 15;
        widget.rectangle.y = y;
        widget.rectangle.width = entryWidth + 15;
        widget.rectangle.height = 24;
        return new Rectangle(getParent().left, y, getParent().right - getParent().left, 20);
    }
    
    @Override
    public int getItemHeight() {
        if (expanded) {
            int i = 24;
            for (AbstractConfigListEntry<?> entry : entries)
                i += entry.getItemHeight();
            return i;
        }
        return 24;
    }
    
    @Override
    public int getInitialReferenceOffset() {
        return 24;
    }
    
    @Override
    public List<? extends GuiEventListener> children() {
        return expanded ? children : Collections.singletonList(widget);
    }
    
    @Override
    public void save() {
        entries.forEach(AbstractConfigListEntry::save);
    }
    
    @Override
    public Optional<Component> getError() {
        Component error = null;
        for (AbstractConfigListEntry<?> entry : entries) {
            Optional<Component> configError = entry.getConfigError();
            if (configError.isPresent()) {
                if (error != null)
                    return Optional.ofNullable(new TranslatableComponent("text.cloth-config.multi_error"));
                return configError;
            }
        }
        return Optional.ofNullable(error);
    }
    
    public class CategoryLabelWidget implements GuiEventListener {
        private final Rectangle rectangle = new Rectangle();
        
        @Override
        public boolean mouseClicked(double double_1, double double_2, int int_1) {
            if (rectangle.contains(double_1, double_2)) {
                expanded = !expanded;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            return false;
        }
    }
    
}
