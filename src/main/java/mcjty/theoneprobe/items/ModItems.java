package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.compat.BaubleTools;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    public static CreativeProbe CREATIVE_PROBE;
    public static Probe PROBE;
    public static Item NETHERITE_HELMET_PROBE;
    public static Item DIAMOND_HELMET_PROBE;
    public static Item GOLD_HELMET_PROBE;
    public static Item IRON_HELMET_PROBE;
    public static Item PROBE_GOGGLES;
    public static ProbeNote PROBE_NOTE;

    public static final String PROBETAG = "theoneprobe";
    public static final String PROBETAG_HAND = "theoneprobe_hand";

    public static void init() {
        PROBE = Registry.register(Registry.ITEM, TheOneProbe.asResource("probe"), new Probe());
        CREATIVE_PROBE = Registry.register(Registry.ITEM, TheOneProbe.asResource("creativeprobe"), new CreativeProbe());

        TopArmorMaterial materialNetheriteHelmet = new TopArmorMaterial("netherite_helmet_probe", 37, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, null);
        TopArmorMaterial materialDiamondHelmet = new TopArmorMaterial("diamond_helmet_probe", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, null);
        TopArmorMaterial materialGoldHelmet = new TopArmorMaterial("gold_helmet_probe", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, null);
        TopArmorMaterial materialIronHelmet = new TopArmorMaterial("iron_helmet_probe", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, null);

        NETHERITE_HELMET_PROBE = makeHelmet(materialNetheriteHelmet, 3, "netherite_helmet_probe", new FabricItemSettings().fireResistant());
        DIAMOND_HELMET_PROBE = makeHelmet(materialDiamondHelmet, 3, "diamond_helmet_probe", new FabricItemSettings());
        GOLD_HELMET_PROBE = makeHelmet(materialGoldHelmet, 4, "gold_helmet_probe", new FabricItemSettings());
        IRON_HELMET_PROBE = makeHelmet(materialIronHelmet, 2, "iron_helmet_probe", new FabricItemSettings());

        PROBE_NOTE = Registry.register(Registry.ITEM, TheOneProbe.asResource("probenote"), new ProbeNote());

        if (TheOneProbe.baubles) {
            PROBE_GOGGLES = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelmet(TopArmorMaterial material, int renderIndex, String name, Item.Properties settings) {
        Item item = new ArmorItem(material, EquipmentSlot.HEAD, settings
            .tab(TheOneProbe.tabProbe)) {

//            @Override
//            public boolean getHasSubtypes() {
//                return true;
//            }
//
//            @Override
//            public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
//                if (this.isInCreativeTab(tab)) {
//                    ItemStack stack = new ItemStack(this);
//                    CompoundNBT tag = new CompoundNBT();
//                    tag.setInteger(PROBETAG, 1);
//                    stack.setTagCompound(tag);
//                    subItems.add(stack);
//                }
//            }

            @Override
            public ItemStack getDefaultInstance() {
                ItemStack stack = super.getDefaultInstance();
                stack.getOrCreateTag().putInt(ModItems.PROBETAG, 1);
                return stack;
            }

            @Override
            public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> nonNullList) {
                if (this.allowedIn(creativeModeTab)) {
                    nonNullList.add(getDefaultInstance());
                }
            }
        };

        return Registry.register(Registry.ITEM, TheOneProbe.asResource(name), item);
    }

    public static boolean isProbeInHand(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == PROBE || stack.getItem() == CREATIVE_PROBE) {
            return true;
        }
        if (stack.getTag() == null) {
            return false;
        }
        return stack.getTag().contains(PROBETAG_HAND);
    }

    private static boolean isProbeHelmet(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getTag() == null) {
            return false;
        }
        return stack.getTag().contains(PROBETAG);
    }

    public static boolean hasAProbeSomewhere(Player player) {
        return hasProbeInHand(player, InteractionHand.MAIN_HAND) || hasProbeInHand(player, InteractionHand.OFF_HAND) || hasProbeInHelmet(player)
                || hasProbeInBauble(player);
    }

    private static boolean hasProbeInHand(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        return isProbeInHand(item);
    }

    private static boolean hasProbeInHelmet(Player player) {
        ItemStack helmet = player.getInventory().getItem(36+3);
//        ItemStack helmet = player.inventory.armorInventory.get(3);
        return isProbeHelmet(helmet);
    }

    private static boolean hasProbeInBauble(Player player) {
        if (TheOneProbe.baubles) {
            return BaubleTools.hasProbeGoggle(player);
        } else {
            return false;
        }
    }
}
