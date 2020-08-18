package Volume;

public class Arquivo {

	private Volume mVolume;
	private FileBinary mFileBinary;

	private Ponto mPonto;
	private SuperBloco mSuperBloco;

	public Arquivo(Volume eVolume,FileBinary eFileBinary, Ponto ePonto) {
		mVolume=eVolume;
		mFileBinary=eFileBinary;
		mPonto=ePonto;
		
		mSuperBloco = new SuperBloco(mVolume,mFileBinary,mPonto.getLocal());
		
	}
	
	
	public String getNome() {return mSuperBloco.getNome();}
	
	public long getSuperBloco() {return mPonto.getLocal();}
	
	public long getDDC() {return mSuperBloco.getDDC();}
	public long getDDM() {return mSuperBloco.getDDM();}

	public long getTamanho() {return mSuperBloco.getUsado();}

	public void aumentar(int t) {
		
		if (t>0) {
			
			mSuperBloco.escreverPos(getTamanho(), new byte[t]);
			
			mSuperBloco.atualizarCom(getTamanho()+t);
		}
		
	}
	
	public void debug() {
		mSuperBloco.debug();
	}
	
	
	public void limpar() {
		
		mSuperBloco.limpar();
		
	}
}
