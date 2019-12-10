package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>mysql ok packet.</b></pre>
 */
public class OKPacket extends SnackBasePacket {
	public static final byte HEADER = 0x00;
	public byte header = HEADER;
	public long affectedRows;
	public long insertId;
	public int serverStatus;
	public int warningCount;
	public byte[] message;
	// there is no need to produce OKPacket every time,sometimes we can use OK
	// byte[] directly.
	public static final byte[] OK = new byte[] { 7, 0, 0, 1, 0, 0, 0, 2, 0, 0,
			0 };
	private static final byte[] AC_OFF = new byte[] { 7, 0, 0, 1, 0, 0, 0, 0,
			0, 0, 0 };

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		header = mm.read();
		affectedRows = mm.readLength();
		insertId = mm.readLength();
		serverStatus = mm.readUB2();
		warningCount = mm.readUB2();
		if (mm.hasRemaining()) {
			this.message = mm.readBytesWithLength();
		}
	}

	@Override
	public void write(ByteBuffer buffer) {
		BufferUtil.writeUB3(buffer, calcPacketSize());
		buffer.put(packetId);
		buffer.put(header);
		BufferUtil.writeLength(buffer, affectedRows);
		BufferUtil.writeLength(buffer, insertId);
		BufferUtil.writeUB2(buffer, serverStatus);
		BufferUtil.writeUB2(buffer, warningCount);
		if (message != null) {
			BufferUtil.writeWithLength(buffer, message);
		}
	}

	@Override
	public int calcPacketSize() {
		int i = 1;
		i += BufferUtil.getLength(affectedRows);
		i += BufferUtil.getLength(insertId);
		i += 4;
		if (message != null) {
			i += BufferUtil.getLength(message);
		}
		return i;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL OK Packet";
	}

}
