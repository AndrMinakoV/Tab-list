package com.example.examplemod;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
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

import static net.minecraft.client.gui.GuiComponent.blit;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientModEvents.class);
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/gui/title/background.png");

        @SubscribeEvent
        public static void onTitleScreenInit(ScreenEvent.Init event) {
            if (!(event.getScreen() instanceof TitleScreen)) return;

            Minecraft mc = Minecraft.getInstance();
            int buttonWidth = 200;
            int buttonHeight = 20;
            int yPos = 4 * mc.getWindow().getGuiScaledHeight() / 5;
            int x = mc.getWindow().getGuiScaledWidth() / 2 - buttonWidth / 2;

            Button forumButton = new Button(x, yPos, buttonWidth, buttonHeight, Component.literal("Forum"), button -> {
                // TODO: Добавьте действие при нажатии
                LOGGER.info("Forum button pressed.");
            });

            Button discordButton = new Button(x, yPos + 24, buttonWidth, buttonHeight, Component.literal("Discord"), button -> {
                // TODO: Добавьте действие при нажатии
                LOGGER.info("Discord button pressed.");
            });

            event.addListener(forumButton);
            event.addListener(discordButton);
        }

        @SubscribeEvent
        public static void onTitleScreenBackgroundRender(ScreenEvent.Render event) {
            if (event.getScreen() instanceof TitleScreen) {
                renderBackgroundTexture(event.getPoseStack());
            }
        }

        @SubscribeEvent
        public static void onTitleScreenRender(ScreenEvent.Render event) {
            // Здесь ничего не делаем, чтобы не блокировать рендеринг стандартного интерфейса
        }



        public static void renderBackgroundTexture(PoseStack poseStack) {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
            blit(poseStack, 0, 0, 0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), 1920, 1080);
        }

    }
}
