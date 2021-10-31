package IM;

import Estruturas.Lista;
import Multimidia.Quad;
import Multimidia.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IMWriter {

    private IM mIM;
    private Utils mUtils;
    private SetoresEscrever mSetoresEscrever;

    private int mBlocosUno;
    private int mBlocosPal;
    private int mBlocosBox;

    private int mGamasUno;
    private int mGamasBox;

    public IMWriter() {

        mIM = new IM();
        mUtils = new Utils();
        mSetoresEscrever = new SetoresEscrever();

        mBlocosBox = 0;
        mBlocosPal = 0;
        mBlocosBox = 0;

        mGamasUno = 0;
        mGamasBox = 0;

    }

    public void salvarAntigo(BufferedImage imagem, String eArquivo) {


        System.out.println("Imagem IM Antigo - Criando");

        File Arq = new File(eArquivo);
        if (Arq.exists()) {
            Arq.delete();
        }


        int w = imagem.getWidth();
        int h = imagem.getHeight();


        try {

            System.out.println("\t - Imagem IM - Cabecalho");

            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "rw");

            BinaryUtils fu = new BinaryUtils(raf);


            fu.writeByte((byte) Constantes.IMAGEM_IM1);
            fu.writeByte((byte) Constantes.IMAGEM_IM2);

            fu.writeByte((byte) Constantes.IMAGEM_VERSAO_1);

            fu.writeLong((long) w);
            fu.writeLong((long) h);

            System.out.println("\t - Imagem IM - Blocos");


            int mBlocos = mUtils.getBlocosNum(w, h);
            int mGamas = mUtils.getGamasNum(w, h);


            boolean mPaletavel = mUtils.isPaletavel(imagem);

            if (mPaletavel) {

                System.out.println("\t - Paletavel : " + "Sim");

                Lista<Cor> mPaleta = mUtils.getPaleta(imagem);
                System.out.println("\t - Paleta : " + mPaleta.getQuantidade());

                if (mUtils.isCinzeta(mPaleta)) {


                    escreverModoPaletaCinzentaAntigo(fu, mPaleta, mBlocos, w, h, imagem);

                    System.out.println("\t - Cinzenta : " + "Sim");

                } else {

                    System.out.println("\t - Cinzenta : " + "Nao");

                    escreverModoPaletaColoridoAntigo(fu, mPaleta, mBlocos, w, h, imagem);
                }

                escreverGamaAntigo(fu, mGamas, w, h, imagem);

            } else {

                System.out.println("\t - Paletavel : " + "Nao");

                escreverNormalAntigo(fu, mBlocos, w, h, imagem);

                escreverGamaAntigo(fu, mGamas, w, h, imagem);

            }


            fu.writeByte((byte) Constantes.IMAGEM_FINALIZADOR);


            raf.close();

            System.out.println("\t - Blocos : " + mBlocos);
            System.out.println("\t - Gamas : " + mGamas);


            System.out.println("Imagem IM - Terminada");


        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void salvar(BufferedImage imagem, String eArquivo) {


        System.out.println("Imagem IM - Criando");

        File Arq = new File(eArquivo);
        if (Arq.exists()) {
            Arq.delete();
        }


        int w = imagem.getWidth();
        int h = imagem.getHeight();

        mBlocosBox = 0;
        mBlocosPal = 0;
        mBlocosUno = 0;

        mGamasBox = 0;
        mGamasUno = 0;

        try {

            System.out.println("\t - Imagem IM - Cabecalho");

            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "rw");

            BinaryUtils fu = new BinaryUtils(raf);


            fu.writeByte((byte) Constantes.IMAGEM_IM1);
            fu.writeByte((byte) Constantes.IMAGEM_IM2);

            fu.writeByte((byte) Constantes.IMAGEM_VERSAO_2);

            fu.writeLong((long) w);
            fu.writeLong((long) h);

            System.out.println("\t - Imagem IM - Blocos");


            int mBlocos = mUtils.getBlocosNum(w, h);
            int mGamas = mUtils.getGamasNum(w, h);

            boolean mPaletavel = mUtils.isPaletavel(imagem);

            if (mPaletavel) {

                System.out.println("\t - Paletavel : " + "Sim");

                Lista<Cor> mPaleta = mUtils.getPaleta(imagem);
                System.out.println("\t - Paleta : " + mPaleta.getQuantidade());

                if (mUtils.isCinzeta(mPaleta)) {

                    System.out.println("\t - Cinzenta : " + "Sim");

                    escreverModoPaletaCinzenta(fu, mPaleta, mBlocos, w, h, imagem);
                } else {

                    System.out.println("\t - Cinzenta : " + "Nao");

                    escreverModoPaletaColorido(fu, mPaleta, mBlocos, w, h, imagem);
                }

                escreverGama(fu, mGamas, w, h, imagem);

            } else {

                System.out.println("\t - Paletavel : " + "Nao");

                escreverNormal(fu, mBlocos, w, h, imagem);

                escreverGama(fu, mGamas, w, h, imagem);

            }

            System.out.println("\t - Blocos : " + mBlocos);
            System.out.println("\t\t - Blocos Uno : " + mBlocosUno);
            System.out.println("\t\t - Blocos Pal : " + mBlocosPal);
            System.out.println("\t\t - Blocos Box : " + mBlocosBox);

            System.out.println("\t - Gamas : " + mGamas);
            System.out.println("\t\t - Gama Uno : " + mGamasUno);
            System.out.println("\t\t - Gama Box : " + mGamasBox);

            fu.writeByte((byte) Constantes.IMAGEM_FINALIZADOR);


            raf.close();

            System.out.println("Imagem IM - Terminada");


        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    private void escreverModoPaletaCinzenta(BinaryUtils fu, Lista<Cor> mPaleta, int mBlocosN, int w, int h, BufferedImage imagem) {


        mSetoresEscrever.escreverCinzento(fu, mPaleta.getQuantidade());

        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {

            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);

            int mTipo = mBlocoIM.getTipo();

            if (mTipo == 10) {
                mTipo = 0;
            }


            if (mBlocoIM.getTipo() == 1) {

                mSetoresEscrever.escreverCinzentoUm(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);
                mBlocosUno += 1;

            } else {

                mSetoresEscrever.escreverCinzentoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);
                mBlocosBox += 1;
            }

        }


    }

    private void escreverModoPaletaColorido(BinaryUtils fu, Lista<Cor> mPaleta, int mBlocosN, int w, int h, BufferedImage imagem) {


        mSetoresEscrever.escreverPaletavel(fu, mPaleta);

        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);


            // mBlocosGravados.adicionar(imagemBloco);

            int mTipo = mBlocoIM.getTipo();

            if (mTipo == 10) {
                mTipo = 0;
            }


            if (mBlocoIM.getTipo() == 1) {

                mSetoresEscrever.escreverPaletavelUm(fu, mPaleta, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);
                mBlocosUno += 1;

            } else {

                mSetoresEscrever.escreverPaletavelNormal(fu, mPaleta, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);
                mBlocosBox += 1;

            }

        }
    }

    private void escreverNormal(BinaryUtils fu, int mBlocosN, int w, int h, BufferedImage imagem) {


        System.out.println("PALETAVEL : NAO ");

        fu.writeByte((byte) Constantes.IMAGEM_NORMAL);


        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();


            if (mBlocoIM.getTipo() == 1) {


                mSetoresEscrever.escreverBlocoUm(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                mBlocosUno += 1;

            } else if (mBlocoIM.getTipo() == 10) {

                mSetoresEscrever.escreverBlocoComPaleta(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                mBlocosPal += 1;
            } else {

                mSetoresEscrever.escreverBlocoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                mBlocosBox += 1;
            }

        }
    }


    private void escreverGama(BinaryUtils fu, int mGamas, int w, int h, BufferedImage imagem) {

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(w, h, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearGama(imagem, movendoGama);

            int mTipo = mBlocoIM.getTipoAlpha();

            if (mTipo == 1) {
                mSetoresEscrever.escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);
                mGamasUno += 1;
            } else {
                mSetoresEscrever.escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);
                mGamasBox += 1;

            }

        }
    }


    private void escreverNormalAntigo(BinaryUtils fu, int mBlocosN, int w, int h, BufferedImage imagem) {


        System.out.println("PALETAVEL : NAO ");

        fu.writeByte((byte) Constantes.IMAGEM_NORMAL);


        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();


            if (mBlocoIM.getTipo() == 1) {


                mSetoresEscrever.escreverBlocoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            }

        }
    }

    private void escreverModoPaletaCinzentaAntigo(BinaryUtils fu, Lista<Cor> mPaleta, int mBlocosN, int w, int h, BufferedImage imagem) {


        mSetoresEscrever.escreverCinzento(fu, mPaleta.getQuantidade());

        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {

            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);

            int mTipo = mBlocoIM.getTipo();

            if (mTipo == 10) {
                mTipo = 0;
            }


            mSetoresEscrever.escreverCinzentoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);


        }


    }

    private void escreverModoPaletaColoridoAntigo(BinaryUtils fu, Lista<Cor> mPaleta, int mBlocosN, int w, int h, BufferedImage imagem) {


        mSetoresEscrever.escreverPaletavel(fu, mPaleta);

        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);


            // mBlocosGravados.adicionar(imagemBloco);

            int mTipo = mBlocoIM.getTipo();

            if (mTipo == 10) {
                mTipo = 0;
            }


            mSetoresEscrever.escreverPaletavelNormal(fu, mPaleta, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);


        }
    }

    private void escreverGamaAntigo(BinaryUtils fu, int mGamas, int w, int h, BufferedImage imagem) {

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(w, h, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearGama(imagem, movendoGama);

            int mTipo = mBlocoIM.getTipoAlpha();


            mSetoresEscrever.escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


        }
    }

}
