package IM;

import AssetContainer.FileBinary;
import Estruturas.Iterador;
import Estruturas.Lista;
import IM.Cor;
import IM.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AN {

    // IMPLEMENTACAO : 2020 08 22 -->> ANIMACAO NORMAL
    // IMPLEMENTACAO : 2020 08 23 -->> ANIMACAO PALETAVEL
    // IMPLEMENTACAO : 2020 08 23 -->> ANIMACAO PRETA E BRANCA

    private Utils mUtils;
    private Lista<BufferedImage> mImagens;

    private int mImagemLargura;
    private int mImagemAltura;
    private int mChrono;

    public AN() {

        mUtils = new Utils();

        mImagens = new Lista<BufferedImage>();

        mImagemLargura = 0;
        mImagemAltura = 0;

    }


    public void criar(Lista<BufferedImage> eImagens, int eChrono, String eArquivo) {

        ANWriter mCriador = new ANWriter();
        mCriador.criar(eImagens, eChrono, eArquivo);


    }


    public void abrir(String eArquivo) {


        ANReader mLeitor = new ANReader();
        mLeitor.abrir(eArquivo);

        mImagens = mLeitor.getImagens();

        mImagemLargura = mLeitor.getLargura();
        mImagemAltura = mLeitor.getAltura();
        mChrono = mLeitor.getChrono();

    }

    public Lista<BufferedImage> getImagens() {
        return mImagens;
    }

    public int getQuantidade() {
        return mImagens.getQuantidade();
    }

    public int getChrono() {
        return mChrono;
    }

    public int getLargura() {
        return mImagemLargura;
    }

    public int getAltura() {
        return mImagemAltura;
    }


}
