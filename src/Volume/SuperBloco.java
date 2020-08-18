package Volume;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SuperBloco {

	private Volume mVolume;
	private FileBinary mFileBinary;

	private long mID;


	private long mPorcao;
	private long mInicio;
	private long mFim;

	Escritor mEscritor;

	public SuperBloco(Volume eVolume, FileBinary eFileBinary, long eID) {

		mVolume = eVolume;
		mFileBinary = eFileBinary;
		mID = eID;

		// SUPERBLOCO -> Contem 512 Longs
		// Primeiro Long : Guarda a Quantidade de Blocos Diretos;
		// Se o primeiro long for igual a 510, o bloco 510 e indireto
		// se o primeiro long for igual a 511, o bloco 511 e duplamente indireto

		mPorcao = eVolume.getClusterPorcao();

		mEscritor = new Escritor();

		if (eID > 0 && eID < mVolume.getClusters_Quantidade()) {
			mInicio = mVolume.getClusters_Fim() + (eID * mPorcao);
			mFim = mInicio + mPorcao;

			mFileBinary.seek(mInicio);

			long mIndex = mInicio;

			byte[] mNome = mFileBinary.readBuffer(100);

			long DDC = mFileBinary.readLong();
			long DDM = mFileBinary.readLong();
			long mUsado = mFileBinary.readLong();

			// debug();

		} else {
			throw new RuntimeException("SuperBloco " + eID + " nao existente !");
		}

	}

	
	
	
	public void debug() {

		System.out.println("Abrindo SuperBloco :: " + mID);
		System.out.println(" - Inicio :: " + mInicio);
		System.out.println(" - Fim :: " + mFim);
		System.out.println(" - Porcao :: " + mPorcao);

		System.out.println(" - Nome :: " + this.getNome());
		System.out.println(" - DDC :: " + this.getDDC());
		System.out.println(" - DDM :: " + this.getDDM());
		System.out.println(" - Usado :: " + this.getUsado());
		System.out.println(" - Tamanho :: " + this.getTamanho());


		System.out.println(" - Blocos Diretos " + this.getBlocos() + " -->> { " + getBlocosDiretosString() + "}");


		if (this.getBlocos() >= 491) {
			System.out.println(" - Possui Indireto :: SIM -->> " + this.getBlocoIndireto());

			BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, this.getBlocoIndireto());

			System.out.println(" - Blocos InDiretos " + mBlocoIndireto.getBlocos() + " -->> { " + mBlocoIndireto.getBlocosString() + "}");

			
			if (this.getBlocos() >= 492) {
				
				System.out.println(" - Possui Bi-Indireto :: SIM -->> " + this.getBlocoBiIndireto());

				BlocoBiIndireto mBlocoBiIndireto = new BlocoBiIndireto(mVolume, mFileBinary, this.getBlocoBiIndireto());

				System.out.println(" - Blocos Bi-InDiretos " + mBlocoBiIndireto.getBlocos() + " -->> { " + mBlocoBiIndireto.getReferenicasString() + "}");

				
			}else {
				
				System.out.println(" - Possui Bi-Indireto :: NAO");

			}
			
		} else {
			System.out.println(" - Possui Indireto :: NAO");
		}

	}

	public FileBinary getFileBinary() {
		return mFileBinary;
	}



	public long getBlocoIndireto() {

		if (this.getBlocos() >= 491) {
			mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8));

			long eUltimo = mFileBinary.readLong();

			return eUltimo;
		} else {
			throw new RuntimeException("SuperBloco nao possui bloco indireto");
		}

	}
	
	
	public long getBlocoBiIndireto() {

		if (this.getBlocos() >= 492) {
			mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8)+8);

			long eUltimo = mFileBinary.readLong();

			return eUltimo;
		} else {
			throw new RuntimeException("SuperBloco nao possui bloco Bi-indireto");
		}

	}
	
	

	public long[] getBlocosDiretos() {

		long[] mIds = null;

		mFileBinary.seek(mInicio);

		mFileBinary.readBuffer(100);

		mFileBinary.readLong();
		mFileBinary.readLong();
		mFileBinary.readLong();

		int tmpQuantos = (int) mFileBinary.readLong();

		int mMaximo = 490;

		if (tmpQuantos > mMaximo) {
			tmpQuantos = mMaximo;
		}

		mIds = new long[(int) tmpQuantos];

		int mIndex = 0;
		while (mIndex < tmpQuantos) {
			mIds[mIndex] = mFileBinary.readLong();
			mIndex += 1;
		}

		return mIds;
	}

	public long[] getIdentificadores() {

		long[] mIds = null;

		mFileBinary.seek(mInicio);

		byte[] mNome = mFileBinary.readBuffer(100);

		mFileBinary.readLong();
		mFileBinary.readLong();
		mFileBinary.readLong();

		int mQuantos = (int) mFileBinary.readLong();

		// System.out.println("SuperBloco :: Obter Identificadores");

		// System.out.println(" Quantos :: " +mQuantos );

		int mMaximo = 490;
		if (mQuantos <= mMaximo) {

			mIds = new long[(int) mQuantos];

			int mIndex = 0;
			while (mIndex < mQuantos) {
				mIds[mIndex] = mFileBinary.readLong();

				// System.out.println(mIndex + " :: " +mIds[mIndex] );

				mIndex += 1;
			}

		} else if (mQuantos == 491) {

			mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8));

			long eUltimo = mFileBinary.readLong();

			System.out.println("Bloco Indireto -->> " + eUltimo);

			BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, eUltimo);

			System.out.println("Bloco Indireto -->> Quantos " + mBlocoIndireto.getBlocos());

			mIds = new long[(490) + (int) (mBlocoIndireto.getBlocos())];

			int mIndex = 0;
			while (mIndex < 490) {
				mIds[mIndex] = mFileBinary.readLong();
				mIndex += 1;
			}

			long[] mIndiretosIds = mBlocoIndireto.getIdentificadores();

			int o = mIndiretosIds.length;
			int e = 0;
			while (e < o) {
				mIds[mIndex] = mIndiretosIds[e];
				mIndex += 1;
				e += 1;
			}

	
			
		} else if (mQuantos == 492) {

			mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8));

			BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, this.getBlocoIndireto());


			BlocoBiIndireto mBlocoBiIndireto = new BlocoBiIndireto(mVolume, mFileBinary, this.getBlocoBiIndireto());

			long[] mIndiretosIds = mBlocoIndireto.getIdentificadores();
			long[] mBiIndiretosIds = mBlocoBiIndireto.getIdentificadores();

			int t1 = mIndiretosIds.length;
			int t2 = mBiIndiretosIds.length;
			
			mIds = new long[(490) + t1 +t2];

			int mIndex = 0;
			while (mIndex < 490) {
				mIds[mIndex] = mFileBinary.readLong();
				mIndex += 1;
			}


			int o = mIndiretosIds.length;
			int e = 0;
			while (e < o) {
				mIds[mIndex] = mIndiretosIds[e];
				mIndex += 1;
				e += 1;
			}

			 o = mBiIndiretosIds.length;
			 e = 0;
			while (e < o) {
				mIds[mIndex] = mBiIndiretosIds[e];
				mIndex += 1;
				e += 1;
			}
			
			
		} else {
			throw new RuntimeException("SuperBloco com mais de 490 indices...");
		}

		return mIds;
	}

	public byte[] getAllBytes() {

		long[] mIds = getIdentificadores();

		 System.out.println("Identificadores Tamanho : " + mIds.length);

		byte[] mBytes = new byte[(int) (((long) mIds.length) * mPorcao)];


		int eIndex = 0;
		int eFim = mIds.length;

		int ePos = 0;

		System.out.println("Ler SuperBloco :: " + mID);

		while (eIndex < eFim) {

			long eID = mIds[eIndex];

			// System.out.println(" - " +eID );

			long mBuscaInicio = mVolume.getClusters_Fim() + (eID * mPorcao);
			long mBuscaFim = mBuscaInicio + mPorcao;
			long mBuscaIndex = mBuscaInicio;

			mFileBinary.seek(mBuscaInicio);

			while (mBuscaIndex < mBuscaFim) {

				mBytes[ePos] = mFileBinary.readByte();

				ePos += 1;
				mBuscaIndex += 1;
			}

			eIndex += 1;
		}

		return mBytes;
	}

	public void expandir() {

		mFileBinary.seek(mInicio);

		byte[] mNome = mFileBinary.readBuffer(100);

		mFileBinary.readLong();
		mFileBinary.readLong();
		mFileBinary.readLong();

		int eQuantos = (int) mFileBinary.readLong();

		// Ultimo Bloco 495

		// Ultimo Bloco Direto 490
		// Bloco Indireto 491

		int mMaximo = 490;
		if (this.getBlocos() < mMaximo) {

			System.out.println("SuperBloco " + mID + " :: Comecar expansao...");
			System.out.println(" - Antes Quantos : " + this.getBlocos());

			long enovoID = mVolume.alocar();

			mFileBinary.seek(mInicio);

			mNome = mFileBinary.readBuffer(100);

			mFileBinary.readLong(); // DDC
			mFileBinary.readLong(); // DDM
			mFileBinary.readLong(); // TU

			eQuantos = (int) mFileBinary.readLong();

			for (int a = 0; a < eQuantos; a++) {
				mFileBinary.readLong();
			}

			mFileBinary.writeLong(enovoID);

			int mQuantos = (int) this.getBlocos() + 1;

			mFileBinary.seek(mInicio);

			mNome = mFileBinary.readBuffer(100);

			mFileBinary.readLong(); // DDC
			mFileBinary.readLong(); // DDM
			mFileBinary.readLong(); // TU

			mFileBinary.writeLong(mQuantos);


			System.out.println(" - Depois Quantos : " + mQuantos);

		} else if (this.getBlocos() == mMaximo) {

			// Alocar Bloco Indireto

			System.out.println("SuperBloco " + mID + " :: Comecar expansao indireta ...");
		
			long enovoID = mVolume.alocar();

			mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8));

			mFileBinary.writeLong(enovoID);

			int mQuantos = eQuantos + 1;


			mFileBinary.seek(mInicio);

			mNome = mFileBinary.readBuffer(100);

			mFileBinary.readLong(); // DDC
			mFileBinary.readLong(); // DDM
			mFileBinary.readLong(); // TU

			mFileBinary.writeLong(mQuantos);


			mVolume.organizarSuperBlocoIndireto(enovoID);

			BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, enovoID);

			mBlocoIndireto.expandir();

		} else if (this.getBlocos() == 491) {

			debug();

			System.out.println("SuperBloco " + mID + " :: Comecar expansao -> INDIRETA ");

			long mUltimo = this.getBlocoIndireto();

			System.out.println("Bloco Indireto :: " + mUltimo);

			BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, mUltimo);

			if (mBlocoIndireto.podeExpandir()) {
				mBlocoIndireto.expandir();
			} else {

				System.out.println("Comecar expansao Bi-indireta ...");
			
				long enovoID = mVolume.alocar();

				mFileBinary.seek(mInicio + 100 + (4 * 8) + (490 * 8) +8);

				mFileBinary.writeLong(enovoID);

				int mQuantos = eQuantos + 1;


				mFileBinary.seek(mInicio);

				mNome = mFileBinary.readBuffer(100);

				mFileBinary.readLong(); // DDC
				mFileBinary.readLong(); // DDM
				mFileBinary.readLong(); // TU

				mFileBinary.writeLong(mQuantos);


				System.out.println(" - Depois Quantos : " + mQuantos);
				
				mVolume.organizarSuperBlocoIndireto(enovoID);

				BlocoBiIndireto mBlocoBiIndireto = new BlocoBiIndireto(mVolume, mFileBinary, enovoID);

				mBlocoBiIndireto.expandir();
				
			}
		} else if (this.getBlocos() == 492) {


			debug();

			System.out.println("SuperBloco " + mID + " :: Comecar expansao -> BI-INDIRETA ");

			long mRegiao492 = this.getBlocoBiIndireto();

			System.out.println("Bloco Bi-Indireto :: " + mRegiao492);

			BlocoBiIndireto mBlocoBiIndireto = new BlocoBiIndireto(mVolume, mFileBinary, mRegiao492);

			if (mBlocoBiIndireto.podeExpandir()) {
				mBlocoBiIndireto.expandir();
			} else {
				
				throw new RuntimeException("NAO EXISTE EXPANSAO ALEM DO BI-INDIRETO");

			}
			
			
		} else {
			throw new RuntimeException("SuperBloco precisa expandir, contudo esta com maximo de indices : 490 ");
		}

	}

	public void atualizar() {

		mFileBinary.seek(mInicio);

		byte[] mNome = mFileBinary.readBuffer(100);

		mFileBinary.readLong(); // DDC
		mFileBinary.readLong(); // DDM

		mFileBinary.writeLong(this.getTamanho());

	}

	public String getNome() {

		mFileBinary.seek(mInicio);
		byte[] mNome = mFileBinary.readBuffer(100);

		Escritor mEscritor = new Escritor();

		return mEscritor.readString(mNome, 0, 100);

	}

	public long getDDC() {

		mFileBinary.seek(mInicio + 100);
		return mFileBinary.readLong();

	}

	public long getDDM() {

		mFileBinary.seek(mInicio + 100 + 8);
		return mFileBinary.readLong();

	}

	public long getUsado() {

		mFileBinary.seek(mInicio + 100 + 8 + 8);
		return mFileBinary.readLong();

	}

	public long getBlocos() {

		mFileBinary.seek(mInicio + 100 + 8 + 8 + 8);
		return mFileBinary.readLong();

	}

	public void atualizarCom(long eCom) {

		mFileBinary.seek(mInicio);

		byte[] mNome = mFileBinary.readBuffer(100);

		mFileBinary.readLong(); // DDC
		//mFileBinary.readLong(); // DDM

		long total = mVolume.getTronarko().getLong (mVolume.getTronarko().getTozte(),mVolume.getTronarko().getHazde());

		mFileBinary.writeLong(total);

		
		mFileBinary.writeLong(eCom);

	}

	public void setDDC(long eCom) {

		mFileBinary.seek(mInicio);

		byte[] mNome = mFileBinary.readBuffer(100);
		mFileBinary.writeLong(eCom);

		
	}
	
	
	public void escrever(byte[] dados) {

		int mDadosTamanho = dados.length;

		while (mDadosTamanho >= getTamanho()) {

			System.out.println("Precisa expandir ->> tem " + getTamanho() + " e precisa de " + mDadosTamanho);

			expandir();

		}

		long[] mIds = getIdentificadores();

		// System.out.println("Identificadores Tamanho : " + mIds.length);

		int eIndex = 0;
		int eFim = dados.length;

		int ePos = 0;
		int mEscreverID = 0;

		long mBuscaInicio = mVolume.getClusters_Fim() + (mIds[mEscreverID] * mPorcao);

		mFileBinary.seek(mBuscaInicio);

		while (eIndex < eFim) {

			if (ePos >= mPorcao) {

				mEscreverID += 1;
				mBuscaInicio = mVolume.getClusters_Fim() + (mIds[mEscreverID] * mPorcao);

				mFileBinary.seek(mBuscaInicio);
				ePos = 0;

				// System.out.println("Gravar no Bloco : " + mIds[mEscreverID]);

			}

			mFileBinary.writeByte(dados[eIndex]);

			eIndex += 1;
			ePos += 1;

		}

	}

	public long getTamanho() {
		return getIdentificadores().length * mPorcao;
	}
	
	public void escreverPos(long iniciarPos, byte[] dados) {

		long mDadosTamanho = iniciarPos + (long) dados.length;

		
		while (mDadosTamanho >= getTamanho()) {

			System.out.println("Precisa expandir ->> tem " + getTamanho() + " e precisa de " + mDadosTamanho);

			expandir();

		}

		long[] mIds = getIdentificadores();

		// System.out.println("Identificadores Tamanho : " + mIds.length);

		int eIndex = 0;
		long eFim = mDadosTamanho;

		int ePos = 0;
		int mEscreverID = 0;

		long mBuscaInicio = mVolume.getClusters_Fim() + (mIds[mEscreverID] * mPorcao);

		mFileBinary.seek(mBuscaInicio);

		int eIndexDentro = 0;

		while (eIndex < eFim) {

			if (ePos >= mPorcao) {

				mEscreverID += 1;
				mBuscaInicio = mVolume.getClusters_Fim() + (mIds[mEscreverID] * mPorcao);

				mFileBinary.seek(mBuscaInicio);
				ePos = 0;

				// System.out.println("Gravar no Bloco : " + mIds[mEscreverID]);

			}

			if (eIndex >= iniciarPos) {
				mFileBinary.writeByte(dados[eIndexDentro]);
				eIndexDentro += 1;
			} else {
				mFileBinary.readByte();
			}

			eIndex += 1;
			ePos += 1;

		}

	}

	public void dump() {

		mVolume.dump(getAllBytes());

	}
	
	
	public String getBlocosDiretosString() {
		
		
		String ret = "";
		
		long[] blocos = this.getBlocosDiretos();
		int bi = 0;
		int bo = blocos.length;

		while (bi < bo) {

			ret += (blocos[bi] + " ");

			bi += 1;
		}
		
		return ret;
	}
	
	public void limpar() {
		
		
		System.out.println("SuperBlock " + mID + " -->> Limpar");
		
		long[] mIds = getIdentificadores();

		int eIndex = 0;
		long eFim = mIds.length;
		
		while (eIndex < eFim) {
			
			long cur = mIds[eIndex];
				
			mVolume.devolver(cur);
			
			eIndex+=1;
		}
		
		long total = mVolume.getTronarko().getLong (mVolume.getTronarko().getTozte(),mVolume.getTronarko().getHazde());

		mFileBinary.seek(mInicio);
		
		mFileBinary.readBuffer(100);

		mFileBinary.readLong(); // DDC
		
		
		mFileBinary.writeLong(total); // DDM
		mFileBinary.writeLong(0); // TU
		mFileBinary.writeLong(0); // Blocos


		
		
	}
	

}
