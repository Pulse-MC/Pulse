package dev.pulsemc.pulse.network.impl;

import dev.pulsemc.pulse.api.events.network.PulsePacketReceiveEvent;
import dev.pulsemc.pulse.api.events.network.PulsePreLoginPacketEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import java.net.InetSocketAddress;

public class PulsePacketInterceptor {

    public static Packet<?> onPacketReceive(Connection connection, Packet<?> packet) {
        var listener = connection.getPacketListener();

        if (listener instanceof ServerGamePacketListenerImpl gameListener) {
            if (PulsePacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
                org.bukkit.entity.Player player = gameListener.getCraftPlayer();
                if (player != null) {
                    PulsePacketReceiveEvent event = new PulsePacketReceiveEvent(player, packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return null;
                    return (Packet<?>) event.getPacket();
                }
            }
        } else {
            if (PulsePreLoginPacketEvent.getHandlerList().getRegisteredListeners().length > 0) {
                if (connection.getRemoteAddress() instanceof InetSocketAddress addr) {
                    PulsePreLoginPacketEvent event = new PulsePreLoginPacketEvent(addr.getAddress(), packet, true);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return null;
                    return (Packet<?>) event.getPacket();
                }
            }
        }
        return packet;
    }

    public static Packet<?> onPacketSendPreLogin(Connection connection, Packet<?> packet) {
        var listener = connection.getPacketListener();

        if (listener instanceof ServerGamePacketListenerImpl) {
            return packet;
        }

        if (PulsePreLoginPacketEvent.getHandlerList().getRegisteredListeners().length > 0) {
            if (connection.getRemoteAddress() instanceof InetSocketAddress addr) {
                PulsePreLoginPacketEvent event = new PulsePreLoginPacketEvent(addr.getAddress(), packet, false);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return null;
                return (Packet<?>) event.getPacket();
            }
        }
        return packet;
    }
}