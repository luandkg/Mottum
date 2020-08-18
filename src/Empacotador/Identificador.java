package Empacotador;

public class Identificador {

	private String mNome;
	private String mValor;

	public Identificador(String eNome) {
		mNome = eNome;
		mValor = "";
	}

	public Identificador(String eNome, String eValor) {
		mNome = eNome;
		mValor = eValor;
	}

	public void setNome(String eNome) {
		mNome = eNome;
	}

	public String getNome() {
		return mNome;
	}

	public void setValor(String eValor) {
		mValor = eValor;
	}

	public String getValor() {
		return mValor;
	}

}
