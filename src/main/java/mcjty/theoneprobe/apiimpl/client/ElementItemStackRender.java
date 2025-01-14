package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;

public class ElementItemStackRender {

    public static void render(ItemStack itemStack, IItemStyle style, PoseStack matrixStack, int x, int y) {
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
        if (!itemStack.isEmpty()) {
            int size = itemStack.getCount();
            String amount;
            if (size <= 1) {
                amount = "";
            } else if (size < 100000) {
                amount = String.valueOf(size);
            } else if (size < 1000000) {
                amount = String.valueOf(size / 1000) + "k";
            } else if (size < 1000000000) {
                amount = String.valueOf(size / 1000000) + "m";
            } else {
                amount = String.valueOf(size / 1000000000) + "g";
            }

            if (!RenderHelper.renderItemStack(Minecraft.getInstance(), itemRender, itemStack, matrixStack, x + (style.getWidth() - 18) / 2, y + (style.getHeight() - 18) / 2, amount)) {
                // There was a crash rendering this item
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, ChatFormatting.RED + "ERROR: " + itemStack.getHoverName());
            }
        }
    }

}
