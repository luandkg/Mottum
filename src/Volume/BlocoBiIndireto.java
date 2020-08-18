package Volume;

public class BlocoBiIndireto {

	private Volume mVolume;
	private FileBinary mFileBinary;
	private long mID;

	private long mPorcao;
	private long mInicio;
	private long mFim;
	private long mQuantos;

	public BlocoBiIndireto(Volume eVolume, FileBinary eFileBinary, long eID) {

		mVolume = eVolume;
		mFileBinary = eFileBinary;
		mID = eID;

		mPorcao = eVolume.getClusterPorcao();

		if (eID > 0 && eID < mVolume.getClusters_Quantidade()) {
			mInicio = mVolume.getClusters_Fim() + (eID * mPorcao);
			mFim = mInicio + mPorcao;

			mFileBinary.seek(mInicio);

			mQuantos = mFileBinary.readLong();

		}

	}

	public long getBlocos() {

		mFileBinary.seek(mInicio);

		return mFileBinary.readLong();

	}

	public boolean podeExpandir() {

		if (getBlocos() < 500) {
			return true;
		} else {
			return false;
		}

	}

	public void expandir() {

		mFileBinary.seek(mInicio);

		int eQuantos = (int) mFileBinary.readLong();
		int mMaximo = 500;

		if (mQuantos < mMaximo) {

			System.out.println("Comecar expansao Bi-indireta ...");
			System.out.println(" - Antes Quantos : " + this.getBlocos());

			if (eQuantos == 0) {

				long enovoID = mVolume.alocar();

				mFileBinary.seek(mInicio);

				mFileBinary.writeLong(1);

				mFileBinary.writeLong(enovoID);

				mVolume.organizarSuperBlocoIndireto(enovoID);

				BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, enovoID);
				mBlocoIndireto.expandir();

			} else {

				mFileBinary.seek(mInicio + (8) + ((eQuantos - 1) * 8));

				long mUltimo = mFileBinary.readLong();

				BlocoIndireto mBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, mUltimo);

				if (mBlocoIndireto.podeExpandir()) {
					mBlocoIndireto.expandir();
				} else {

					long enovoID = mVolume.alocar();

					mFileBinary.seek(mInicio + (8) + ((eQuantos) * 8));
					mFileBinary.writeLong(enovoID);

					mFileBinary.seek(mInicio);

					mFileBinary.writeLong(eQuantos + 1);

					mVolume.organizarSuperBlocoIndireto(enovoID);

					BlocoIndireto mNovoBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, enovoID);
					mNovoBlocoIndireto.expandir();

				}

			}

			System.out.println(" - Depois Quantos : " + this.getBlocos());

		} else {

			throw new RuntimeException("Bloco Bi-Indireto nao pode expandir alem do maximo de indices : 500 ");

		}

	}

	public long[] getReferenciadores() {

		long[] mIds = null;

		mFileBinary.seek(mInicio);

		mQuantos = (int) mFileBinary.readLong();

		mIds = new long[(int) mQuantos];

		int mIndex = 0;
		while (mIndex < mQuantos) {
			mIds[mIndex] = mFileBinary.readLong();
			mIndex += 1;
		}

		return mIds;

	}

	public long[] getIdentificadores() {


		long[] mRefs = getReferenciadores();

		int mContando = 0;
		int mIndentificadoresTotal = 0;

		int mMaximo = mRefs.length;

		while (mContando < mMaximo) {
			mIndentificadoresTotal += mRefs[mContando];
			mContando += 1;
		}

		long[] mIds = new long[mIndentificadoresTotal];

		mContando=0;
		
		int ma = 0;
		
		while (mContando < mMaximo) {
		
			
			BlocoIndireto mNovoBlocoIndireto = new BlocoIndireto(mVolume, mFileBinary, mRefs[mContando]);
			
			long[] mNovoBlocoIndireto_Ids = mNovoBlocoIndireto.getIdentificadores();
			
			int mi = 0;
			int mo = mNovoBlocoIndireto_Ids.length;
			
			while (mi < mo) {
				
				mIds[ma] = mNovoBlocoIndireto_Ids[mi];
				
				mi+=1;
				ma+=1;
			}
			
			mContando += 1;
		}

		return mIds;

	}

	public String getReferenicasString() {

		String ret = "";

		long[] blocos = this.getReferenciadores();
		int bi = 0;
		int bo = blocos.length;

		while (bi < bo) {

			ret += (blocos[bi] + " ");

			bi += 1;
		}

		return ret;
	}

}
