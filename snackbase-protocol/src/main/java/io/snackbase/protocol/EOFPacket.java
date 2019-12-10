package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>mysql eof packet.</b></pre>
 */
public class EOFPacket extends SnackBasePacket {

	public byte header = (byte) 0xfe;
	public int warningCount;
	public int status = 2;

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		header = mm.read();
		warningCount = mm.readUB2();
		status = mm.readUB2();
	}

	@Override
	public void write(ByteBuffer buffer) {
		int size = calcPacketSize();
		BufferUtil.writeUB3(buffer, size);
		buffer.put(packetId);
		buffer.put(header);
		BufferUtil.writeUB2(buffer, warningCount);
		BufferUtil.writeUB2(buffer, status);
	}

	@Override
	public int calcPacketSize() {
		return 5;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL EOF Packet";
	}

}
