package solomonx;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomonx.api.Addon;
import solomonx.api.Context;
import solomonx.spi.AddonCustomizer;
import solomonx.spi.AddonData;
import solomonx.spi.AddonProcessor;
import solomonx.util.Decorators;
import solomonx.util.Observers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class ConfigBuilder {
    protected final List<AddonProcessor> addonProcessors;
    protected final Map<Class<Addon>, AddonData> addonDataMap = new LinkedHashMap<>();

    public ConfigBuilder register(Addon addon) {
        var data = new AddonData(addon);
        for (var processor : this.addonProcessors) {
            processor.process(data);
        }
        return this.registerData(data);
    }

    public ConfigBuilder register(List<Addon> addonList) {
        addonList.forEach(this::register);
        return this;
    }

    public ConfigBuilder registerData(AddonData addonData) {
        var key = addonData.getKey();
        if (this.addonDataMap.containsKey(key)) {
            LOG.warn("Addon addonData with key {} will be overwritten", key);
        }
        this.addonDataMap.put(key, addonData);
        return this;
    }

    public ConfigBuilder registerData(List<AddonData> addonDataList) {
        addonDataList.forEach(this::registerData);
        return this;
    }

    public ConfigBuilder customize(Class<Addon> key, Consumer<AddonData> customization) {
        Optional.ofNullable(this.addonDataMap.get(key))
                .orElseThrow(() -> new IllegalArgumentException("Key is not registered: " + key))
                .customize(customization);
        return this;
    }

    public ConfigBuilder customize(AddonCustomizer customizer) {
        return this.customize(customizer.key(), customizer.customization());
    }

    public ConfigBuilder customize(List<AddonCustomizer> customizerList) {
        customizerList.forEach(this::customize);
        return this;
    }

    public ConfigBuilder decorateBefore(Consumer<Context<Object, Object>> handler) {
        return this.registerAndApplyAnonymous(Decorators.before(handler));
    }

    public ConfigBuilder decorateAfter(Consumer<Context<Object, Object>> handler) {
        return this.registerAndApplyAnonymous(Decorators.after(handler));
    }

    public ConfigBuilder onSuccess(BiConsumer<Object, Object> handler) {
        return this.registerAndApplyAnonymous(Observers.onSuccess(handler));
    }

    public ConfigBuilder onFailure(BiConsumer<Object, RuntimeException> handler) {
        return this.registerAndApplyAnonymous(Observers.onFailure(handler));
    }

    private ConfigBuilder registerAndApplyAnonymous(Addon addon) {
        var data = new AddonData(addon).customize(AddonData::apply);
        return this.registerData(data);
    }
}
