package com.luigi.phonkeditmod.mixin;

import com.luigi.phonkeditmod.PhonkEditModClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

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
}
