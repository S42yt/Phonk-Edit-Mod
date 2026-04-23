package com.luigi.phonkeditmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Notificação estilo "toast" do Minecraft (como conquistas)
 * Aparece no canto superior direito e desaparece automaticamente
 */
public class NotificationToast implements Toast {
	private static final Identifier TEXTURE = Identifier.ofVanilla("toast/advancement");
	private static final int DISPLAY_TIME = 6000; // 6 segundos
	private static final Identifier ICON_TEXTURE = Identifier.of("phonk-edit-mod", "icon.png");

	private final Text title;
	private final Text description;
	private long startTime = -1;
	private Visibility visibility = Visibility.SHOW;

	public NotificationToast(String title, String description) {
		this.title = Text.literal(title);
		this.description = Text.literal(description);
	}

	@Override
	public Visibility getVisibility() {
		return this.visibility;
	}

	@Override
	public void update(ToastManager manager, long time) {
		if (this.startTime == -1) {
			this.startTime = time;
		}
		if (time - this.startTime >= DISPLAY_TIME) {
			this.visibility = Visibility.HIDE;
		}
	}

	@Override
	public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
		MinecraftClient mc = MinecraftClient.getInstance();

		// Desenha o fundo (textura de conquista)
		context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, this.getWidth(), this.getHeight());

		// Desenha o título (linha 1)
		context.drawText(mc.textRenderer, this.title, 30, 7, 0xFFFF00, false);

		// Desenha a descrição (linha 2)
		context.drawText(mc.textRenderer, this.description, 30, 18, 0xFFFFFF, false);

		// Desenha um ícone (opcional - usando caveira do mod)
		context.drawTexture(RenderPipelines.GUI_TEXTURED, ICON_TEXTURE, 8, 8, 0f, 0f, 16, 16, 16, 16);
	}

	@Override
	public int getWidth() {
		return 160;
	}

	@Override
	public int getHeight() {
		return 32;
	}

	public static void showAudioLoaded(int count) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getToastManager() != null) {
			String title = "§6Phonk Edit Mod";
			String description = count > 0
				? "§a" + count + " §7audio" + (count > 1 ? "s" : "") + " loaded"
				: "§cNo audio found";
			client.getToastManager().add(new NotificationToast(title, description));
		}
	}

	public static void showImagesLoaded(int count) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getToastManager() != null) {
			String title = "§6Phonk Edit Mod";
			String description = count > 0
				? "§a" + count + " §7image" + (count > 1 ? "s" : "") + " loaded"
				: "§cNo images found";
			client.getToastManager().add(new NotificationToast(title, description));
		}
	}

	public static void showImageErrors(int errorCount) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getToastManager() != null) {
			String title = "§cPhonk Edit Mod";
			String description = "§c" + errorCount + " §7invalid image" + (errorCount > 1 ? "s" : "") + " (PNG only!)";
			client.getToastManager().add(new NotificationToast(title, description));
		}
	}

	public static void showAudioErrors(int errorCount) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getToastManager() != null) {
			String title = "§cPhonk Edit Mod";
			String description = "§c" + errorCount + " §7invalid audio" + (errorCount > 1 ? "s" : "") + " (OGG only!)";
			client.getToastManager().add(new NotificationToast(title, description));
		}
	}

	public static void show(String title, String description) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getToastManager() != null) {
			client.getToastManager().add(new NotificationToast(title, description));
		}
	}
}
