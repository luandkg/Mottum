package Volume;

public class Ponto {

	private int mTipo;
	private long mLocal;
	
	public Ponto(int eTipo,long eLocal) {
		
		mTipo=eTipo;
				mLocal=eLocal;
	}
	
	public int getTipo() {return mTipo; }
	public long getLocal() {return mLocal; }

}
