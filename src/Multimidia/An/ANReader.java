package IM;

import AssetContainer.FileBinary;
import Estruturas.Lista;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ANReader {

    private IM mIM;
    private Utils mUtils;

    private int mImagemLargura;
    private int mImagemAltura;
    private int mChrono;
    private SetoresLer mSetoresLer;

    private Lista<BufferedImage> mImagens;

    public ANReader() {

        mIM = new IM();
        mUtils = new Utils();
        mImagens = new Lista<BufferedImage>();
        mChrono = 0;
        mSetoresLer = new SetoresLer();

    }

    public void abrir(String eArquivo) {

        mImagens.limpar();
        mImagemLargura = 0;
        mImagemAltura = 0;
        mChrono = 0;

        try {
            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "r");

            FileBinary fu = new FileBinary(raf);

            fu.setPonteiro(0);

            System.out.println("Imagem IM - ABRIR");

            byte b1 = fu.readByte();
            byte b2 = fu.readByte();
            byte b3 = fu.readByte();

            System.out.println("Cabecalho : " + b1 + "." + b2 + "." + b3);

            int mQuadros = fu.readInt();
            System.out.println("Quadros : " + mQuadros);

            long w = fu.readLong();
            long h = fu.readLong();

            System.out.println("Tamanho : " + w + " :: " + h);

            mChrono = fu.readInt();
            System.out.println("Chrono : " + mChrono);


            mImagemLargura = (int) w;
            mImagemAltura = (int) h;


            byte m = fu.readByte();
            if ((int) m == Constantes.IMAGEM_NORMAL) {
                System.out.println("Modo : NORMAL");
            } else if ((int) m == Constantes.IMAGEM_PALETAVEL) {
                System.out.println("Modo : PALETAVEL");
            } else if ((int) m == Constantes.IMAGEM_CINZA) {
                System.out.println("Modo : CINZENTO");
            } else {
                System.out.println("Modo : DESCONHECIDO");
            }

            Lista<Cor> mPaletaDaImagem = new Lista<Cor>();

            IMReader mIMReader = new IMReader();

            int mQuadroID = 0;

            byte bc = fu.readByte();
            while (bc != -1) {

                int mTipoBloco = fu.organizarByteInt(bc);

                if (mTipoBloco == Constantes.PALETA) {

                    mPaletaDaImagem = mSetoresLer.lerPaleta(fu);

                } else if (mTipoBloco == Constantes.CHRONO_INICIO) {

                    System.out.println("Ler Quadro : " + mQuadroID);
                    lerChrono(fu, (int) w, (int) h, mPaletaDaImagem);

                    mQuadroID += 1;
                }


                bc = fu.readByte();
            }

            System.out.println("Imagens  : " + mImagens.getQuantidade());

            System.out.println("Imagem IM - TERMINADO");

        } catch ( IOException e) {

            e.printStackTrace();
        }


    }

    public void lerChrono(FileBinary fu, int w, int h, Lista<Cor> mPaletaDaImagem) {


        IMReader mIMReader = new IMReader();

        mIMReader.setPaleta(mPaletaDaImagem);

        mIMReader.setBlocoCorrente(0);
        mIMReader.setGamaCorrente(0);
        mIMReader.setImagemLargura(w);
        mIMReader.setImagemAltura(h);

        mIMReader.setImagem(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));


        byte bc = fu.readByte();

        while (bc != -1) {

            int mTipoBloco = fu.organizarByteInt(bc);

            if (mTipoBloco == Constantes.CINZENTO_UM) {

                mSetoresLer.lerCinzentoUm(fu,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.CINZENTO_NORMAL) {

                mSetoresLer.lerCinzentoNormal(fu,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.GAMA_UM) {

                mSetoresLer.lerGamaUm(fu, mImagemLargura, mImagemAltura, mIMReader.getGamaGeral(), mIMReader.getImagem());
                mIMReader.aumentarGamaGeral();

            } else if (mTipoBloco == Constantes.GAMA_NORMAL) {

                mSetoresLer.lerGamaNormal(fu, mImagemLargura, mImagemAltura, mIMReader.getGamaGeral(), mIMReader.getImagem());
                mIMReader.aumentarGamaGeral();


            } else if (mTipoBloco == Constantes.PALETAVEL_UM) {

                mSetoresLer.lerPaletavelUm(fu, mPaletaDaImagem,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.PALETAVEL_NORMAL) {

                mSetoresLer.lerPaletavelNormal(fu, mPaletaDaImagem,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_UM) {

                mSetoresLer.lerBlocoUm(fu,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_PALETAVEL) {

                mSetoresLer.lerBlocoPaletavel(fu,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_NORMAL) {

                mSetoresLer.lerBlocoNormal(fu,mImagemLargura, mImagemAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.CHRONO_FIM) {
                break;
            }


            bc = fu.readByte();


        }

        mImagens.adicionar(mIMReader.getImagem());

    }


    public int getLargura() {
        return mImagemLargura;
    }

    public int getAltura() {
        return mImagemAltura;
    }

    public int getChrono() {
        return mChrono;
    }


    public Lista<BufferedImage> getImagens() {
        return mImagens;
    }

}
