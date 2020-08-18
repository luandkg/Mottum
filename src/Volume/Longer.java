package Volume;

import java.io.IOException;

public class Longer {

	public byte[] longToBytes(long l) {
		byte[] result = new byte[8];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (l & 0xFF);
			l >>= 8;
		}
		return result;
	}
	
	public long readLong(byte[] leitor, int index) {
		long result = 0;
		
		
		
	

			byte[] lendo = new byte[8];
			for (int i = 0; i < 8; i++) {
				lendo[i] =leitor[index+i];
			}
			
			for (int i = 0; i < 8; i++) {
				result <<= Long.BYTES;
				result |= (lendo[i] & 0xFF);
			}

		
		return result;
	}
	
}
