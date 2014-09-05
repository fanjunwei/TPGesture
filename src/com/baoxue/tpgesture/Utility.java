package com.baoxue.tpgesture;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

public class Utility {
	public static int FourBytesToInt(byte Bytes[]) {
		return FourBytesToInt(Bytes, 0);
	}

	public static int FourBytesToInt(byte Bytes[], int startIndex) {
		int a = Bytes[startIndex] & 0xff;
		int b = Bytes[startIndex + 1] & 0xff;
		int c = Bytes[startIndex + 2] & 0xff;
		int d = Bytes[startIndex + 3] & 0xff;
		return a | b << 8 | c << 16 | d << 24;
	}

	public static byte[] intToByte(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i & 0xff);
		result[1] = (byte) ((i & 0xff00) >> 8);
		result[2] = (byte) ((i & 0xff0000) >> 16);
		result[3] = (byte) ((i & 0xff000000) >> 24);
		return result;

	}

	public static String formatLength(long length) {
		java.text.NumberFormat formater = java.text.DecimalFormat.getInstance();
		formater.setMaximumFractionDigits(2);
		formater.setMinimumFractionDigits(0);

		if (length > 1024 * 1024) {
			float value = ((float) length) / (1024 * 1024);
			return formater.format(value) + "MB";
		} else if (length > 1024) {
			float value = ((float) length) / 1024;
			return formater.format(value) + "KB";
		} else {
			return String.valueOf(length) + "B";
		}
	}


	public static int readHttp(InputStream input, byte[] buffer, int offset,
			int length) {
		int readlen = 0;
		int total = 0;
		if (length > 0) {
			try {
				while ((readlen = input.read(buffer, offset, length)) > 0
						&& length > 0) {
					length -= readlen;
					offset += readlen;
					total += readlen;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return total;
	}

	public static byte[] readBlock(InputStream input) {
		byte[] lenByte = new byte[4];
		byte[] buffer = null;
		int bytes = 0;

		readHttp(input, lenByte, 0, 4);
		bytes = Utility.FourBytesToInt(lenByte);
		if (bytes > 0) {
			buffer = new byte[bytes];
			readHttp(input, buffer, 0, bytes);
		}
		return buffer;

	}

	public static String runCommand(String cmd) {

		LocalSocket s = null;
		LocalSocketAddress l;
		s = new LocalSocket();
		l = new LocalSocketAddress("task",
				LocalSocketAddress.Namespace.RESERVED);
		byte[] lineBuffer = null;
		StringBuffer sb = new StringBuffer();
		try {
			s.connect(l);
			OutputStream out = s.getOutputStream();

			byte[] b = cmd.getBytes();
			out.write((int) b.length);
			out.write(b);
			byte[] end = new byte[] { (byte) 0xff };

			out.write(end);

			InputStream input = s.getInputStream();

			while ((lineBuffer = readBlock(input)) != null) {
				sb.append(new String(lineBuffer, "utf-8"));
			}
			input.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();
	}

}
