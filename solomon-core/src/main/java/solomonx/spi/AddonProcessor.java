package solomonx.spi;

@FunctionalInterface
public interface AddonProcessor {
    void process(AddonData data);
}
