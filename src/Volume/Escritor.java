package Volume;

import java.io.IOException;

public class Escritor {

	private String ALFABETO;
	private int ALFABETO_MAX;

	public Escritor() {

		ALFABETO = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789 ()[]{}+-.,<>//\\:;";
		ALFABETO_MAX = ALFABETO.length();

	}

	public byte getChar(String l) {

		int procurando = 0;
		boolean enc = false;

		while (procurando < ALFABETO_MAX) {

			String pl = String.valueOf(ALFABETO.charAt(procurando));
			if (pl.contentEquals(l)) {
				enc = true;
				break;

			}
			procurando += 1;
		}

		if (!enc) {
			throw new IllegalArgumentException("Char nao encontrado : " + l);
		}

		return (byte) (procurando + 1);
	}

	public String charFromByte(Byte b) {

		int procurando = 0;
		int pb = ((int) b) - 1;
		String ret = "";

		boolean enc = false;

		while (procurando < ALFABETO_MAX) {

			if (procurando == pb) {
				ret = String.valueOf(ALFABETO.charAt(procurando));
				enc = true;
				break;
			}

			procurando += 1;
		}

		if (!enc) {
			throw new IllegalArgumentException("Byte Char nao encontrado : " + pb);
		}

		return ret;
	}

	public byte[] stringToBytes(String s, int eLimite) {
		byte[] result = new byte[eLimite];

		int tamanho = s.length();

		if (tamanho > eLimite) {
			throw new IllegalArgumentException("String alem do limite : " + tamanho);
		} else {

			int procurando = 0;

			while (procurando < tamanho) {

				byte v = getChar(String.valueOf(s.charAt(procurando)));
				result[procurando] = v;

				procurando += 1;
			}

		}

		return result;
	}

	public byte[] writeString(String s, int eLimite) {

		return stringToBytes(s, eLimite);

	}

	public String readString(byte[] leitor, int index, int eLimite) {

		String ret = "";
		byte[] result = new byte[eLimite];

		long tamanho = leitor.length;

		int cur = 0;
		int max = eLimite;

		while (index < tamanho) {

			if (cur < max) {

				result[cur] = leitor[index];

			} else {
				break;
			}

			// System.out.println("Cur : " + cur + " -->> " + result[cur]);

			index += 1;
			cur += 1;

		}

		int eIndex = 0;
		while (eIndex < eLimite) {
			byte i = result[eIndex];
			// System.out.println("Passando : " + i);
			if (i == 0) {
				break;
			} else {
				ret += charFromByte(i);
			}
			eIndex += 1;
		}

		return ret;
	}

}
