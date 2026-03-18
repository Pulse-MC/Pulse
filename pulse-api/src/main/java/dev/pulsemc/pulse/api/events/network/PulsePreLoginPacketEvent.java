package dev.pulsemc.pulse.api.events.network;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import java.net.InetAddress;

/**
 * Triggered asynchronously for packets sent or received before the player has fully joined.
 * <p>
 * This includes Handshake, Status (Ping/MOTD), and Login phase packets.
 * Since the Bukkit {@link org.bukkit.entity.Player} object does not exist yet,
 * this event provides the connection's IP address instead.
 */
public class PulsePreLoginPacketEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private Object packet;
    private final boolean isIncoming; // TRUE = Client, FALSE = Server
    private boolean cancelled;

    public PulsePreLoginPacketEvent(@NotNull InetAddress address, @NotNull Object packet, boolean isIncoming) {
        super(true); // TRUE = Async Event
        this.address = address;
        this.packet = packet;
        this.isIncoming = isIncoming;
    }

    @NotNull public InetAddress getAddress() { return address; }
    @NotNull public Object getPacket() { return packet; }
    public void setPacket(@NotNull Object packet) { this.packet = packet; }

    public boolean isIncoming() { return isIncoming; }
    @NotNull public String getPacketName() { return packet.getClass().getSimpleName(); }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public @NotNull HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}