package com.ycg.ycexpress.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Common {
	private static String DateFormate = "yyyy-MM-dd HH:mm:ss";

	private static AtomicLong serial = new AtomicLong(1);

	private static Common instance = new Common();

	private Common() {

	}

	public static Common getInstance() {
		return instance;
	}

	public String getDateFormate() {
		return DateFormate;
	}

	public byte[] getSerialID() {

		long cmID = serial.getAndIncrement();

		if (cmID < 0 || cmID > 65535) {
			serial.set(0);
			cmID = 1;
		}

		byte rebyte[] = new byte[2];

		rebyte[0] = (byte) (cmID >> 8);
		rebyte[1] = (byte) cmID;

		return rebyte;
	}

	/**
	 * 将数组转成字符串
	 *
	 * @param array
	 * @return
	 */
	public String byte2string(byte array) {
		return String.format("%02X", array);
	}

	/**
	 * 获得无符号的byte
	 *
	 * @param Sign
	 * @return
	 */
	public int getNonSign(byte Sign) {
		if (Sign < 0) {
			return (Sign + 256);
		} else {
			return Sign;
		}
	}

	public String translate(String s) {
		return s.replaceAll("%2E", ".").replaceAll("%20", " ").replaceAll("%22", "\"").replaceAll("%23", "#")
				.replaceAll("%25", "%").replaceAll("%26", "&").replaceAll("%28", "(").replaceAll("%29", ")")
				.replaceAll("%2B", "+").replaceAll("%2C", "-").replaceAll("%2F", "/").replaceAll("%3A", ":")
				.replaceAll("%3B", ";").replaceAll("%3C", "<").replaceAll("%3D", "=").replaceAll("%3E", ">")
				.replaceAll("%3F", "?").replaceAll("%40", "@").replaceAll("%5C", "\\").replaceAll("%7C", "|");
	}

	public byte[] getGPRS2_0IDBytes(String s) {

		return new byte[] { (byte) Integer.parseInt(s.substring(0, 2), 16),
				(byte) Integer.parseInt(s.substring(2, 4), 16), (byte) Integer.parseInt(s.substring(4, 6), 16),
				(byte) Integer.parseInt(s.substring(6, 8), 16), (byte) Integer.parseInt(s.substring(8, 10), 16),
				(byte) Integer.parseInt(s.substring(10, 12), 16), (byte) Integer.parseInt(s.substring(12, 14), 16) };
	}

	/**
	 * byte数组转换为Mina ByteBuffer形式的String
	 *
	 * @param data
	 * @return
	 */
	public String byteToMinaString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		buf.append("[pos=");
		buf.append("0");
		buf.append(" lim=");
		buf.append(data.length);
		buf.append(" cap=");
		buf.append(data.length);
		buf.append(": ");
		for (int i = 0; i < data.length; i++) {
			buf.append(String.format("%02X", data[i]));
			buf.append(" ");
		}
		buf.append(']');
		return buf.toString();
	}

	/**
	 * byte数组转化为String
	 *
	 * @param data
	 * @return
	 */
	public String bytesToString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(String.format("%02x", data[i]));
			buf.append(" ");
		}
		return buf.toString().trim();
	}

	public long byte2Long(int a, int b, int c, int d) {
		return (long) a * 0x1000000 + (long) b * 0x10000 + (long) c * 0x100 + d;
	}

	/**
	 * 将byte转换为long型
	 */
	public long byte2Long(byte a, byte b, byte c, byte d) {
		return byte2Long(getNonSign(a), getNonSign(b), getNonSign(c), getNonSign(d));
	}

	public double bytes2double(byte b, byte b1, byte b2, byte b3) {
		return ((double) byte2long((new byte[] { b, b1, b2, b3 }))) / (double) 1000000;
	}

	/**
	 * 10进制转换为16进制
	 *
	 * @param param
	 * @return
	 */

	public String long2HexStr(long param) {
		try {
			String hex = Long.toHexString(param).toString().toUpperCase();
			return hex;
		} catch (NumberFormatException e) {
			return "";
		}
	}

	/**
	 * 16进制转换为10进制
	 *
	 * @param param
	 * @return
	 */

	public long hexStr2long(String param) {
		param = param.toUpperCase();
		long ret = 0;
		int val = 0;
		for (int i = 0; i < param.length(); i++) {
			val = 0;
			switch (param.charAt(i)) {
				case '0':
					val = 0;
					break;
				case '1':
					val = 1;
					break;
				case '2':
					val = 2;
					break;
				case '3':
					val = 3;
					break;
				case '4':
					val = 4;
					break;
				case '5':
					val = 5;
					break;
				case '6':
					val = 6;
					break;
				case '7':
					val = 7;
					break;
				case '8':
					val = 8;
					break;
				case '9':
					val = 9;
					break;
				case 'A':
					val = 10;
					break;
				case 'B':
					val = 11;
					break;
				case 'C':
					val = 12;
					break;
				case 'D':
					val = 13;
					break;
				case 'E':
					val = 14;
					break;
				case 'F':
					val = 15;
					break;
			}
			ret = ret + (long) val * (long) (Math.pow(16, (param.length() - i - 1)));
		}
		return ret;
	}

	public void printDeviceData(String direction, String vtID, String cmdID, String msgSerial, byte[] vtDataBytes) {
		String time = Common.getInstance().getCurrentTimeStr();
		System.out.println("\r\n[" + direction + "] [" + time + "] [" + vtID + "] [0x" + cmdID + "] [" + msgSerial
				+ "]\r\n[" + byte2string(vtDataBytes) + "]");

	}

	public String toBCD(byte[] b) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			stringBuffer.append(String.format("%02X", b[i]));
		}
		return stringBuffer.toString();
	}

	public byte[] BCD2Bytes(String hex) {
		if (hex == null)
			return new byte[0];
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * 定长BCD码转换成字节数组
	 *
	 * @param hex
	 *            BCD码
	 * @param fixedLen
	 *            长度
	 * @return 字节数组
	 */
	public byte[] BCD2FixedBytes(String hex, int fixedLen) {
		byte[] returnBytes = new byte[fixedLen];
		byte[] temp = BCD2Bytes(hex);
		try {
			if (temp != null && temp.length > 0)
				System.arraycopy(temp, 0, returnBytes, 0, temp.length > fixedLen ? fixedLen : temp.length);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return returnBytes;
	}

	private byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 将数组转成字符串 在调试或记录日志时用到
	 *
	 * @param array
	 * @return
	 */
	public String byte2string(byte[] array) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			sb = sb.append(String.format("%02X", array[i]));
		}

		return sb.toString().trim();
	}

	public byte[] int2byte(int value, int arrayLen) { // 将整型转为字节数组
		if (arrayLen < 1) {
			arrayLen = 1;
		}
		byte ret[] = new byte[arrayLen];
		ByteBuffer a = ByteBuffer.allocate(4);
		a.putInt(value);
		a.clear();
		byte b[] = new byte[4];
		a.get(b);
		if (arrayLen == 1) {
			ret[0] = b[3];
		}
		if (arrayLen == 2) {
			ret[0] = b[2];
			ret[1] = b[3];
		}
		if (arrayLen == 3) {
			ret[0] = b[1];
			ret[1] = b[2];
			ret[2] = b[3];
		}
		if (arrayLen >= 4) {
			ret[arrayLen - 4] = b[0];
			ret[arrayLen - 3] = b[1];
			ret[arrayLen - 2] = b[2];
			ret[arrayLen - 1] = b[3];
		}
		return ret;
	}

	public long byte2long(byte[] b) {
		long l = 0;
		for (int i = 0; i < b.length; i++) {
			l = l + (long) ((b[i] & 0xff) * Math.pow(256, b.length - i - 1));
		}
		return l;
	}

	public String getTimeStr(byte[] time) {
		return String.format("%02d-%02d-%02d %02d:%02d:%02d", (time[0]), (time[1]), (time[2]), (time[3]), (time[4]),
				(time[5]));
	}

	public byte[] getCurrentTimeInBcdBytes(String format) {
		String timeStr = getCurrentTimeInFormat(format);
		return Common.getInstance().BCD2Bytes(timeStr);
	}

	public String getCurrentTimeInFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	public String getCurrentTimeStr() {
		Calendar c = Calendar.getInstance();
		return String.format("%02d-%02d-%02d %02d:%02d:%02d", c.get(Calendar.YEAR) - 2000, c.get(Calendar.MONTH) + 1,
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND));
	}

	public byte[] getTimeBytes(String s) {
		return new byte[] { (byte) (Integer.parseInt(s.substring(0, 2))), (byte) (Integer.parseInt(s.substring(3, 5))),
				(byte) (Integer.parseInt(s.substring(6, 8))), (byte) (Integer.parseInt(s.substring(9, 11))),
				(byte) (Integer.parseInt(s.substring(12, 14))), (byte) (Integer.parseInt(s.substring(15, 17))) };
	}

	public byte[] booleans2Bytes(boolean[] status) {
		StringBuffer sb = new StringBuffer();
		for (boolean s : status) {
			sb.append(s ? "1" : "0");
		}

		byte[] b = new byte[status.length / 8];
		for (int i = 0; i < status.length / 8; i++) {
			int temp = Integer.valueOf(sb.toString().substring(i * 8, i * 8 + 8), 2);
			b[i] = (byte) temp;
		}
		return b;
	}

	public boolean[] bytes2booleans(byte[] status) {
		boolean[] b = new boolean[status.length * 8];
		for (int i = 0; i < status.length; i++) {
			boolean[] temp = byte2booleans(status[i]);
			for (int j = 0; j < temp.length; j++) {
				b[(status.length - 1 - i) * 8 + j] = temp[j];
			}
		}
		return b;
	}

	public boolean[] byte2booleans(byte status) {
		boolean[] b = new boolean[8];
		String s = Integer.toBinaryString(status & 0xFF);
		int len = s.toCharArray().length;
		for (int n = 0; n < 8 - len; n++) {
			s = "0" + s;
		}

		for (int i = 0; i < 8; i++) {
			if (s.charAt(i) == '1') {
				b[7 - i] = true;
			}
		}
		return b;
	}

	public byte[] hexStr2bytes(String rawData) {
		String[] readyStr = rawData.trim().split(" ");
		byte[] b = new byte[readyStr.length];
		for (int i = 0; i < readyStr.length; i++) {
			b[i] = (byte) Integer.parseInt(readyStr[i], 16);
		}
		return b;
	}

	/**
	 * 异或和校验
	 *
	 * @param 校验数组
	 * @return 校验字节
	 */
	public byte getCheckSum(byte[] data) {
		byte ret = 0;
		for (int i = 0; i < data.length; i++) {
			ret ^= data[i];
		}
		return ret;

	}

	/**
	 * LRC 校验 LRC1：从长度开始到数据结束的每一个字节的异或值再异或0x33； LRC2：从长度开始到数据结束的每一个字节的累加和再加0x33
	 *
	 * @param data
	 * @return byte[LRC1、LRC2]
	 */
	public byte[] getLRC(byte[] data) {
		byte[] LRC = new byte[2];
		LRC[0] = (byte) (getCheckSum(data) ^ 0x33);

		byte[] temp = new byte[data.length + 1];
		System.arraycopy(data, 0, temp, 0, data.length);
		temp[temp.length - 1] = LRC[0];
		int sum = 0;
		for (int i = 0; i < temp.length - 1; i++) {
			sum += temp[i];
		}
		LRC[1] = (byte) (sum + 0x33);
		return LRC;
	}

	/**
	 * 将包装好的字节转译 [0x7d]转成[0x7d, 0x01] ; [0x7e]转成[0x7d, 0x02]，,并在头尾加上标识符0x7e
	 *
	 * @param data
	 * @return
	 */
	public byte[] encryptJT808Bytes(byte[] data) {
		java.nio.ByteBuffer bf = java.nio.ByteBuffer.allocate(data.length * 2 + 2);
		bf.put((byte) 0x7E);
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 0x7D) {
				bf.put((byte) 0x7D);
				bf.put((byte) 0x01);
			} else if (data[i] == 0x7E) {
				bf.put((byte) 0x7D);
				bf.put((byte) 0x02);
			} else {
				bf.put(data[i]);
			}
		}
		bf.put((byte) 0x7e);

		byte[] ret = new byte[bf.position()];
		System.arraycopy(bf.array(), 0, ret, 0, ret.length);
		return ret;
	}

	public byte[] wrapFlags(byte[] data) {
		java.nio.ByteBuffer bf = java.nio.ByteBuffer.allocate(data.length + 2);
		bf.put((byte) 0x7E);
		bf.put(data);
		bf.put((byte) 0x7E);
		bf.clear();
		return bf.array();
	}

	public byte[] unwrapFlags(byte[] data) {
		byte[] b = new byte[data.length - 2];
		System.arraycopy(data, 1, b, 0, b.length);
		return b;
	}

	/**
	 * 去掉头尾的标识符，并将字节组转译 [0x7d, 0x01]转成[0x7d] ; [0x7d, 0x02]转成[0x7e]
	 *
	 * @param data
	 * @return
	 */
	public byte[] decryptJT808Bytes(byte[] data) {
		java.nio.ByteBuffer bf = java.nio.ByteBuffer.allocate(data.length);

		for (int i = 1; i < data.length - 1; i++) {
			if (i == data.length - 2) {
				bf.put(data[i]);
				break;
			}

			if (data[i] == (byte) 0x7D && data[i + 1] == (byte) 0x01) {
				bf.put((byte) 0x7D);
				i++;
			} else if (data[i] == (byte) 0x7D && data[i + 1] == (byte) 0x02) {
				bf.put((byte) 0x7E);
				i++;
			} else {
				bf.put(data[i]);
			}

		}

		byte[] b = new byte[bf.position()];
		System.arraycopy(bf.array(), 0, b, 0, b.length);
		return b;
	}

	/**
	 * 去掉头尾的标识符，并将字节组转译 [0x7d, 0x01]转成[0x7d] ; [0x7d, 0x02]转成[0x7e]
	 *
	 * @param data
	 * @return
	 */
	public byte[] decryptJT808BytesWithDelimiter(byte[] data) {
		java.nio.ByteBuffer bf = java.nio.ByteBuffer.allocate(data.length);
		bf.put(data[0]);

		for (int i = 1; i < data.length - 1; i++) {
			if (i == data.length - 2) {
				bf.put(data[i]);
				break;
			}

			if (data[i] == (byte) 0x7D && data[i + 1] == (byte) 0x01) {
				bf.put((byte) 0x7D);
				i++;
			} else if (data[i] == (byte) 0x7D && data[i + 1] == (byte) 0x02) {
				bf.put((byte) 0x7E);
				i++;
			} else {
				bf.put(data[i]);
			}

		}

		bf.put(data[data.length - 1]);

		return bf.array();
	}

	public LinkedList<byte[]> multipack(byte[] b, int singlePackSize) {
		int len = b.length;
		int Kpart = singlePackSize;
		int total = 0;
		LinkedList<byte[]> list = new LinkedList<byte[]>();

		if (len > Kpart) {

			total = (int) (len / Kpart);
			int y = (int) (len % Kpart);
			if (y != 0)
				total = total + 1;
			int blen = 0;
			for (int i = 0; i < total; i++) {
				if (i == total - 1 && y != 0) {
					blen = y;
				} else {

					blen = Kpart;
				}
				ByteBuffer ioBuffer = ByteBuffer.allocate(blen);
				for (int j = 0; j < blen; j++)
					ioBuffer.put(b[i * Kpart + j]);

				byte[] byteArray = new byte[ioBuffer.position()];
				ioBuffer.clear();
				ioBuffer.get(byteArray);
				list.add(byteArray);

				// int ii = commandSender.pack(byteArray, true, DeviceID);
				// if (i == 0) {
				// returnSerial = ii;
				// }
				// packResList.put(ii,
				// Long.parseLong(DeviceID + String.valueOf(cmdID)));

			}
		} else {
			list.add(b);
		}
		return list;
	}

	public LinkedHashMap<Integer, byte[]> multipack4taxi(byte[] b, int singlePackSize) {
		int len = b.length;
		int Kpart = singlePackSize;
		int total = 0;
		int startByteAddr = 0;
		LinkedHashMap<Integer, byte[]> map = new LinkedHashMap<Integer, byte[]>();

		if (len > Kpart) {

			total = (int) (len / Kpart);
			int y = (int) (len % Kpart);
			if (y != 0)
				total = total + 1;
			int blen = 0;
			for (int i = 0; i < total; i++) {
				if (i == total - 1 && y != 0) {
					blen = y;
				} else {

					blen = Kpart;
				}
				startByteAddr = i * Kpart;

				ByteBuffer ioBuffer = ByteBuffer.allocate(blen);
				for (int j = 0; j < blen; j++)
					ioBuffer.put(b[i * Kpart + j]);

				byte[] byteArray = new byte[ioBuffer.position()];
				ioBuffer.clear();
				ioBuffer.get(byteArray);
				map.put(startByteAddr, byteArray);

			}
		} else {
			map.put(0, b);
		}
		return map;
	}

	/**
	 * 将字符串转化成固定长度的数组，不足长度在后面补0x00，超过长度将丢失字符串后面部分
	 *
	 * @param s
	 *            字符串
	 * @param len
	 *            数组长度
	 * @return 字节数组
	 */
	public byte[] str2fixedLenBytes(String s, int len) {
		if (s == null || len < 0) {
			return null;
		}
		byte[] b = s.getBytes();
		byte[] returnBytes = new byte[len];
		System.arraycopy(b, 0, returnBytes, 0, b.length <= len ? b.length : len);
		return returnBytes;
	}

	/**
	 * 通过文件锁的方式判断程序是否已经运行
	 *
	 * @return
	 */
	@SuppressWarnings("resource")
	public boolean isApplicaitonAlreadyRunning() {

		File file = new File(new File("").getAbsolutePath() + File.separator + " .lock");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		FileLock lock = null;
		try {
			lock = new RandomAccessFile(file.getAbsolutePath(), "rw").getChannel().tryLock();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return lock == null ? true : false;

	}

	/**
	 * NIO way
	 *
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public byte[] file2Bytes(String filename) throws IOException {

		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (channel != null)
					channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fs != null)
					fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 字符串转换到时间格式
	 *
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		ParsePosition pos = new ParsePosition(0);
		Date date = null;
		try {
			date = sdf.parse(dateStr, pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

		/*
		 * SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		 * ParsePosition pos = new ParsePosition(0); Date strtodate =
		 * formatter.parse(dateStr, pos); return strtodate;
		 */
	}

	public boolean withInNdaysBeforeNow(String dateStr, int nDays) {
		return withInNsecondsBeforeNow(dateStr, nDays * 60 * 60 * 24);
	}

	public boolean withInNhoursBeforeNow(String dateStr, int nhours) {
		return withInNsecondsBeforeNow(dateStr, nhours * 60 * 60);
	}

	public boolean withInNminutesBeforeNow(String dateStr, int nMinutes) {
		return withInNsecondsBeforeNow(dateStr, nMinutes * 60);
	}

	public boolean withInNsecondsBeforeNow(String dateStr, int nSeconds) {
		Date now = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DateFormate, Locale.CHINA);
			Date compareDate = sdf.parse(dateStr);
			long compareDateTimeInMillsc = compareDate.getTime();
			long nowDateTimeInMillsc = now.getTime();
			long diff = 1000 * nSeconds;
			return nowDateTimeInMillsc - compareDateTimeInMillsc <= diff;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean betweenNdays(String dateStr, int nDaysBeforeNow, int nDaysAfterNow) {
		return withInNdaysBeforeNow(dateStr, nDaysBeforeNow) && withInNdaysBeforeNow(dateStr, nDaysAfterNow * -1);
	}

	public byte[] encryptTo3Des(byte[] myEncryptionKey, String source) throws Exception {

		KeySpec ks = new DESedeKeySpec(myEncryptionKey);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
		Cipher cipher = Cipher.getInstance("DESede");
		SecretKey key = skf.generateSecret(ks);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] plainText = source.getBytes();
		byte[] encryptedText = cipher.doFinal(plainText);
		return encryptedText;
	}

	public final byte[] MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			return md;
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

}
