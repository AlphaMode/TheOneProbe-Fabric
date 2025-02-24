package mcjty.theoneprobe.network;

import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class PacketReturnInfo {

    private ResourceKey<Level> dim;
    private BlockPos pos;
    private ProbeInfo probeInfo;

    public PacketReturnInfo(FriendlyByteBuf buf) {
        dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
        pos = buf.readBlockPos();
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

    public FriendlyByteBuf toBytes() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeResourceLocation(dim.location());
        buf.writeBlockPos(pos);
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
        return buf;
    }

    public PacketReturnInfo() {
    }

    public PacketReturnInfo(ResourceKey<Level> dim, BlockPos pos, ProbeInfo probeInfo) {
        this.dim = dim;
        this.pos = pos;
        this.probeInfo = probeInfo;
    }

    public void handle(Minecraft client) {
        client.execute(() -> {
            OverlayRenderer.registerProbeInfo(dim, pos, probeInfo);
        });
    }
}
