package solomonx.spi;

import solomonx.api.Addon;

import java.util.function.Consumer;

public record AddonCustomizer (
    Class<Addon> key,
    Consumer<AddonData> customization
) {}
