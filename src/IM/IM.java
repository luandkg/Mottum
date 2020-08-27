package IM;

import Estruturas.Iterador;
import Estruturas.Lista;
import java.awt.*;
import java.awt.image.BufferedImage;


public class IM {

    // IMPLEMENTACAO : 2020 08 22 -->> IMAGEM PALETAVEL
    // IMPLEMENTACAO : 2020 08 23 -->> IMAGEM NORMAL
    // IMPLEMENTACAO : 2020 08 24 -->> IMAGEM PRETA E BRANCA


    private BufferedImage mImagem;
    private Lista<BufferedImage> mBlocosGravados;

    private int mImagemLargura;
    private int mImagemAltura;

    private int mImagemBlocos;
    private int mImagemGamas;
    private int mProcessandoMaximo;


    public IM() {

        mBlocosGravados = new Lista<BufferedImage>();
        mProcessandoMaximo = 0;

    }


    public Lista<BufferedImage> getBlocosGravados() {
        return mBlocosGravados;
    }



    public void salvar(BufferedImage imagem, String eArquivo) {

        IMWriter mEscritor = new IMWriter();
        mEscritor.salvar(imagem, eArquivo);


    }

    public BufferedImage getCopia(BufferedImage eOriginal) {

        BufferedImage eCopia = new BufferedImage(eOriginal.getWidth(), eOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);


        for (int aqy = 0; aqy < (eOriginal.getHeight()); aqy++) {

            for (int aqx = 0; aqx < (eOriginal.getWidth()); aqx++) {

                int pixel = eOriginal.getRGB(aqx, aqy);

                eCopia.setRGB(aqx, aqy, pixel);
            }

        }


        return eCopia;

    }

    public void abrir(String eArquivo) {


        IMReader mLeitor = new IMReader();
        mLeitor.abrir(eArquivo);

        mImagem = mLeitor.getImagem();

        mImagemLargura = mLeitor.getLargura();
        mImagemAltura = mLeitor.getAltura();

        mImagemBlocos = mLeitor.getImagemBlocos();
        mImagemGamas = mLeitor.getImagemGamas();
        mProcessandoMaximo = mLeitor.getImagemProcessos();


    }

    public void abrirFluxo(String eArquivo,long eInicio) {


        IMReader mLeitor = new IMReader();

        mLeitor.abrirEmFluxo(eArquivo,eInicio);

        mImagem = mLeitor.getImagem();

        mImagemLargura = mLeitor.getLargura();
        mImagemAltura = mLeitor.getAltura();

        mImagemBlocos = mLeitor.getImagemBlocos();
        mImagemGamas = mLeitor.getImagemGamas();
        mProcessandoMaximo = mLeitor.getImagemProcessos();


    }


    public void abrirAte(String eArquivo, int ate) {


        // processogeral += 1;

        // if (processogeral >= ate) {
        //     break;
        //  }


    }

    public BufferedImage getImagem() {
        return mImagem;
    }

    public int getLargura() {
        return mImagemLargura;
    }

    public int getAltura() {
        return mImagemAltura;
    }

    public int getImagemBlocos() {
        return mImagemBlocos;
    }

    public int getImagemGamas() {
        return mImagemGamas;
    }


    public int getImagemProcessos() {
        return mProcessandoMaximo;
    }

    public void mudarTudo(Color eCor) {


        for (int y = 0; y < this.getAltura(); y++) {
            for (int x = 0; x < this.getLargura(); x++) {
                mImagem.setRGB(x, y, eCor.getRGB());
            }
        }


    }

    public static IM Criar(int eLargura, int eAltura) {

        IM mIM = new IM();

        mIM.mImagem = new BufferedImage((int) eLargura, (int) eAltura, BufferedImage.TYPE_INT_ARGB);
        mIM.mImagemLargura = eLargura;
        mIM.mImagemAltura = eAltura;

        return mIM;

    }

    public void setPixel(int ex, int ey, Cor eCor) {

        Color aCor = new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue(), eCor.getAlpha());
        mImagem.setRGB(ex, ey, aCor.getRGB());

    }

    public Cor getPixel(int ex, int ey) {
        int pixel = mImagem.getRGB(ex, ey);


        int blue = pixel & 0xff;
        int green = (pixel & 0xff00) >> 8;
        int red = (pixel & 0xff0000) >> 16;
        int alpha = (pixel >> 24) & 0xff;

        Cor aCor = new Cor(red, green, blue);
        aCor.setAlpha(alpha);

        return aCor;
    }
}
