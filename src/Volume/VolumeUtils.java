package Volume;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class VolumeUtils {

	public static void alocar(String eLocal, long eTamanho) {

		try {

			RandomAccessFile raf = new RandomAccessFile(new File(eLocal), "rw");

			raf.seek(0);

			long mIndex = 0;
			while (mIndex < eTamanho) {
				raf.write(0);
				mIndex += 1;
			}

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void zerar(String eLocal) {

		try {

			RandomAccessFile raf = new RandomAccessFile(new File(eLocal), "rw");

			raf.seek(0);

			long eTamanho = raf.length();

			boolean temQ = false;
			long Q2 = 0;
			long Q3 = 0;
			long Q4 = 0;
			long Q5 = 0;
			if (eTamanho > 1024) {
				temQ = true;
				Q2 = eTamanho / 4;
				Q3 = Q2 + Q2;
				Q4 = Q2 + Q2 + Q2;
				Q5 = eTamanho - 1;
			}

			long mIndex = 0;

			if (temQ) {

				while (mIndex < eTamanho) {

					if (mIndex == Q2) {
						System.out.println("\t - Zerando 25 %");
					} else if (mIndex == Q3) {
						System.out.println("\t - Zerando 50 %");
					} else if (mIndex == Q4) {
						System.out.println("\t - Zerando 75 %");
					} else if (mIndex == Q5) {
						System.out.println("\t - Zerando 100 %");
					}

					raf.write(0);
					mIndex += 1;
				}

			} else {
				while (mIndex < eTamanho) {
					raf.write(0);
					mIndex += 1;
				}
			}

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void formatar(String eLocal) {

		System.out.println(" Volume --> FORMATAR : ");
		System.out.println("");

		try {

			FileBinary mFileBinary = new FileBinary(new RandomAccessFile(new File(eLocal), "rw"));

			mFileBinary.seek(0);

			long eTamanho = mFileBinary.getLength();

			System.out.println("\t Tamanho : " + eTamanho);

			mFileBinary.writeByte((byte) 200);
			mFileBinary.writeByte((byte) 100);
			mFileBinary.writeByte((byte) 50);

			eTamanho -= 3;

			System.out.println("\t Sobrou 1 : " + eTamanho);

			long P1 = mFileBinary.getPosition();

			System.out.println("\t P1 : " + P1);

			mFileBinary.writeLong(0);

			long P2 = mFileBinary.getPosition();

			System.out.println("\t P2 : " + P2);

			mFileBinary.writeLong(0);

			long P3 = mFileBinary.getPosition();

			System.out.println("\t P3 : " + P3);

			long eRestante = mFileBinary.getLength() - mFileBinary.getPosition();

			System.out.println("\t Restante : " + eRestante);

			// System.out.println("Teste Usado :: " + (10485760-10485749));

			long eBlocoTamanho = 4 * 1024;
			long eBlocoQuantidade = 0;

			while (eRestante > eBlocoTamanho) {
				eRestante -= eBlocoTamanho;
				eBlocoQuantidade += 1;
			}

			System.out.println("\t Bloco Tamanho : " + eBlocoTamanho);
			System.out.println("\t Bloco Quantidade : " + eBlocoQuantidade);

			long mMapaInicio = mFileBinary.getPosition();

			System.out.println("\t MAPA INICIO : " + mMapaInicio);

			long mMapeadorIndex = 0;
			long mMapeadorClusterIndex = 0;

			long mMapeadorClusters = 0;

			while (mMapeadorIndex < eBlocoQuantidade) {

				mFileBinary.writeByte((byte) 1);

				mMapeadorIndex += 1;
				mMapeadorClusterIndex += 1;

				if (mMapeadorClusterIndex >= eBlocoTamanho) {
					mMapeadorClusterIndex = 0;
					mMapeadorClusters += 1;
				}
			}

			mMapeadorClusters += 1;

			long mMapaFim = mFileBinary.getPosition();

			System.out.println("\t MAPA FIM : " + mMapaFim);
			System.out.println("\t MAPA CLUSTERS : " + mMapeadorClusters);

			System.out.println("\t POSICAO : " + mFileBinary.getPosition());

			mFileBinary.seek(P1);
			mFileBinary.writeLong(eBlocoQuantidade);

			mFileBinary.seek(mMapaInicio);

			// MAPEAR CLUSTER MAP
			mMapeadorClusterIndex = 0;
			while (mMapeadorClusterIndex < mMapeadorClusters) {

				mFileBinary.writeByte((byte) 2);

				mMapeadorClusterIndex += 1;
			}

			// PROCURAR LIVRE
			mMapeadorIndex = 0;
			long mInicializador = 0;

			mFileBinary.seek(mMapaInicio);

			while (mMapeadorIndex < eBlocoQuantidade) {

				int valor = mFileBinary.readByte();

				if (valor == 1) {
					mInicializador = mMapeadorIndex;
					break;
				}

				mMapeadorIndex += 1;
			}

			mFileBinary.seek(P2);
			mFileBinary.writeLong(mInicializador);

			System.out.println("\t Inicializador : " + mInicializador);

			mFileBinary.seek(mMapaInicio+mInicializador);
			mFileBinary.writeByte((byte) 2);

			
			
			mMapeadorIndex = 0;
			long mGuardar = 0;

			mFileBinary.seek(mMapaInicio);

			while (mMapeadorIndex < eBlocoQuantidade) {

				int valor = mFileBinary.readByte();

				if (valor == 1) {
					mGuardar = mMapeadorIndex;
					break;
				}

				mMapeadorIndex += 1;
			}
			
			mFileBinary.seek(mMapaInicio+mGuardar);
			mFileBinary.writeByte((byte) 2);

			
			superbloco_formatar(mFileBinary, eBlocoQuantidade, mMapaFim, mInicializador,mGuardar);

			mFileBinary.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static void superbloco_formatar(FileBinary mFileBinary, long mClusters, long eDadosInicio, long eID,long mGuardar) {

		if (eID > 0 && eID < mClusters) {
			long mTamanho = 4 * 1024;
			long mInicio = eDadosInicio + (eID * mTamanho);
			long mFim = mInicio + mTamanho;

			System.out.println("SuperBloco :: " + eID);
			System.out.println(" - Inicio :: " + mInicio);
			System.out.println(" - Fim :: " + mFim);
			System.out.println(" - Tamanho :: " + mTamanho);

			mFileBinary.seek(mInicio);

			long mIndex = mInicio;
			
			while (mIndex < mFim) {

				 mFileBinary.writeByte((byte)0);

				mIndex += 1;
			}
			
			mFileBinary.seek(mInicio);

			for(int i=0;i<512;i++) {
				mFileBinary.writeLong(0);
			}
			
			// CRIAR SUPERBLOCO
			
			mFileBinary.seek(mInicio);
			
			Escritor mEscritor = new Escritor();
			
			for (byte b : mEscritor.stringToBytes("ROOT", 100)) {
				mFileBinary.writeByte(b); // NOME
			}
			
			mFileBinary.writeLong(0); // DDC
			mFileBinary.writeLong(0); // DDM
			mFileBinary.writeLong(0); // TU

			mFileBinary.writeLong(1);
			mFileBinary.writeLong(mGuardar);

			
			long mRealInicio = eDadosInicio + (mGuardar * mTamanho);
			long mRealFim = mRealInicio + mTamanho;
			long mRealIndex = mRealInicio;
			
			mFileBinary.seek(mRealInicio);

			while (mRealIndex < mRealFim) {

				 mFileBinary.writeByte((byte)0);

				 mRealIndex += 1;
			}
			mFileBinary.seek(mRealInicio);
			
			//mFileBinary.writeLong(0);
			
			mFileBinary.writeByte((byte)13);

			
		} else {
			throw new RuntimeException("SuperBloco " + eID + " nao existente !");
		}

	}

}
