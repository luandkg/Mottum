package AssetContainer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ArquivoImagem {

	private Arquivo mArquivo;
	private boolean mObtido;
	private BufferedImage mImagem;

	public ArquivoImagem(Arquivo eArquivo) {
		mArquivo = eArquivo;
		mObtido = false;
		mImagem = null;
	}

	public BufferedImage getImagem() {

		if (!mObtido) {
			try {
				InputStream in = new ByteArrayInputStream(mArquivo.getBytes());
				mImagem = ImageIO.read(in);
				mObtido = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mImagem;
	}

	
	public String getNome() {
		return mArquivo.getNome();
	}

	public long getTipo() {
		return mArquivo.getTipo();
	}

	public long getInicio() {
		return mArquivo.getInicio();
	}

	public long getFim() {
		return mArquivo.getFim();
	}

	public long getTamanho() {return mArquivo.getTamanho();}
	
}
