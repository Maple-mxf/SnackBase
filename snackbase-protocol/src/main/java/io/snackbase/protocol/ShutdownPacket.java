package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;


/**
 * 
 * <pre><b>ping shutdown packet.</b></pre>
 */

public class ShutdownPacket extends SnackBasePacket {

	//default value
	public byte type = 0;

	@Override
	public int calcPacketSize() {
		return 2;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Shutdown Packet";
	}

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		if (packetLength == 2)
			type = mm.read();
	}

	@Override
	public void write(ByteBuffer buffer) {
		int size = calcPacketSize();
		BufferUtil.writeUB3(buffer, size);
		buffer.put(packetId);
		buffer.put(COM_SHUTDOWN);
		buffer.put(type);
	}

}
