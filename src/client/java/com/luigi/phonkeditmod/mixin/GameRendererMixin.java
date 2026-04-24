package com.luigi.phonkeditmod.mixin;

import com.luigi.phonkeditmod.PhonkEditModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Unique
    private final Pool phonkPool = new Pool(4);

    @Inject(method = "bobView", at = @At("TAIL"))
    private void applyShakeEffect(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (PhonkEditModClient.shouldApplyCameraEffects()) {
            float intensity = PhonkEditModClient.getShakeIntensity();

            float shakeX = (float) (Math.random() - 0.5) * intensity;
            float shakeY = (float) (Math.random() - 0.5) * intensity;
            float shakeZ = (float) (Math.random() - 0.5) * intensity;

            matrices.translate(shakeX, shakeY, shakeZ);
        }
    }

    // getFov now returns float in 1.21.11 (was double before)
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void applyZoomEffect(CallbackInfoReturnable<Float> cir) {
        if (PhonkEditModClient.shouldApplyCameraEffects()) {
            float zoom = PhonkEditModClient.getZoomLevel();
            float fov = cir.getReturnValue();
            cir.setReturnValue(fov / zoom);
        }
    }

    // Apply the full-screen grayscale/blur shader AFTER all GUI (world + HUD + overlays)
    // has been submitted to the framebuffer, so the effect covers everything.
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            shift = At.Shift.AFTER
        )
    )
    private void applyPhonkFullScreenShader(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        Identifier shaderId = PhonkEditModClient.getActiveShaderEffect();
        if (shaderId == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        try {
            PostEffectProcessor processor = mc.getShaderLoader()
                    .loadPostEffect(shaderId, DefaultFramebufferSet.MAIN_ONLY);
            if (processor != null) {
                processor.render(mc.getFramebuffer(), phonkPool);
            }
        } catch (Exception e) {
            System.err.println("[Phonk Edit Mod] Shader error: " + e.getMessage());
        }
    }
}
