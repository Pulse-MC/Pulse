package dev.pulsemc.pulse.api.events.network;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Triggered asynchronously when the server receives a packet from a client.
 * <p>
 * This event allows plugins to intercept, read, or cancel incoming packets
 * (e.g., player movement, clicks, interactions) before the server processes them.
 *
 * <b>WARNING:</b> This event fires on Netty IO threads. It is fully asynchronous.
 * Do not use Bukkit API methods here without proper synchronization!
 */
public class PulsePacketReceiveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private Object packet;
    private boolean cancelled;

    public PulsePacketReceiveEvent(@NotNull Player player, @NotNull Object packet) {
        super(true); // TRUE = Async Event
        this.player = player;
        this.packet = packet;
    }

    @NotNull public Player getPlayer() { return player; }
    @NotNull public Object getPacket() { return packet; }

    /**
     * Replaces the incoming packet with a modified one.
     */
    public void setPacket(@NotNull Object packet) { this.packet = packet; }

    /**
     * Gets the simple class name of the packet (e.g. "ServerboundPlayerMovePacket")
     */
    @NotNull public String getPacketName() { return packet.getClass().getSimpleName(); }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public @NotNull HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}