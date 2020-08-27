package Volume;

import java.util.ArrayList;

public class Raiz {

	private Volume mVolume;
	private SuperBloco mSuperBloco;
	private ArrayList<Ponto> mPontos;
	private Escritor mEscritor;
	private Longer mLonger;

	public Raiz(Volume eVolume, SuperBloco eSuperBloco) {

		mVolume = eVolume;
		mSuperBloco = eSuperBloco;
		mPontos = new ArrayList<Ponto>();

		mEscritor = new Escritor();
		mLonger = new Longer();
	}

	public void ler() {

		mPontos.clear();

		byte[] s1_bytes = mSuperBloco.getAllBytes();

		// System.out.println("S1 - Tamanho : " + mSuperBloco.getTamanho());
		// System.out.println("S1 - Bytes : " + s1_bytes.length);

		int eIndex = 0;
		int eFim = s1_bytes.length;

		// long ePonteiroInicial = mLonger.readLong(s1_bytes, eIndex);
		// eIndex += 8;

		// PAREI AQUI

		while (eIndex < eFim) {

			byte b = s1_bytes[eIndex];

			if (b == (byte) 13) {
				break;
			} else if (b == (byte) 11) {

				eIndex += 1;

				//String ePontoNome = mEscritor.readString(s1_bytes, eIndex, 100);

				//eIndex += 100;

				long ePontoLocal = mLonger.readLong(s1_bytes, eIndex);
				eIndex += 7;

				//mPontos.add(new Ponto(11, ePontoNome, ePontoLocal));
				mPontos.add(new Ponto(11, ePontoLocal));

				// System.out.println("\t - Existe Arquivo : " + ePontoNome + " -->> " +
				// ePontoLocal);

			}

			// System.out.print(b + " ");
			eIndex += 1;
		}

	}

	public long obterPontoFinal() {

		long ret = -1;

		mPontos.clear();

		byte[] s1_bytes = mSuperBloco.getAllBytes();


		int eIndex = 0;
		int eFim = s1_bytes.length;


		while (eIndex < eFim) {

			byte b = s1_bytes[eIndex];

			if (b == (byte) 13) {
				ret = eIndex;
				break;
			} else if (b == (byte) 11) {

				eIndex += 1;

				//eIndex += 100;

				eIndex += 7;

	

			}

			eIndex += 1;
		}

		return ret;

	}

	public void criarArquivo(String eNome) {

		System.out.println(" ----------------------------------------");

		System.out.println("Criar Arquivo : " + eNome);

		//byte[] guardar = new byte[(1 + 100 + 8) + 1];
		byte[] guardar = new byte[(1  + 8) + 1];

		int mIndex = 0;

		guardar[mIndex] = (byte) 11;
		mIndex += 1;

		//for (byte b : mEscritor.stringToBytes(eNome, 100)) {

			//guardar[mIndex] = b;
			//mIndex += 1;

		//}

		long NovoSuperBloco = mVolume.alocar();

	
		
		
		for (byte b : mLonger.longToBytes(NovoSuperBloco)) {

			guardar[mIndex] = b;
			mIndex += 1;

		}

		guardar[mIndex] = (byte) 13;

		long pontoFinal = obterPontoFinal();
		System.out.println("Ponto Final : " + pontoFinal);

		mSuperBloco.escreverPos(pontoFinal, guardar);

		mSuperBloco.atualizarCom(pontoFinal +(long)guardar.length );

		//SuperBloco mArquivo = new SuperBloco(mVolume, mSuperBloco.getFileBinary(), NovoSuperBloco);

		mVolume.criarSuperBloco( NovoSuperBloco,eNome);
		
		SuperBloco sb = new SuperBloco(mVolume, mSuperBloco.getFileBinary(), NovoSuperBloco);
		
		long total = mVolume.getTronarko().getLong (mVolume.getTronarko().getTozte(),mVolume.getTronarko().getHazde());

		sb.setDDC(total);
		
		mSuperBloco.debug();
		
		
		System.out.println(" ----------------------------------------");

	}

	public void removerArquivo(String eNome) {

		for(Arquivo mArquivo : getArquivos()){

			if (mArquivo.getNome().contentEquals(eNome)){
				mArquivo.limpar();



				break;
			}

		}

	}


		public void expandirmuito() {
	
		mSuperBloco.expandir();
		//mSuperBloco.debug();

	}

	public void debug() {
		mSuperBloco.debug();
	}
	
	public void criarArquivoAntigo(String eNome) {

		System.out.println(" ----------------------------------------");

		System.out.println("Criar Arquivo : " + eNome);

		ler();

		int antes = (mPontos.size()) * (1 + 100 + 8) + 1;
		int depois = antes + (1 + 100 + 8);

		byte[] guardar = new byte[depois];

		// System.out.println(" Antes :: " + antes);
		// System.out.println(" Guardar :: " + depois);

		int mIndex = 0;

		for (Ponto ePonto : mPontos) {

			guardar[mIndex] = (byte) ePonto.getTipo();
			mIndex += 1;

			// DEVE VOLTAR ?
			//for (byte b : mEscritor.stringToBytes(ePonto.getNome(), 100)) {

				//guardar[mIndex] = b;
			//	mIndex += 1;

			//}

			for (byte b : mLonger.longToBytes(ePonto.getLocal())) {

				guardar[mIndex] = b;
				mIndex += 1;

			}

		}

		guardar = new byte[(1 + 100 + 8) + 1];

		mIndex = 0;

		guardar[mIndex] = (byte) 11;
		mIndex += 1;

		for (byte b : mEscritor.stringToBytes(eNome, 100)) {

			guardar[mIndex] = b;
			mIndex += 1;

		}

		long NovoSuperBloco = mVolume.alocar();

		for (byte b : mLonger.longToBytes(NovoSuperBloco)) {

			guardar[mIndex] = b;
			mIndex += 1;

		}

		guardar[mIndex] = (byte) 13;

		// System.out.println("\n SUPERBLOCO \n");
		// mSuperBloco.dump();

		// System.out.println("\n ESCREVER \n");

		// mVolume.dump(guardar);

		long pontoFinal = obterPontoFinal();
		System.out.println("Ponto Final : " + pontoFinal);

		// mSuperBloco.escrever(guardar);

		mSuperBloco.escreverPos(pontoFinal, guardar);

		// System.out.println("Ler depois de gravado");
		// ler();

		System.out.println(" ----------------------------------------");

	}

	public ArrayList<Arquivo> getArquivos() {

		ArrayList<Arquivo> ret = new ArrayList<Arquivo>();

		ler();

		for (Ponto ePonto : mPontos) {

			if (ePonto.getTipo() == 11) {
				ret.add(new Arquivo(mVolume,mSuperBloco.getFileBinary(), ePonto));
			}

		}

		return ret;

	}
}
