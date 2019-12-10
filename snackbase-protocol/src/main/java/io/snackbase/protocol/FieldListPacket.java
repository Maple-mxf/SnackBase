package io.snackbase.protocol;

import io.snackbase.protocol.util.BufferUtil;

import java.nio.ByteBuffer;

/**
 * 
 * <pre><b>mysql field list packet.</b></pre>
 */
public class FieldListPacket extends SnackBasePacket {
	public byte flag;
	public byte[] table;
	public byte[] fieldWildcard;

	@Override
	public void read(byte[] data) {
		SnackBaseMessage mm = new SnackBaseMessage(data);
		packetLength = mm.readUB3();
		packetId = mm.read();
		flag = mm.read();
		table = mm.readBytesWithNull();
		fieldWildcard = mm.readBytes();
	}

	@Override
	public void write(ByteBuffer buffer) {
		BufferUtil.writeUB3(buffer, calcPacketSize());
		buffer.put(packetId);
		buffer.put(COM_FIELD_LIST);
		BufferUtil.writeWithNull(buffer, table);
		buffer.put(fieldWildcard);
	}

	@Override
	public int calcPacketSize() {
		int i = 1;
		i += table.length + 1;
		i += fieldWildcard.length;
		return i;
	}

	@Override
	protected String getPacketInfo() {
		return "MySQL Field List Packet";
	}

}
