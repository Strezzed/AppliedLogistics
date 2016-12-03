package tech.flatstone.appliedlogistics.common.sounds;

import net.minecraft.util.ResourceLocation;

public class SoundBase {
    protected String resourcePath = "";
    protected String internalName = "";

    public SoundBase(String resourcePath) {
        this.resourcePath = resourcePath;
        this.internalName = resourcePath;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}