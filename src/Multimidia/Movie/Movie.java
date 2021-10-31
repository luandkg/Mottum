package Movie;

import AssetContainer.FileBinary;
import Estruturas.Lista;
import IM.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Movie {

    private int mLargura;
    private int mAltura;
    private int mTaxa;
    private BinaryUtils mArquivo;

    private Quadrum mQuadrumInicial;
    private Quadrum mQuadrumCorrente;
    private BufferedImage mImagemCorrente;

    private int mQuadroIndex;
    private int mFrameIndex;
    private boolean mAcabou;
    private LeitorS mSetoresLer;

    public Movie() {
        mLargura = 0;
        mAltura = 0;
        mTaxa = 0;
        mQuadroIndex = 0;
        mFrameIndex = 0;
        mAcabou = false;
        mSetoresLer = new LeitorS();

    }

    public boolean getAcabou() {
        return mAcabou;
    }

    public void abrir(String eArquivo) {


        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(new File(eArquivo), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mArquivo = new BinaryUtils(raf);

        mArquivo.setPonteiro(0);


        byte b1 = mArquivo.readByte();
        byte b2 = mArquivo.readByte();
        byte b3 = mArquivo.readByte();

        //  System.out.println("Cabecalho : " + b1 + "." + b2 + "." + b3);

        byte p1 = mArquivo.readByte();

        long w = mArquivo.readLong();
        long h = mArquivo.readLong();

        byte p2 = mArquivo.readByte();

        long eTaxa = mArquivo.readLong();

        byte p3 = mArquivo.readByte();


        mLargura = (int) w;
        mAltura = (int) h;
        mTaxa = (int) eTaxa;

        long ePosicao = mArquivo.getPonteiro();

        mQuadrumInicial = new Quadrum(mArquivo, ePosicao);
        mQuadrumCorrente = mQuadrumInicial;

    }


    public Quadrum getQuadroCorrente() {
        return mQuadrumCorrente;
    }

    public BufferedImage getImagemCorrente() {
        return mImagemCorrente;
    }

    public void lerFrame() {
        for (Frame eFrame : mQuadrumCorrente.getFrames()) {
            if (eFrame.getIndex() == mFrameIndex) {
                if (eFrame.getConteudo() == 0) {
                    mAcabou = true;
                } else {

                    mArquivo.setPonteiro(eFrame.getConteudo());
                    lerChrono();

                }
                break;
            }
        }

    }

    public void proximo() {

        System.out.println("Ler Quadro -- " + mQuadroIndex + "::" + mFrameIndex);

        lerFrame();

        avancar();

    }

    public void avancar() {
        mFrameIndex += 1;
        if (mFrameIndex >= 100) {
            mFrameIndex = 0;
            mQuadroIndex += 1;
            if (mQuadrumCorrente.getProximo() > 0) {
                mQuadrumCorrente = new Quadrum(mArquivo, mQuadrumCorrente.getProximo());
            } else {
                mAcabou = true;
            }
        }
    }

    public int getFrameCorrente() {
        return (mQuadroIndex * 100) + mFrameIndex;
    }

    public void lerChrono() {


        mImagemCorrente = new BufferedImage(mLargura, mAltura, BufferedImage.TYPE_INT_ARGB);

        IMReader mIMReader = new IMReader();


        mIMReader.setBlocoCorrente(0);
        mIMReader.setGamaCorrente(0);
        mIMReader.setImagemLargura(mLargura);
        mIMReader.setImagemAltura(mAltura);

        mIMReader.setImagem(mImagemCorrente);

        // System.out.println("Ler Chrono :: " + mArquivo.getPonteiro());

        byte bc = mArquivo.readByte();
        bc = mArquivo.readByte();

        // System.out.println("BC - " + mArquivo.organizarByteInt(bc));

        while (bc != -1 || bc != 51) {

            int mTipoBloco = mArquivo.organizarByteInt(bc);


            // System.out.println("Ler bloco = " + mTipoBloco);


            if (mTipoBloco == Constantes.PALETA) {

                mSetoresLer.setPaleta(mSetoresLer.lerPaleta(mArquivo));

            } else if (mTipoBloco == Constantes.CINZENTO_UM) {

                mSetoresLer.lerCinzentoUm(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.CINZENTO_NORMAL) {

                mSetoresLer.lerCinzentoNormal(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.GAMA_UM) {

                mSetoresLer.lerGamaUm(mArquivo, mLargura, mAltura, mIMReader.getGamaGeral(), mIMReader.getImagem());
                mIMReader.aumentarGamaGeral();

            } else if (mTipoBloco == Constantes.GAMA_NORMAL) {

                mSetoresLer.lerGamaNormal(mArquivo, mLargura, mAltura, mIMReader.getGamaGeral(), mIMReader.getImagem());
                mIMReader.aumentarGamaGeral();


            } else if (mTipoBloco == Constantes.PALETAVEL_UM) {

                mSetoresLer.lerPaletavelUm(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.PALETAVEL_NORMAL) {

                mSetoresLer.lerPaletavelNormal(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_UM) {

                mSetoresLer.lerBlocoUm(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_PALETAVEL) {

                mSetoresLer.lerBlocoPaletavel(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == Constantes.BLOCO_NORMAL) {

                mSetoresLer.lerBlocoNormal(mArquivo, mLargura, mAltura, mIMReader.getBlocoGeral(), mIMReader.getImagem());
                mIMReader.aumentarBlocoGeral();

            } else if (mTipoBloco == 51) {
                break;
            } else {
                break;
            }


            bc = mArquivo.readByte();


        }

        mImagemCorrente = mIMReader.getImagem();

    }


    public int getLargura() {
        return mLargura;
    }

    public int getAltura() {
        return mAltura;
    }

    public int getTaxa() {
        return mTaxa;
    }


    public int getQuadrosContagem() {
        int eContando = 1;

        Quadrum eQuadroPercursor = mQuadrumInicial;


        while (eQuadroPercursor.getProximo() != 0) {
            eContando += 1;
            eQuadroPercursor = new Quadrum(mArquivo, eQuadroPercursor.getProximo());
        }

        return eContando;
    }


    public ArrayList<Quadrum> getQuadros() {

        ArrayList<Quadrum> mQuadros = new ArrayList<Quadrum>();

        mQuadros.add(mQuadrumInicial);

        Quadrum eQuadroPercursor = mQuadrumInicial;


        while (eQuadroPercursor.getProximo() != 0) {
            eQuadroPercursor = new Quadrum(mArquivo, eQuadroPercursor.getProximo());
            mQuadros.add(eQuadroPercursor);
        }

        return mQuadros;

    }

    public int getFramesTotal() {

        int eContagem = 0;

        ArrayList<Quadrum> mQuadros = new ArrayList<Quadrum>();

        mQuadros.add(mQuadrumInicial);

        Quadrum eQuadroPercursor = mQuadrumInicial;

        eContagem += eQuadroPercursor.getFramesUsadosContagem();

        while (eQuadroPercursor.getProximo() != 0) {
            eQuadroPercursor = new Quadrum(mArquivo, eQuadroPercursor.getProximo());
            eContagem += eQuadroPercursor.getFramesUsadosContagem();
        }

        return eContagem;

    }

    public int getDuracao() {
        return getFramesTotal() * mTaxa;
    }

    public void fechar() {
        try {
            mArquivo.fechar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
