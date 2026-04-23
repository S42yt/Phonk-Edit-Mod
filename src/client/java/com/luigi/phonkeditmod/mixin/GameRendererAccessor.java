package com.luigi.phonkeditmod.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    // setPostProcessor is still private — invoker needed to call it from outside
    @Invoker("setPostProcessor")
    void callSetPostProcessor(Identifier id);

    // clearPostProcessor() is now public in 1.21.11 — call it directly, no invoker needed
    // GameRenderer#clearPostProcessor() replaces the old disablePostProcessor()
}
