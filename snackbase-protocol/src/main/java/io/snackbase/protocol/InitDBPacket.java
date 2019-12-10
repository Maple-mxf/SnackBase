package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>mysql init db packet.</b></pre>
 */
public class InitDBPacket extends SnackBasePacket {
	public byte flag;
	public byte[] schema;

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		flag = mm.read();
		this.schema = mm.readBytes();
	}

	@Override
	public void write(ByteBuffer buffer) {
		BufferUtil.writeUB3(buffer, calcPacketSize());
		buffer.put(packetId);
		buffer.put(COM_INIT_DB);
		buffer.put(schema);
	}

	@Override
	public int calcPacketSize() {
		int i = 1;
		i += schema.length;
		return i;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Init DB Packet";
	}

}
