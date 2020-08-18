package AssetContainer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class AssetContainer {


	private ArrayList<Pasta> mPastas;
	private ArrayList<Arquivo> mArquivos;
	
	private String mCabecalho;
	private String mVersao;
	private String mCriado;
	private String mFinalizado;

	private String mArquivo;
	private boolean mAberto;

	public AssetContainer() {
		
		mPastas = new ArrayList<Pasta>();
		mArquivos = new ArrayList<Arquivo>();
		
		mCabecalho = "";
		mVersao="";
		mCriado = "";
		mFinalizado = "";

		mAberto=false;
	}

	public String getCabecalho() {
		return mCabecalho;
	}
	
	public String getVersao() {
		return mVersao;
	}
	
	public String getCriado() {
		return mCriado;
	}
	
	public String getFinalizado() {
		return mFinalizado;
	}

	public String getArquivo() {
		return mArquivo;
	}
	public boolean isAberto() {
		return mAberto;
	}

	public long getTamanho() {
		File mFile = new File(mArquivo);
		return mFile.length();
	}
	
	public void abrir(String eArquivo) {

		mPastas = new ArrayList<Pasta>();
		mCabecalho = "";
		mArquivo=eArquivo;
		
		
		try {

			RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "rw");

			FileBinary fu = new FileBinary(raf);

			fu.inicio();

			mCabecalho = fu.readString();
			mVersao = fu.readString();
			mCriado = fu.readString();
			mFinalizado = fu.readString();

			int v = 0;

			while (v != 13) {
				v = (int) fu.readByte();

				if (v == 11) {

					String s1 = fu.readString();
					long l2 = fu.readLong();
					long l3 = fu.readLong();

					mPastas.add(new Pasta(this, new Ponto(s1, 11, l2, l3)));

				} else if (v == 12) {

					String s1 = fu.readString();
					long l2 = fu.readLong();
					long l3 = fu.readLong();

					mArquivos.add(new Arquivo(this, new Ponto(s1, 12, l2, l3)));

				}
			}


			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		mAberto=true;
	}

	public void listarDebug(String elocalExportar) {

		System.out.println("Cabecalho : " + mCabecalho);

		
		listarDebugCom(0,elocalExportar, this.getArquivos(), this.getPastas());
	}
	
	
	public int getArquivosContagem() {
		int i = 0;
		
		i+=this.getArquivos().size();
		
		for (Pasta mPasta : this.getPastas()) {
			i+=getArquivosContagemInterno(mPasta);
		}
		
		return i;
	}

	
	public int getArquivosContagemInterno(Pasta ePasta) {
		int i = 0;
		
		i+=ePasta.getArquivos().size();
		
		for (Pasta mPasta : ePasta.getPastas()) {
			i+=getArquivosContagemInterno(mPasta);
		}
		
		return i;
	}
	
	
	public int getPastasContagem() {
		int i = 0;
		
		i+=this.getPastas().size();
		
		for (Pasta mPasta : this.getPastas()) {
			i+=mPasta.getPastasContagemInterno(mPasta);
		}
		
		return i;
	}


	
	
	public int getContagem() {
		int i = 0;
		
		i+=getArquivos().size();
		i+=this.getPastas().size();
		
		for (Pasta mPasta : this.getPastas()) {
			i+=mPasta.getContagem();
		}
		
		return i;
	}
	

	
	
	public void listarDebugCom(int t,String eAnterior, ArrayList<Arquivo> mDebugArquivos, ArrayList<Pasta> mDebugPastas) {

		String mTab = "";
		if (t > 0) {
			for (int i = 0; i < t; i++) {
				mTab += "\t";
			}
		}

		for (Pasta mPasta : mDebugPastas) {

			System.out.println(mTab + "DIR = " + mPasta.getNome());
			System.out.println(mTab + "\tI = " + mPasta.getInicio());
			System.out.println(mTab + "\tF = " + mPasta.getFim());

			listarDebugCom(t + 1,eAnterior + mPasta.getNome() +".", mPasta.getArquivos(), mPasta.getPastas());

		}

		for (Arquivo subArquivo : mDebugArquivos) {

			System.out.println(mTab + "tARQ = " + subArquivo.getNome());
			System.out.println(mTab + "\tI = " + subArquivo.getInicio());
			System.out.println(mTab + "\tF = " + subArquivo.getFim());
			System.out.println(mTab + "\tTamanho = " + subArquivo.getTamanho());
			
			
		subArquivo.exportar(eAnterior  + subArquivo.getNome());
			
		}

	}
	
	public void listarTabelaDeArquivos() {

		System.out.println("Cabecalho : " + mCabecalho);
	
		listarTabelaDeArquivosInterno("", this.getArquivos(), this.getPastas());
	}
	
	public void listarTabelaDeArquivosInterno(String eAntes, ArrayList<Arquivo> mDebugArquivos, ArrayList<Pasta> mDebugPastas) {



		for (Pasta mPasta : mDebugPastas) {

	
			listarTabelaDeArquivosInterno(eAntes + mPasta.getNome() + "/", mPasta.getArquivos(), mPasta.getPastas());

		}

		for (Arquivo subArquivo : mDebugArquivos) {


			
			
		System.out.println(" -->> " + " [ " +subArquivo.getInicio() + " " + subArquivo.getFim() + " ] :: " + eAntes + subArquivo.getNome()  );
			
		}

	}
	

	public ArrayList<Arquivo> getArquivos() {

	

		return mArquivos;

	}

	public ArrayList<Pasta> getPastas() {

	
		return mPastas;

	}

	
	public boolean existePasta(String eNome) {
		
		boolean ret = false;
		
		for (Pasta mPasta : getPastas()) {
			if (mPasta.getNome().contentEquals(eNome)) {
				ret=true;
				break;
			}
		}
		return ret;
	}
	
	public boolean existeArquivo(String eNome) {
		
		boolean ret = false;
		
		for (Arquivo mArquivo : getArquivos()) {
			if (mArquivo.getNome().contentEquals(eNome)) {
				ret=true;
				break;
			}
		}
		return ret;
	}
	
	
	public Pasta getPasta(String eNome) {
		
		Pasta ret = null;
		
		for (Pasta mPasta : getPastas()) {
			if (mPasta.getNome().contentEquals(eNome)) {
				ret=mPasta;
				break;
			}
		}
		return ret;
	}
	
	public Arquivo getArquivo(String eNome) {
		
		Arquivo ret = null;
		
		for (Arquivo mArquivo : getArquivos()) {
			if (mArquivo.getNome().contentEquals(eNome)) {
				ret=mArquivo;
				break;
			}
		}
		return ret;
	}
	
	public ArquivoImagem getArquivoImagem(String eNome) {
		return new ArquivoImagem(getArquivo(eNome));
	}
	

	
}
