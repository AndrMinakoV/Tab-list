package com.example.examplemod;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.screens.ConnectScreen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
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


import javax.swing.*;
import java.net.URI;

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
            public final Screen previous = new TitleScreen();

            protected CustomTitleScreen() {
                super(Component.literal("Custom Title Screen"));
            }
            @Override
            protected void init() {
                super.init();
                int buttonWidthLarge = 200; // Ширина для "Играть" и "Дискорд"
                int buttonHeight = 20;
                int buttonSpacing = 10; // Пространство между маленькими кнопками
                int smallButtonWidth = (buttonWidthLarge - buttonSpacing) / 2; // Ширина для "Настройки" и "Форум"
                String logo = "Ваш текст здесь"; // Текст, который вы хотите отобразить

                int centerX = this.width / 2;
                int centerY = this.height /2 + 50; // Центрируем кнопки по вертикали

                int topButtonY = centerY - buttonHeight * 2 - buttonSpacing; // Y для кнопки "Играть"
                int middleButtonY = centerY - buttonHeight / 2; // Y для кнопок "Настройки" и "Форум"
                int bottomButtonY = centerY + buttonHeight + buttonSpacing; // Y для "Дискорд"
                // Кнопка "Играть"
                addTexturedButton(centerX - buttonWidthLarge / 2, topButtonY, "play_button.png", "Играть: [14/260]", button -> {
                    Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(this));
                });


                // Кнопки "Настройки" и "Форум"
                int settingsX = centerX - buttonWidthLarge / 2; // Начало слева от центра
                int forumX = settingsX + smallButtonWidth + buttonSpacing; // Правее "Настройки"
// Кнопка "Настройки"
                addTexturedButton(settingsX, middleButtonY, "settings_button.png", "Настройки", (button) -> {
                    Minecraft.getInstance().setScreen(new OptionsScreen(this, Minecraft.getInstance().options));
                });

                addTexturedButton(forumX, middleButtonY, "forum_button.png", "Форум", button -> {
                    Util.getPlatform().openUri(URI.create("https://pornhub.com"));
                });

                // Кнопка "Дискорд"
// Кнопка "Дискорд"
                addTexturedButton(centerX - buttonWidthLarge / 2, bottomButtonY, "discord_button.png", "Дискорд", button -> {
                    Util.getPlatform().openUri(URI.create("https://discord.gg/mcskill"));
                });



            }



            private void addTexturedButton(int x, int y, String textureName, String buttonText, Button.OnPress onPress) {
                // Подставляем ширину текстуры кнопки согласно её размеру
                int textureWidth = buttonText.equals("Настройки") || buttonText.equals("Форум") ? 95 : 200; // 100 для маленьких, 200 для большой кнопки
                int textureHeight = 20;
                ResourceLocation buttonTexture = new ResourceLocation(ExampleMod.MODID, "textures/gui/buttons/" + textureName);

                TexturedButton button = new TexturedButton(
                        x, y, textureWidth, textureHeight, Component.literal(buttonText),
                        onPress, // Использование переданного параметра 'onPress'
                        buttonTexture, 0, 0, textureWidth, textureHeight, textureWidth
                );
                this.addRenderableWidget(button);

            }





            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(poseStack);
                super.render(poseStack, mouseX, mouseY, partialTicks); // Этот вызов отрисует кнопки и другие элементы интерфейса\
                String text1 = "123";
                String text2 = "456";
                int x1 = 1000; // Горизонтальная позиция для первой части текста
                int y = 500; // Вертикальная позиция текста
                int color1 = 0x64EF86; // Цвет первой части текста
                int color2 = 0x612FF2; // Цвет второй части текста

                // Отрисовка первой части текста
                drawString(poseStack, this.font, text1, x1, y, color1);

                // Рассчитываем ширину первой части текста
                int widthOfFirstText = this.font.width(text1);

                // Отрисовка второй части текста сразу после первой
                int x2 = x1 + widthOfFirstText; // Горизонтальная позиция для второй части текста
                drawString(poseStack, this.font, text2, x2, y, color2);
            }

            @Override
            public void renderBackground(PoseStack poseStack) {
                RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
                blit(poseStack, 0, 0, this.width, this.height, 0, 0, 1792, 1024, 1792, 1024);
            }
        }
    }
}
