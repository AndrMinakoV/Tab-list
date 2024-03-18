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

            public TexturedButton(int x, int y, int width, int height, Component message, OnPress onPress, ResourceLocation texture, int u, int v, int textureWidth, int textureHeight) {
                super(x, y, width, height, message, onPress);
                this.texture = texture;
                this.u = u;
                this.v = v;
                this.textureWidth = textureWidth;
                this.textureHeight = textureHeight;
            }

            @Override
            public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.setShaderTexture(0, texture);
                blit(poseStack, this.x, this.y, this.u, this.v, this.width, this.height, textureWidth, textureHeight);
                if (this.isHoveredOrFocused()) {
                    this.renderToolTip(poseStack, mouseX, mouseY);
                }
            }
        }

        public static class CustomTitleScreen extends Screen {
            protected CustomTitleScreen() {
                super(Component.literal("Custom Title Screen"));
            }

            @Override
            protected void init() {
                super.init();
                Minecraft mc = Minecraft.getInstance();
                int buttonWidth = 200;
                int buttonHeight = 20;
                int spacing = 24; // Расстояние между кнопками
                int x = (this.width - buttonWidth) / 2;
                int y = (this.height - buttonHeight * 3 - spacing * 2) / 2;

                // Предполагается, что у вас есть текстуры кнопок
                ResourceLocation playButtonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/play_button.png");
                ResourceLocation settingsButtonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/settings_button.png");
                ResourceLocation forumButtonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/forum_button.png");
                ResourceLocation discordButtonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/discord_button.png");

                TexturedButton playButton = new TexturedButton(x, y, buttonWidth, buttonHeight, Component.literal("Подключиться: [14/260]"), button -> {
                    // Действие при нажатии на кнопку "Play"
                }, playButtonTexture, 0, 0, 347, 80);

                TexturedButton settingsButton = new TexturedButton(x - buttonWidth / 2 - spacing / 2, y + buttonHeight + spacing, buttonWidth / 2, buttonHeight, Component.literal("Настройки"), button -> {
                    // Действие при нажатии на кнопку "Настройки"
                }, settingsButtonTexture, 0, 0, 256, 256);

                TexturedButton forumButton = new TexturedButton(x + buttonWidth / 2 + spacing / 2, y + buttonHeight + spacing, buttonWidth / 2, buttonHeight, Component.literal("Форум"), button -> {
                    // Действие при нажатии на кнопку "Форум"
                }, forumButtonTexture, 0, 0, 256, 256);

                TexturedButton discordButton = new TexturedButton(x, y + buttonHeight * 2 + spacing * 2, buttonWidth, buttonHeight, Component.literal("Дискорд"), button -> {
                    // Действие при нажатии на кнопку "Дискорд"
                }, discordButtonTexture, 0, 0, 256, 256);

                this.addRenderableWidget(playButton);
                this.addRenderableWidget(settingsButton);
                this.addRenderableWidget(forumButton);
                this.addRenderableWidget(discordButton);
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
