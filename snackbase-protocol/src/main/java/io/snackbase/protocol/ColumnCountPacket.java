package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>column count packet.</b></pre>
 */
public class ColumnCountPacket extends SnackBasePacket {

	public int columnCount;

	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		this.packetLength = mm.readUB3();
		this.packetId = mm.read();
		this.columnCount = (int) mm.readLength();
	}

	@Override
	public void write(ByteBuffer buffer) {
		int size = calcPacketSize();
		BufferUtil.writeUB3(buffer, size);
		buffer.put(packetId);
		BufferUtil.writeLength(buffer, columnCount);
	}

	@Override
	public int calcPacketSize() {
		int size = BufferUtil.getLength(columnCount);
		return size;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Column Count Packet";
	}

}
