package Volume;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileBinary {

	private RandomAccessFile mFile;

	public FileBinary(RandomAccessFile eFile) {

		mFile = eFile;

	
	}

	public byte[] longToBytes(long l) {
		byte[] result = new byte[8];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (l & 0xFF);
			l >>= 8;
		}
		return result;
	}

	public long readLong() {
		long result = 0;
		try {

			byte[] lendo = new byte[8];
			for (int i = 0; i < 8; i++) {
				lendo[i] = mFile.readByte();
			}
			for (int i = 0; i < 8; i++) {
				result <<= Long.BYTES;
				result |= (lendo[i] & 0xFF);
			}

		} catch (IOException e) {

		}
		return result;
	}

	public String organizar(byte b) {
		String si = String.valueOf(b);

		if (si.length() == 1) {
			si = "00" + si;
		} else if (si.length() == 2) {
			si = "0" + si;
		}

		return si;
	}

	public void dump() {

		try {

			long tamanho = mFile.length();
			long index = 0;
			int cur = 0;
			int max = 30;

			mFile.seek(0);

			while (index < tamanho) {
				System.out.print(organizar(mFile.readByte()) + " ");
				index += 1;
				cur += 1;
				if (cur >= max) {
					cur = 0;
					System.out.print("\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void limpar() {

		try {

			long tamanho = mFile.length();
			long index = 0;

			mFile.seek(0);

			while (index < tamanho) {
				mFile.write(0);
				index += 1;

			}
		} catch (IOException e) {

		}

	}

	public void writeLong(long l) {

		try {
			mFile.write(longToBytes(l));
		} catch (IOException e) {

		}

	}



	public long getLength() {
		try {
			return mFile.length();
		} catch (IOException e) {
			return -1;
		}
	}


	public void inicio() {
		try {
			mFile.seek(0);
		} catch (IOException e) {

		}
	}



	public byte readByte() {
		try {		
			return  mFile.readByte();
		} catch (IOException e) {
			return -1;
		}
	}
	
	public byte[] readBuffer(int eTam) {
		byte[] eB = new byte[eTam];
		
		try {	
			
			int index = 0;
			while(index<eTam)
			{
				eB[index] =  mFile.readByte();
				index+=1;
			}
			 
		} catch (IOException e) {
			
		}
		
		return eB;
	}
	
	
	
	public void writeByte(Byte eByte) {
		try {
			mFile.writeByte(eByte);
		} catch (IOException e) {

		}
	}

	public void writeBuffer(byte[] eBytes) {
		try {
			int mIndex = 0;
			int mFim = eBytes.length;
			while(mIndex<mFim) {
				
				mFile.writeByte(eBytes[mIndex]);

				mIndex+=1;
			}
		} catch (IOException e) {

		}
	}
	

	public long getPosition() {
		try {
			return mFile.getFilePointer();
		} catch (IOException e) {
			return -1;
		}
	}

	public void seek(long p) {
		try {
			mFile.seek(p);
		} catch (IOException e) {
		}
	}

	public void close() {
		try {
			mFile.close();
		} catch (IOException e) {
		}
	}

}
