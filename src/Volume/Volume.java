package Volume;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import Tronarko.Tronarko;

public class Volume {

	private FileBinary mFileBinary = null;

	private long mMapaInicio;
	private long mMapaFim;
	private long mMapaTamanho;

	private long P1;
	private long P2;
	private long P3;
	private long mInicializador;

	private String mCabecalho;

	private long mClusterPorcao;

	private SuperBloco SuperBloco_Raiz;
	private Raiz mRaiz;

	private long[] mCache;
	private long mCacheUltimoAlocado;
	private boolean mCacheAtivado;

	private Tronarko mTronarko;
	
	public Volume(String eLocal) {

		mTronarko = new Tronarko();
		
		System.out.println(" Volume --> ABRIR : ");
		System.out.println("");

		mClusterPorcao = 4 * 1024;

		mCache = new long[0];
		mCacheAtivado = false;

		try {

			mFileBinary = new FileBinary(new RandomAccessFile(new File(eLocal), "rw"));

			mFileBinary.seek(0);

			byte v1 = mFileBinary.readByte();
			byte v2 = mFileBinary.readByte();
			byte v3 = mFileBinary.readByte();

			mCabecalho = byteToInt(v1) + "." + byteToInt(v2) + "." + byteToInt(v3);

			P1 = mFileBinary.getPosition();

			mMapaTamanho = mFileBinary.readLong();

			// System.out.println("\t P1 : " + P1);

			P2 = mFileBinary.getPosition();

			// System.out.println("\t P2 : " + P2);

			mInicializador = mFileBinary.readLong();

			P3 = mFileBinary.getPosition();

			// System.out.println("\t P3 : " + P3);

			mMapaInicio = mFileBinary.getPosition();

			mMapaFim = mMapaInicio + mMapaTamanho;

			// mostrar_mapa();

			// mostrar_mapaSeccionado(0);

			SuperBloco_Raiz = new SuperBloco(this, mFileBinary, mInicializador);

			mRaiz = new Raiz(this, SuperBloco_Raiz);

			// mRaiz.criarArquivo("arquivo1.txt");

			// mRaiz.ler();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public Tronarko getTronarko() {return mTronarko;}
	
	public void expandirmuito() {

		mRaiz.expandirmuito();
		mRaiz.debug();

	}

	public int byteToInt(byte b) {
		int r = 0;

		if (b < 0) {
			r = 256 + b;
		} else {
			r = b;
		}
		return r;

	}

	public String getCabecalho() {
		return mCabecalho;
	}

	public long getClusters_Inicio() {
		return mMapaInicio;
	}

	public long getClusters_Fim() {
		return mMapaFim;
	}

	public long getClusters_Quantidade() {
		return mMapaTamanho;
	}

	public long getInicializador() {
		mFileBinary.seek(P2);
		return mFileBinary.readLong();
	}

	public long getClusterPorcao() {
		return mClusterPorcao;
	}

	public long alocar() {

		boolean mEnc = false;
		long mAlocado = 0;

		boolean mCachear = false;

		if (mCache.length > 0) {

			System.out.println("CLUSTER CACHE - PROCURAR");

			int mIndex = 0;
			long mQuantidade = mCache.length;
			while (mIndex < mQuantidade) {

				mFileBinary.seek(mMapaInicio + mCache[mIndex]);
				int valor = mFileBinary.readByte();
				if (valor == 1) {
					mAlocado = mCache[mIndex];
					mEnc = true;
					break;
				}

				mIndex += 1;
			}

			if (!mEnc) {
				mCachear = true;
				System.out.println("CLUSTER CACHE - RUIM");
			}

		} else {
			mCachear = true;
		}

		if (!mEnc) {

			System.out.println("CLUSTER - PROCURAR ULTIMO CASH");

			mFileBinary.seek(mMapaInicio);

			if (mCacheAtivado) {
				mFileBinary.seek(mMapaInicio + mCacheUltimoAlocado);

			} else {
				mCacheAtivado = true;
			}

			long mIndex = mCacheUltimoAlocado;
			long mIndexInicio = mMapaInicio + mCacheUltimoAlocado;

			mFileBinary.seek(mIndexInicio);

			while (mIndexInicio < mMapaFim) {

				int valor = mFileBinary.readByte();

				if (valor == 1) {
					mAlocado = mIndex;
					mCacheUltimoAlocado = mAlocado;
					mEnc = true;
					break;
				}

				mIndex += 1;
				mIndexInicio += 1;
			}

			if (!mEnc) {

				System.out.println("CLUSTER - PROCURAR INICIO");

				mCacheUltimoAlocado = 0;
				mIndex = 0;
				mIndexInicio = mMapaInicio;
				mFileBinary.seek(mIndexInicio);

				while (mIndexInicio < mMapaFim) {

					int valor = mFileBinary.readByte();

					if (valor == 1) {
						mAlocado = mIndex;
						mCacheUltimoAlocado = mAlocado;
						mEnc = true;
						break;
					}

					mIndex += 1;
					mIndexInicio += 1;
				}
			}

		}

		if (mEnc) {
			mFileBinary.seek(mMapaInicio + mAlocado);
			mFileBinary.writeByte((byte) 2);

			if (mCachear) {

				System.out.println("CLUSTER CACHE - CRIAR");

				int eCacheQuantidade = 5;
				int eCacheObtidos = 0;

				mCache = new long[eCacheQuantidade];
				long mIndex = mAlocado;

				mFileBinary.seek(mMapaInicio + mAlocado);

				while (mIndex < mMapaTamanho) {

					int valor = mFileBinary.readByte();

					if (eCacheObtidos >= eCacheQuantidade) {
						break;
					}

					if (valor == 1) {
						mCache[eCacheObtidos] = mIndex;
						eCacheObtidos += 1;
					}

					mIndex += 1;
				}

				if (eCacheObtidos >= eCacheQuantidade) {

				} else {
					mCache = new long[0];

					System.out.println("CLUSTER CACHE - ACABANDO");

				}

			}

		} else {
			throw new RuntimeException("Nao existe espaço ...");
		}

		return mAlocado;
	}
	
	public void devolver(long eID) {
		
		mFileBinary.seek(mMapaInicio);
		
		if ( eID>=0 && eID < mMapaTamanho) {
			
			mFileBinary.seek(mMapaInicio+eID);
			mFileBinary.writeByte((byte)1);
		}
		
	}

	public long getClusters_Ocupados() {

		long mIndex = 0;
		long mContando = 0;

		mFileBinary.seek(mMapaInicio);

		while (mIndex < mMapaTamanho) {

			int valor = mFileBinary.readByte();

			if (valor == 1) {
			} else {
				mContando += 1;
			}

			mIndex += 1;
		}

		return mContando;
	}

	public long getClusters_Livres() {

		long mIndex = 0;
		long mContando = 0;

		mFileBinary.seek(mMapaInicio);

		while (mIndex < mMapaTamanho) {

			int valor = mFileBinary.readByte();

			if (valor == 1) {
				mContando += 1;
			}

			mIndex += 1;
		}

		return mContando;
	}

	public void mostrar_mapa() {

		mFileBinary.seek(mMapaInicio);

		long mIndex = mMapaInicio;

		int mQuadro = 100;
		int mQuadroIndex = 0;

		System.out.println("\t ################# CLUSTERS ####################");
		System.out.print("\n\t");
		
		while (mIndex < mMapaFim) {

			System.out.print(mFileBinary.readByte() + " ");

			mIndex += 1;
			mQuadroIndex += 1;

			if (mQuadroIndex >= mQuadro) {
				System.out.print("\n\t");
				mQuadroIndex = 0;
			}

		}

		System.out.println("\n\t################################################");

	}

	public void mostrar_mapaSeccionado(int eSessao) {

		mFileBinary.seek(mMapaInicio);

		long mIndex = mMapaInicio;

		int mQuadro = 50;
		int mQuadroIndex = 0;

		long eSessaoInicio = mIndex + eSessao * (300);
		long eSessaoFim = eSessaoInicio + 300;

		// System.out.println("Mapa Inicio : " +mMapaInicio );

		System.out.print("\n");
		System.out.println("\t###################################### CLUSTERS - SESSAO : " + eSessao
				+ " ######################################");
		System.out.print("\t");

		while (mIndex < mMapaFim) {

			int b = mFileBinary.readByte();

			if (mIndex >= eSessaoInicio && mIndex < eSessaoFim) {
				System.out.print(b + " ");
			}

			mIndex += 1;

			if (mIndex >= eSessaoInicio && mIndex < eSessaoFim) {
				mQuadroIndex += 1;

				if (mQuadroIndex >= mQuadro) {
					System.out.print("\n\t");
					mQuadroIndex = 0;
				}
			}

		}

		System.out.println(
				"\n\t###################################################################################################");

	}

	public void criarSuperBloco(long eID, String eNome) {

		long mInicio = getClusters_Fim() + (eID * this.getClusterPorcao());
		long mFim = mInicio + this.getClusterPorcao();

		mFileBinary.seek(mInicio);

		Escritor mEscritor = new Escritor();

		mFileBinary.writeBuffer(mEscritor.writeString(eNome, 100));
		mFileBinary.writeLong(0); // DDC
		mFileBinary.writeLong(0); // DDM
		mFileBinary.writeLong(0); // TU

	}

	public void organizarSuperBlocoIndireto(long eID) {

		long mInicio = getClusters_Fim() + (eID * this.getClusterPorcao());

		mFileBinary.seek(mInicio);

		mFileBinary.writeLong(0); // INDICE CONTADOR

	}

	public void criarArquivo(String eNome) {

		mRaiz.criarArquivo(eNome);

	}

	public ArrayList<Arquivo> getArquivos() {
		return mRaiz.getArquivos();
	}

	public void fechar() {

		mFileBinary.close();

	}

	public void dump(byte[] mBytes) {

		int eIndex = 0;
		int eQ = 50;
		int eQi = 0;

		int eFim = mBytes.length;

		while (eIndex < eFim) {

			if (eQi >= eQ) {
				eQi = 0;
				System.out.print("\n");

			}

			System.out.print(organizar(mBytes[eIndex]) + " ");

			eIndex += 1;
			eQi += 1;

		}

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

	public void mostrar_arquivos() {

		for (Arquivo ma : getArquivos()) {
			System.out.println("Arquivo :: " + ma.getNome() + " -> " + ma.getSuperBloco());
		}

	}

	public boolean existeArquivo(String eNome) {

		boolean ret = false;

		for (Arquivo ma : getArquivos()) {
			if (ma.getNome().contentEquals(eNome)) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	public Arquivo getArquivo(String eNome) {

		Arquivo ret = null;

		for (Arquivo ma : getArquivos()) {
			if (ma.getNome().contentEquals(eNome)) {
				ret = ma;
				break;
			}
		}

		return ret;
	}

	public void teste() {

		String t = "texto 5.txt";

		if (existeArquivo(t)) {

			Arquivo a = this.getArquivo(t);

			System.out.println("Arquivo : " + a.getNome());
			System.out.println("\t - Bloco ID : " + a.getSuperBloco());
			System.out.println("\t - Tamanho Antes : " + a.getTamanho() + " -->> " + formatarTamanho(a.getTamanho()));

			//a.aumentar(5000);

			System.out.println("\t - Tamanho Depois : " + a.getTamanho() + " -->> " + formatarTamanho(a.getTamanho()));
			System.out.println("\t - DDC : " + formatarTron(a.getDDC()));
			System.out.println("\t - DDM : " + formatarTron(a.getDDM()));

			a.debug();
			
			a.limpar();
			
			a.debug();

		} else {
			System.out.println("Arquivo : " + t + " -->> NAO EXISTE !");
		}

	}

	public String formatarTron(long eTron) {
	return mTronarko.setLong(eTron);
	}
	
	public String formatarTamanho(long eTamanho) {
		String sufixo = "bytes";
		
		if (eTamanho>=1024) {
			
			sufixo="kb";
			int eT = 0;
			
			while(eTamanho>=1024){
				eTamanho-=1024;
				eT+=1;
			}
			
			eTamanho=eT;
			
		}
	
		
		return eTamanho + " " + sufixo;
		
	}
}
