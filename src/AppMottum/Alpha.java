package AppMottum;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Mottum.Windows;
import Mottum.Cenarios.Cena;
import Mottum.Estrutural.Rect;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;
import Mottum.Utils.Imaginador;
import Mottum.Utils.Local;
import Mottum.Utils.Recurso;


public class Alpha extends Cena {

	private Windows mWindows;

	private Escritor TextoGrande;
	
	BotaoCor BTN_ESQUERDA;
	Clicavel mClicavel;
	int eixo_x = 0;

	
	Rect mRect;
	private BufferedImage mImagem;
	
	public Alpha(Windows eWindows) {
		mWindows = eWindows;

		TextoGrande = new Escritor(30, Color.BLACK);

		mClicavel = new Clicavel();

		BTN_ESQUERDA = new BotaoCor(600, 50, 100, 100, new Color(26, 188, 156));
		
		mRect = new Rect(0,0,100,100);
		
	}

	public void setImagem(BufferedImage eImagem) {
		
		mImagem = eImagem;
		
	}
	
	
	
	@Override
	public void iniciar() {
		mWindows.setTitle("Alpha");
	}

	@Override
	public void update(double dt) {

		mClicavel.update(dt, mWindows.getMouse().Pressed());

		//System.out.println("Clicavel : " + mClicavel.getClicado());
		
		int epx = (int) mWindows.getMouse().x;
		int epy = (int) mWindows.getMouse().y;
		
		
		mRect.setPosicao(epx,epy);
		
		if (mClicavel.getClicado()) {

			int px = (int) mWindows.getMouse().x;
			int py = (int) mWindows.getMouse().y;

			if (BTN_ESQUERDA.getClicado(px, py)) {
				BTN_ESQUERDA.setX(BTN_ESQUERDA.getX() + 10);
			}
			

		
			
		}else {
			
			//mRect.setTamanho(100, 100);
		}

	}

	@Override
	public void draw(Graphics g) {

		mWindows.Limpar(g);
		BTN_ESQUERDA.draw(g);

		g.setColor(Color.RED);
		//g.fillRect(mRect.getX(),mRect.getY(),mRect.getLargura(),mRect.getAltura());

		TextoGrande.EscreveNegrito(g, "TESTE",200,200);

		
		g.drawImage(mImagem,mRect.getX(),mRect.getY(),mImagem.getWidth(),mImagem.getHeight(),null);
		

	}




}
