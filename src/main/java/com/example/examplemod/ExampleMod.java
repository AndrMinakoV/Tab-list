package com.example.examplemod;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.client.gui.GuiComponent;

import javax.swing.*;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientModEvents());
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/gui/title/background.png");

        @SubscribeEvent
        public static void onScreenOpening(ScreenEvent.Opening event) {
            if (event.getScreen() instanceof TitleScreen) {
                event.setNewScreen(new CustomTitleScreen());
            }
        }
        public static class TexturedButton extends Button {
            private final ResourceLocation texture;
            private final int u;
            private final int v;
            private final int textureWidth;
            private final int textureHeight;
            private final int uHover;

            public TexturedButton(int x, int y, int width, int height, Component message, OnPress onPress, ResourceLocation texture, int u, int v, int textureWidth, int textureHeight, int uHover) {
                super(x, y, width, height, message, onPress);
                this.texture = texture;
                this.u = u;
                this.v = v;
                this.textureWidth = textureWidth;
                this.textureHeight = textureHeight;
                this.uHover = uHover;
            }

            @Override
            public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.setShaderTexture(0, texture);
                int uOffset = this.isHoveredOrFocused() ? uHover : u;
                blit(poseStack, this.x, this.y, uOffset, v, this.width, this.height, textureWidth, textureHeight);

                // Отрисовываем текст по центру кнопки
                drawCenteredString(poseStack, mc.font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0x76b5c5);
            }

        }

        public static class CustomTitleScreen extends Screen {
            protected CustomTitleScreen() {
                super(Component.literal("Custom Title Screen"));
            }

            @Override
            protected void init() {
                super.init();
                int buttonWidth = 200; // Должен соответствовать ширине текстуры
                int buttonHeight = 20; // Должен соответствовать высоте текстуры
                int y = this.height / 4;

                addTexturedButton(this.width / 2 - buttonWidth / 2, y, "play_button.png", "Подключиться: [14/260]");
                addTexturedButton(this.width / 4 - buttonWidth / 4, y + buttonHeight * 2, "settings_button.png", "Настройки");
                addTexturedButton(this.width / 4 * 3 - buttonWidth / 4, y + buttonHeight * 2, "forum_button.png", "Форум");
                addTexturedButton(this.width / 2 - buttonWidth / 2, y + buttonHeight * 4, "discord_button.png", "Дискорд");
            }

            private void addTexturedButton(int x, int y, String textureName, String buttonText) {
                ResourceLocation buttonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/" + textureName);
                int textureWidth = 200; // Ширина текстуры в пикселях.
                int textureHeight = 40; // Высота текстуры в пикселях.
                int u = 0; // Начальная координата U для нормального состояния.
                int v = 0; // Начальная координата V.
                int uHover = textureWidth; // Предполагаем, что текстура наведения находится справа от обычного состояния.

                TexturedButton texturedButton = new TexturedButton(
                        x, y, textureWidth, textureHeight, Component.literal(buttonText),
                        (button) -> {
                            // Обработка нажатия кнопки
                        },
                        buttonTexture, u, v, textureWidth, textureHeight, uHover
                );
                this.addRenderableWidget(texturedButton);
            }




            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(poseStack);
                super.render(poseStack, mouseX, mouseY, partialTicks); // Этот вызов отрисует кнопки и другие элементы интерфейса
            }

            @Override
            public void renderBackground(PoseStack poseStack) {
                RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
                blit(poseStack, 0, 0, this.width, this.height, 0, 0, 1792, 1024, 1792, 1024);
            }
        }
    }
}
