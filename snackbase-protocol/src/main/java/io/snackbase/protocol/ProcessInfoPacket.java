package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>process info command packet.</b></pre>
 */
public class ProcessInfoPacket extends SnackBasePacket {

	public byte payload;

	@Override
	public int calcPacketSize() {
		return 1;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Process Info Packet";
	}

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		payload = mm.read();
	}

	@Override
	public void write(ByteBuffer buffer) {
		int size = calcPacketSize();
		BufferUtil.writeUB3(buffer, size);
		buffer.put(packetId);
		buffer.put(COM_PROCESS_INFO);
	}

}
