package Volume;

public class BlocoIndireto {

	private Volume mVolume;
	private FileBinary mFileBinary;
	private long mID;

	private long mPorcao;
	private long mInicio;
	private long mFim;
	private long mQuantos;
	
	
	public BlocoIndireto(Volume eVolume, FileBinary eFileBinary, long eID) {
		
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
		
		mFileBinary.seek(mInicio );
		
		return mFileBinary.readLong();
		
	}
	
	
	public boolean podeExpandir() {
		
		if (getBlocos()<500) {
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
		
			System.out.println("Comecar expansao indireta ...");
			System.out.println(" - Antes Quantos : " + eQuantos);

			long enovoID = mVolume.alocar();

			mFileBinary.seek(mInicio);
			
			mFileBinary.readLong();

			for (int a = 0; a < eQuantos; a++) {
				mFileBinary.readLong();
			}
			
			mFileBinary.writeLong(enovoID);

			mQuantos += 1;

			mFileBinary.seek(mInicio);
			
			mFileBinary.writeLong(mQuantos);
			
		}else {
			
			throw new RuntimeException("Bloco Indireto nao pode expandir alem do maximo de indices : 500 ");

			
		}
		
	}
	
	public long[] getIdentificadores() {
	
	
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
	
	
	public String getBlocosString() {
		
		
		String ret = "";
		
		long[] blocos = this.getIdentificadores();
		int bi = 0;
		int bo = blocos.length;

		while (bi < bo) {

			ret += (blocos[bi] + " ");

			bi += 1;
		}
		
		return ret;
	}
	
	
}
