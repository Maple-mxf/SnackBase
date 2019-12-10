package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 
 * <pre><b>resultset row packet.</b></pre>
 */
public class ResultsetRowPacket extends SnackBasePacket {
	private static final byte NULL_MARK = (byte) 251;
	public int columnCount;
	public List<byte[]> columnValues;

	public ResultsetRowPacket() {

	}

	public ResultsetRowPacket(int columnCount) {
		this.columnCount = columnCount;
	}

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		for (int i = 0; i < columnCount; i++) {
			columnValues.add(mm.readBytesWithLength());
		}
	}

	@Override
	public void write(ByteBuffer buffer) {
		BufferUtil.writeUB3(buffer, calcPacketSize());
		buffer.put(packetId);
		for (int i = 0; i < columnCount; i++) {
			byte[] fv = columnValues.get(i);
			if (fv == null) {
				buffer.put(NULL_MARK);
			} else {
				BufferUtil.writeLength(buffer, fv.length);
				buffer.put(fv);
			}
		}
	}

	@Override
	public int calcPacketSize() {
		int size = 0;
		for (int i = 0; i < columnCount; i++) {
			byte[] v = columnValues.get(i);
			size += (v == null || v.length == 0) ? 1 : BufferUtil.getLength(v);
		}
		return size;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Resultset Row Packet";
	}

}
