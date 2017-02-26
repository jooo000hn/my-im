package io.goku.chat.core.transport;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

/**
 * 数据缓冲区对象，直接封装了netty的ByteBuf
 * 
 * byteBuffer 的封裝
 * 
 */
public class DataBuffer {

	protected ByteBuf mBuffer;

	public DataBuffer() {
		mBuffer = Unpooled.buffer();
	}

	public DataBuffer(ByteBuf binaryBuffer) {
		mBuffer = binaryBuffer;
	}

	public DataBuffer(int length) {
		mBuffer = Unpooled.buffer(length);
	}

	public boolean isReadable() {
		return mBuffer.isReadable();
	}

	public boolean isWritable() {
		return mBuffer.isWritable();
	}

	public byte[] array() {
		return mBuffer.array();
	}

	public void setOrignalBuffer(ByteBuf buffer) {
		this.mBuffer = buffer;
	}

	public ByteBuf getOrignalBuffer() {
		return mBuffer;
	}

	public void writeByte(int value) {
		mBuffer.writeByte(value);
	}

	public byte readByte() {
		return mBuffer.readByte();
	}

	public void writeBytes(byte[] bytes) {
		mBuffer.writeBytes(bytes);
	}

	public byte[] readBytes(int length) {
		byte[] bytes = new byte[length];
		mBuffer.readBytes(bytes);
		return bytes;
	}

	public int readInt() {
		if (mBuffer.isReadable()) {
			return mBuffer.readInt();
		} else {
			return 0;
		}
	}

	public void writeInt(int value) {
		mBuffer.writeInt(value);
	}

	public short readShort() {
		if (mBuffer.isReadable()) {
			return mBuffer.readShort();
		} else {
			return 0;
		}
	}

	public void writeShort(int value) {
		mBuffer.writeShort(value);
	}

	public char readChar() {
		return mBuffer.readChar();
	}

	public void writeChar(char c) {
		mBuffer.writeChar(c);
	}

	public long readLong() {
		if (mBuffer.isReadable()) {
			return mBuffer.readLong();
		} else {
			return 0;
		}
	}

	public void writeLong(long value) {
		mBuffer.writeLong(value);
	}

	public double readDouble() {
		if (mBuffer.isReadable()) {
			return mBuffer.readDouble();
		} else {
			return 0;
		}
	}

	public void writeDouble(double value) {
		mBuffer.writeDouble(value);
	}

	/**
	 * 读取一个字符串
	 *
	 * @return 格式：前导length表示字符串的byte数 length(4字节)string(length字节)
	 */
	public String readString() {
		int length = readInt();
		byte[] bytes = readBytes(length);

		return new String(bytes);
	}

	/**
	 * 写入一个字符串
	 *
	 * @param str
	 *            数据格式见方法readString()
	 */
	public void writeString(String str) {
		if (str == null) {
			str = "";
		}
		byte[] bytes = str.getBytes();
		writeInt(bytes.length);
		writeBytes(bytes);
	}

	/**
	 * 读取int数组
	 *
	 * @return 格式：前导count表示数组中有多少个元素 count(4字节)int1(4字节)...intCount(4字节)
	 */
	public int[] readIntArray() {
		int count = readInt();
		int[] intArray = new int[count];
		for (int i = 0; i < count; i++) {
			intArray[i] = readInt();
		}
		return intArray;
	}

	/**
	 * 写入int数组
	 *
	 * @param intArray
	 *            格式见readIntArray()
	 */
	public void writeIntArray(int[] intArray) {
		int count = intArray.length;
		writeInt(count);
		for (int i = 0; i < count; i++) {
			writeInt(intArray[i]);
		}
	}

	/**
	 * @param intList
	 * @Description: 写入一个int的list, list转数组太蛋疼了
	 */
	public void writeIntList(List<Integer> intList) {
		if (intList == null || intList.isEmpty()) {
			writeInt(0);
			return;
		}
		int count = intList.size();
		writeInt(count);
		for (int i = 0; i < count; i++) {
			writeInt(intList.get(i));
		}
	}

	/**
	 * 读取byte数组
	 *
	 * @return 格式：前导count表示数组中有多少个元素 count(4字节)byte1(4字节)...byteCount(4字节)
	 */
	public byte[] readByteArray() {
		int length = readInt(); // 获取长度
		byte[] bytes = new byte[length];
		mBuffer.readBytes(bytes);
		return bytes;
	}

	/**
	 * 写入byte数组
	 *
	 * @param byteArray
	 *            格式见readByteArray()
	 */
	public void writeByteArray(byte[] byteArray) {
		int length = byteArray.length;
		writeInt(length);
		mBuffer.writeBytes(byteArray);
	}

	/**
	 * 读取String数组
	 *
	 * @return 格式：前导count表示数组中有多少个元素 count(4字节)string1(4字节)...stringsCount(4字节)
	 */
	public String[] readStringArray() {
		int count = readInt(); // 获取长度
		String[] strArray = new String[count];
		for (int i = 0; i < count; i++) {
			strArray[i] = readString();
		}
		return strArray;
	}

	/**
	 * 写入String数组
	 *
	 * @param strArray
	 *            格式见readStringArray()
	 */
	public void writeStringArray(String[] strArray) {
		int count = strArray.length;
		writeInt(count);
		for (int i = 0; i < count; i++) {
			writeString(strArray[i]);
		}
	}

	/**
	 * 获取有效(可读取)的byte数
	 *
	 * @return
	 */
	public int readableBytes() {
		return mBuffer.readableBytes();
	}

	/**
	 * 返回可以读取的字节数
	 * 
	 * @return
	 */
	public int getSize() {
		return this.readableBytes();
	}

	public DataBuffer readDataBuffer() {
		if (mBuffer == null || mBuffer.readableBytes() == 0) {
			return new DataBuffer(0);
		}
		int length = readableBytes();
		DataBuffer dataBuffer = new DataBuffer(0);
		dataBuffer.setOrignalBuffer(mBuffer.readBytes(length));
		return dataBuffer;
	}

	public void writeDataBuffer(DataBuffer inputBuffer) {
		if (inputBuffer == null || inputBuffer.readableBytes() == 0) {
			return;
		}
		mBuffer.writeBytes(inputBuffer.mBuffer);
	}
	
	public String toString() {
		if (this.mBuffer == null) {
			return "";
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < this.getSize(); i++) {
			if (i > 0) {
				stringBuffer.append(", ");
			}
			stringBuffer.append(byte2hex(this.mBuffer.getByte(i)));
		}
		return "DataBuffer [mBuffer=" + stringBuffer.toString() + "]";
	}

	public String tobyteString() {
		if (this.mBuffer == null) {
			return "";
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < this.getSize(); i++) {
			if (i > 0) {
				stringBuffer.append(", ");
			}
			stringBuffer.append(byte2hex(this.mBuffer.getByte(i)));
		}
		return stringBuffer.toString();
	}
	
	private String byte2hex(byte b) {
		int h = b >>> 4 & 0xF;
		char c1 = h >= 10 ? (char) (h - 10 + 65) : (char) (h + 48);

		int l = b & 0xF;
		char c2 = l >= 10 ? (char) (l - 10 + 65) : (char) (l + 48);

		return new String(new char[] { c1, c2 });
	}
	
	 
	
}