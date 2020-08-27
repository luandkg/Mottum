package AssetContainer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


import Mottum.Windows;
import Mottum.Cenarios.Cena;

import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;

import IM.IM;


public class VisualizadorAC extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;

    Clicavel mClicavel;

    String eArquivoIM = "imagem.im";

    BufferedImage imagem = null;

    String mArquivo = "asset.dkga";

    private IM mIM;


    public VisualizadorAC(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        mIM = new IM();


        AssetContainer mAC = new AssetContainer();
        mAC.abrir(mArquivo);

        Arquivo ma = mAC.getArquivo("imagem.im");

        System.out.println(" IMAGEM : " + ma.getInicio() + " <> " + ma.getFim());

        mIM.abrirFluxo(mArquivo,ma.getInicio());

        imagem=mIM.getImagem();



    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Visualizador IM");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());


        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


        }


    }

    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);


        TextoGrande.EscreveNegrito(g, "Visualizador AC", 20, 80);

        g.drawImage(imagem, 200, 200, imagem.getWidth(), imagem.getHeight(), null);


    }


}
