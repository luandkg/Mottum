package IM;

import AssetContainer.FileBinary;
import Estruturas.Lista;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IMChunker {

    private SetoresLer mSetoresLer;

    private BufferedImage mImagem;

    private int mImagemLargura;
    private int mImagemAltura;

    private int mImagemBlocos;
    private int mImagemGamas;
    private int mProcessandoMaximo;

    private int blocogeral;
    private int gamageral;
    private Lista<Cor> mPaletaDaImagem;
    private Lista<Chunk> mChunks;

    public IMChunker() {

        mSetoresLer = new SetoresLer();
        mChunks = new Lista<Chunk>();

    }

    public Lista<Chunk> getChunks() {
        return mChunks;
    }



    public void abrirEmFluxo(String eArquivo, long eInicio) {

        mChunks.limpar();

        try {

            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "r");

            mProcessandoMaximo = 0;
            mImagemLargura = 0;
            mImagemAltura = 0;
            mImagemBlocos = 0;
            mImagemGamas = 0;

            blocogeral = 0;
            gamageral = 0;

            FileBinary fu = new FileBinary(raf);

            fu.setPonteiro(eInicio);

            System.out.println("Imagem IM - ABRIR");

            byte b1 = fu.readByte();
            byte b2 = fu.readByte();
            byte b3 = fu.readByte();

            System.out.println("\t - Cabecalho : " + b1 + "." + b2 + "." + b3);

            long w = fu.readLong();
            long h = fu.readLong();

            System.out.println("\t - Tamanho : " + w + " :: " + h);

            mImagem = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);

            mImagemLargura = (int) w;
            mImagemAltura = (int) h;


            byte m = fu.readByte();
            if ((int) m == Constantes.IMAGEM_NORMAL) {
                System.out.println("\t - Modo : NORMAL");
            } else if ((int) m == Constantes.IMAGEM_PALETAVEL) {
                System.out.println("\t - Modo : PALETAVEL");
            } else if ((int) m == Constantes.IMAGEM_CINZA) {
                System.out.println("\t - Modo : CINZENTO");
            } else {
                System.out.println("\t - Modo : DESCONHECIDO");
                return;
            }

            mPaletaDaImagem = new Lista<Cor>();

            int mBlocos = 0;

            int mBlocosPalUno = 0;
            int mBlocosPalBox = 0;

            int mBlocosCorUno = 0;
            int mBlocosCorPal = 0;
            int mBlocosCorBox = 0;

            int mGamas = 0;
            int mGamasUno = 0;
            int mGamasBox = 0;


            byte bc = fu.readByte();
            while (bc != -1) {

                int mTipoBloco = fu.organizarByteInt(bc);

                if (mTipoBloco == Constantes.PALETA) {

                    mPaletaDaImagem = mSetoresLer.lerPaleta(fu);

                    System.out.println("\t - Paleta : " + mPaletaDaImagem.getQuantidade());

                    mChunks.adicionar(Chunk.PALETA);

                } else if (mTipoBloco == Constantes.CINZENTO_UM) {

                    mBlocos += 1;

                    mSetoresLer.lerCinzentoUm(fu, mImagemLargura, mImagemAltura, gamageral, mImagem);
                    mProcessandoMaximo += 1;
                    mImagemGamas += 1;

                    mChunks.adicionar(Chunk.CINZENTO_UM);

                } else if (mTipoBloco == Constantes.CINZENTO_NORMAL) {

                    mBlocos += 1;

                    mSetoresLer.lerCinzentoNormal(fu, mImagemLargura, mImagemAltura, gamageral, mImagem);
                    mProcessandoMaximo += 1;
                    mImagemGamas += 1;

                    mChunks.adicionar(Chunk.CINZENTO_NORMAL);

                } else if (mTipoBloco == Constantes.GAMA_UM) {

                    mGamas += 1;
                    mGamasUno += 1;

                    mSetoresLer.lerGamaUm(fu, mImagemLargura, mImagemAltura, gamageral, mImagem);

                    mProcessandoMaximo += 1;
                    gamageral += 1;

                    mChunks.adicionar(Chunk.GAMA_UM);

                } else if (mTipoBloco == Constantes.GAMA_NORMAL) {

                    mGamas += 1;
                    mGamasBox += 1;

                    mSetoresLer.lerGamaNormal(fu, mImagemLargura, mImagemAltura, gamageral, mImagem);

                    mProcessandoMaximo += 1;
                    gamageral += 1;

                    mChunks.adicionar(Chunk.GAMA_NORMAL);

                } else if (mTipoBloco == Constantes.PALETAVEL_UM) {

                    mBlocos += 1;
                    mBlocosPalUno += 1;

                    mSetoresLer.lerPaletavelUm(fu, mPaletaDaImagem, mImagemLargura, mImagemAltura, blocogeral, mImagem);

                    mProcessandoMaximo += 1;
                    blocogeral += 1;

                    mChunks.adicionar(Chunk.PALETAVEL_UM);

                } else if (mTipoBloco == Constantes.PALETAVEL_NORMAL) {

                    mBlocos += 1;
                    mBlocosPalBox += 1;

                    mSetoresLer.lerPaletavelNormal(fu, mPaletaDaImagem, mImagemLargura, mImagemAltura, blocogeral, mImagem);

                    mProcessandoMaximo += 1;
                    blocogeral += 1;

                    mChunks.adicionar(Chunk.PALETAVEL_NORMAL);

                } else if (mTipoBloco == Constantes.BLOCO_UM) {

                    mBlocos += 1;
                    mBlocosCorUno += 1;

                    mSetoresLer.lerBlocoUm(fu, mImagemLargura, mImagemAltura, blocogeral, mImagem);

                    mProcessandoMaximo += 1;
                    blocogeral += 1;

                    mChunks.adicionar(Chunk.BLOCO_UM);

                } else if (mTipoBloco == Constantes.BLOCO_PALETAVEL) {

                    mBlocos += 1;
                    mBlocosCorPal += 1;

                    mSetoresLer.lerBlocoPaletavel(fu, mImagemLargura, mImagemAltura, blocogeral, mImagem);

                    mProcessandoMaximo += 1;
                    blocogeral += 1;

                    mChunks.adicionar(Chunk.BLOCO_PALETAVEL);

                } else if (mTipoBloco == Constantes.BLOCO_NORMAL) {

                    mBlocos += 1;
                    mBlocosCorBox += 1;

                    mSetoresLer.lerBlocoNormal(fu, mImagemLargura, mImagemAltura, blocogeral, mImagem);

                    mProcessandoMaximo += 1;
                    blocogeral += 1;

                    mChunks.adicionar(Chunk.BLOCO_NORMAL);

                } else if (mTipoBloco == Constantes.IMAGEM_FINALIZADOR) {

                    mChunks.adicionar(Chunk.IMAGEM_FINALIZADOR);

                    break;

                }


                bc = fu.readByte();

                if (fu.organizarByteInt(bc) == Constantes.IMAGEM_FINALIZADOR) {
                    break;
                }

            }

            System.out.println("\t - Blocos : " + mBlocos);

            System.out.println("\t\t - Blocos Pal Uno : " + mBlocosPalUno);
            System.out.println("\t\t - Blocos Pal Box : " + mBlocosPalBox);

            System.out.println("\t\t - Blocos Cor Uno : " + mBlocosCorUno);
            System.out.println("\t\t - Blocos Cor Pal : " + mBlocosCorPal);
            System.out.println("\t\t - Blocos Cor Box : " + mBlocosCorBox);


            System.out.println("\t - Gamas : " + mGamas);
            System.out.println("\t\t - Gama Uno : " + mGamasUno);
            System.out.println("\t\t - Gama Box : " + mGamasBox);


            raf.close();

            System.out.println("Imagem IM - TERMINADO");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void abrir(String eArquivo) {

        abrirEmFluxo(eArquivo, 0);

    }

    public void setPaleta(Lista<Cor> ePaleta) {
        mPaletaDaImagem = ePaleta;
    }

    public int getGamaGeral() {
        return gamageral;
    }

    public int getBlocoGeral() {
        return blocogeral;
    }

    public void aumentarGamaGeral() {
        gamageral += 1;
    }

    public void aumentarBlocoGeral() {
        blocogeral += 1;
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


    public void setBlocoCorrente(int a) {
        blocogeral = a;
    }

    public void setGamaCorrente(int a) {
        gamageral = a;
    }

    public void setImagemLargura(int a) {
        mImagemLargura = a;
    }

    public void setImagemAltura(int a) {
        mImagemAltura = a;
    }


    public void setImagem(BufferedImage aImagem) {
        mImagem = aImagem;
    }
}
