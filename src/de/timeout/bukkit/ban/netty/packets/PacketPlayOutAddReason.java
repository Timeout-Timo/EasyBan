package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.utils.Reason.ReasonType;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutAddReason implements Packet {
	
	private String name, display, title;
	private ReasonType type;
	private long firsttime, secondtime, thirdtime, firsttosecond, secondtothird, points;
	
	public PacketPlayOutAddReason(BukkitReason reason) {
		this.name = reason.getName();
		this.display = reason.getDisplay();
		this.title = reason.getEncodedTitle();
		this.type = reason.getType();
		this.firsttime = reason.getFirsttime();
		this.secondtime = reason.getSecondtime();
		this.thirdtime = reason.getThirdtime();
		this.firsttosecond = reason.getFirstToSecond();
		this.secondtothird = reason.getSecondToThird();
		this.points = reason.getPoints();
	}
	
	@Override
	public void read(ByteBuf byteBuf) throws IOException {}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF(name);
		out.writeUTF(display);
		out.writeUTF(title);
		out.writeUTF(type.getName());
		out.writeLong(firsttime);
		out.writeLong(secondtime);
		out.writeLong(thirdtime);
		out.writeLong(firsttosecond);
		out.writeLong(secondtothird);
		out.writeLong(points);
		
		byteBuf.writeBytes(out.toByteArray());
	}

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

	public String getTitle() {
		return title;
	}

	public ReasonType getType() {
		return type;
	}

	public long getFirsttime() {
		return firsttime;
	}

	public long getSecondtime() {
		return secondtime;
	}

	public long getThirdtime() {
		return thirdtime;
	}

	public long getFirsttosecond() {
		return firsttosecond;
	}

	public long getSecondtothird() {
		return secondtothird;
	}

	public long getPoints() {
		return points;
	}
}
