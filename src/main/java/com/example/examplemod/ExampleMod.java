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
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientModEvents.class);
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/gui/title/background.png");

        // Слушатель для добавления кнопок на экране заголовка.

        public static class ExampleButton extends Button {
            public ExampleButton(int x, int y, int width, int height, Component message, OnPress onPress) {
                super(x, y, width, height, message, onPress);
            }

            @Override
            public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                super.renderButton(poseStack, mouseX, mouseY, partialTicks);
                // Тут ваш код для рендеринга фона кнопки, если он вам нужен
                // Например, вы можете нарисовать цветной прямоугольник или использовать свою текстуру
            }
        }

        @SubscribeEvent
        public static void onTitleScreenInit(ScreenEvent.Init event) {
            if (!(event.getScreen() instanceof TitleScreen)) return;
            event.getScreen().children().removeIf(e ->
                    e instanceof Button && (
                            ((Button) e).getMessage().getString().equals("Mods") ||
                                    ((Button) e).getMessage().getString().equals("Выход")
                    )
            );
            Minecraft mc = Minecraft.getInstance();
            int buttonWidth = 100;
            int buttonHeight = 20;
            int y = mc.getWindow().getGuiScaledHeight() / 4 + 120;
            int x = mc.getWindow().getGuiScaledWidth() / 2 - buttonWidth - 5; // Изменено здесь

            Button forumButton = new ExampleButton(x, y, buttonWidth, buttonHeight, Component.literal("Форум"), button -> {
                // Действие при нажатии на кнопку "Форум"
            });

            // Расчёт x для discordButton с учётом отступа
            int discordButtonX = x + buttonWidth + 10;

            Button discordButton = new ExampleButton(discordButtonX, y, buttonWidth, buttonHeight, Component.literal("Дискорд"), button -> {
                // Действие при нажатии на кнопку "Дискорд"
            });

            event.addListener(forumButton);
            event.addListener(discordButton);
        }



        /* // Слушатель для рендеринга фона перед интерфейсом.
        @SubscribeEvent
        public static void onScreenOpening(ScreenEvent.Opening event) {
            Screen currentScreen = event.getScreen();
            if (currentScreen instanceof TitleScreen) {
                // Предполагаем, что CustomTitleScreen расширяет TitleScreen и добавляет необходимую логику
                // для отображения вашей статичной панорамы
                event.setNewScreen(new CustomTitleScreen());
            }
        }

        public static class CustomTitleScreen extends TitleScreen {
            // ... ваш код для CustomTitleScreen ...

            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                // Рендеринг вашего фона
                this.renderBackground(poseStack);

                // Отрисовка других элементов экрана, если это необходимо
                super.render(poseStack, mouseX, mouseY, partialTicks);
            }

            @Override
            public void renderBackground(PoseStack poseStack) {
                // Рендеринг вашего статичного фона
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                GuiComponent.blit(poseStack, 0, 0, screenWidth, screenHeight, 0, 0, 1792, 1024, 1792, 1024);
            }
        }
*/


    }
}