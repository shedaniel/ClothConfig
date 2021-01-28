package me.shedaniel.clothconfig2.api;

import me.shedaniel.clothconfig2.impl.ConfigBuilderImpl;
import me.shedaniel.clothconfig2.impl.ConfigEntryBuilderImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface ConfigBuilder {
    
    static ConfigBuilder create() {
        return new ConfigBuilderImpl();
    }
    
    ConfigBuilder setFallbackCategory(ConfigCategory fallbackCategory);
    
    Screen getParentScreen();
    
    ConfigBuilder setParentScreen(Screen parent);
    
    Component getTitle();
    
    ConfigBuilder setTitle(Component title);
    
    boolean isEditable();
    
    ConfigBuilder setEditable(boolean editable);
    
    ConfigCategory getOrCreateCategory(Component categoryKey);
    
    ConfigBuilder removeCategory(Component categoryKey);
    
    ConfigBuilder removeCategoryIfExists(Component categoryKey);
    
    boolean hasCategory(Component category);
    
    ConfigBuilder setShouldTabsSmoothScroll(boolean shouldTabsSmoothScroll);
    
    boolean isTabsSmoothScrolling();
    
    ConfigBuilder setShouldListSmoothScroll(boolean shouldListSmoothScroll);
    
    boolean isListSmoothScrolling();
    
    ConfigBuilder setDoesConfirmSave(boolean confirmSave);
    
    boolean doesConfirmSave();
    
    /**
     * This feature has been removed.
     */
    @Deprecated
    default ConfigBuilder setDoesProcessErrors(boolean processErrors) {
        return this;
    }
    
    /**
     * This feature has been removed.
     */
    @Deprecated
    default boolean doesProcessErrors() {
        return false;
    }
    
    ResourceLocation getDefaultBackgroundTexture();
    
    ConfigBuilder setDefaultBackgroundTexture(ResourceLocation texture);
    
    Runnable getSavingRunnable();
    
    ConfigBuilder setSavingRunnable(Runnable runnable);
    
    Consumer<Screen> getAfterInitConsumer();
    
    ConfigBuilder setAfterInitConsumer(Consumer<Screen> afterInitConsumer);
    
    default ConfigBuilder alwaysShowTabs() {
        return setAlwaysShowTabs(true);
    }
    
    void setGlobalized(boolean globalized);
    
    void setGlobalizedExpanded(boolean globalizedExpanded);
    
    boolean isAlwaysShowTabs();
    
    ConfigBuilder setAlwaysShowTabs(boolean alwaysShowTabs);
    
    ConfigBuilder setTransparentBackground(boolean transparentBackground);
    
    default ConfigBuilder transparentBackground() {
        return setTransparentBackground(true);
    }
    
    default ConfigBuilder solidBackground() {
        return setTransparentBackground(false);
    }
    
    @Deprecated
    default ConfigEntryBuilderImpl getEntryBuilder() {
        return (ConfigEntryBuilderImpl) entryBuilder();
    }
    
    default ConfigEntryBuilder entryBuilder() {
        return ConfigEntryBuilderImpl.create();
    }
    
    Screen build();
    
    boolean hasTransparentBackground();
}
