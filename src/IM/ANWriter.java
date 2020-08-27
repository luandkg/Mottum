package IM;

import AssetContainer.FileBinary;
import Estruturas.Iterador;
import Estruturas.Lista;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ANWriter {

    private IM mIM;
    private Utils mUtils;

    public ANWriter() {

        mIM = new IM();
        mUtils = new Utils();

    }

    public void criar(Lista<BufferedImage> eImagens, int eChrono, String eArquivo) {

        if (eImagens.getQuantidade() > 0) {
            Iterador<BufferedImage> mIterador = new Iterador<BufferedImage>(eImagens);

            int w = eImagens.getValor(0).getWidth();
            int h = eImagens.getValor(0).getHeight();


            for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                BufferedImage mCorrente = mIterador.getValor();

                if (mCorrente.getWidth() != w) {
                    throw new IllegalArgumentException("Todos os quadros devem possuir a mesma largura !");
                }
                if (mCorrente.getHeight() != h) {
                    throw new IllegalArgumentException("Todos os quadros devem possuir a mesma altura !");
                }

            }

            try {

                System.out.println("Animacao AN - Cabecalho");

                RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "rw");

                FileBinary fu = new FileBinary(raf);

                fu.writeByte((byte) 65);
                fu.writeByte((byte) 78);

                fu.writeByte((byte) 1);

                fu.writeInt(eImagens.getQuantidade());

                fu.writeLong((long) w);
                fu.writeLong((long) h);

                fu.writeInt(eChrono);


                System.out.println("Animacao IM - Blocos");

                int mBlocosN = mUtils.getBlocosNum(w, h);
                int mGamas = mUtils.getGamasNum(w, h);

                System.out.println("Blocos : " + mBlocosN);
                System.out.println("Gamas : " + mGamas);


                boolean mPaletavel = mUtils.isAnimacaoPaletavel(eImagens);

                System.out.println("Paletavel : " + mPaletavel);

                IMWriter mIMWriter = new IMWriter();


                if (mPaletavel) {

                    Lista<Cor> mPaleta = mUtils.getAnimacaoPaleta(eImagens);

                    System.out.println("Preta e Branca : " + mUtils.isCinzeta(mPaleta));

                    if (mUtils.isCinzeta(mPaleta)) {

                        System.out.println("Animacao IM - CINZENTA : IMPLEMENTANDO ");

                        fu.writeByte((byte) Constantes.IMAGEM_CINZA);

                        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                            System.out.println("QUADRO : " + mIterador.getIndice());

                            fu.writeByte((byte) Constantes.CHRONO_INICIO);

                            escreverQuadroCinzento(fu, mIterador.getValor(), w, h, mBlocosN, mGamas,mIMWriter);

                            fu.writeByte((byte) Constantes.CHRONO_FIM);


                        }

                    } else {

                        System.out.println("Animacao IM - PALETAVEL : IMPLEMENTANDO ");


                        fu.writeByte((byte) Constantes.IMAGEM_PALETAVEL);


                        mIMWriter.escreverPaletavel(fu, mPaleta);


                        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                            System.out.println("QUADRO : " + mIterador.getIndice());

                            fu.writeByte((byte) Constantes.CHRONO_INICIO);

                            escreverQuadroPaletavel(fu, mIterador.getValor(), w, h, mBlocosN, mGamas,mIMWriter,mPaleta);

                            fu.writeByte((byte) Constantes.CHRONO_FIM);


                        }



                    }

                } else {

                    System.out.println("PALETAVEL : NAO ");

                    fu.writeByte((byte) Constantes.IMAGEM_NORMAL);

                    for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                        System.out.println("QUADRO : " + mIterador.getIndice());

                        fu.writeByte((byte) Constantes.CHRONO_INICIO);

                        escreverQuadro(fu, mIterador.getValor(), w, h, mBlocosN, mGamas,mIMWriter);

                        fu.writeByte((byte) Constantes.CHRONO_FIM);


                    }


                }


                fu.writeByte((byte) Constantes.IMAGEM_FINALIZADOR);


                raf.close();

                System.out.println("Animacao IM - Terminada");


            } catch (IOException e) {

                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("A animcao precisa ter no minimo uma imagem !");
        }


    }



    private void escreverQuadro(FileBinary fu, BufferedImage imagem, int w, int h, int mBlocosN, int mGamas, IMWriter mIMWriter ) {



        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mIMWriter.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();


            if (mBlocoIM.getTipo() == 1) {


                mIMWriter.escreverBlocoUm(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else if (mBlocoIM.getTipo() == 10) {

                mIMWriter.escreverBlocoComPaleta(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else {

                mIMWriter.escreverBlocoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            }

        }

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(w, h, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mIMWriter.mapearGama(imagem, movendoGama);


            int mTipo = mBlocoIM.getTipoAlpha();
            //System.out.println("Tipo Alfa : " + mTipo);

            if (mTipo == 1) {


                mIMWriter.escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


            } else {

                mIMWriter.escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);

            }

        }

    }

    private void escreverQuadroPaletavel(FileBinary fu, BufferedImage imagem, int w, int h, int mBlocosN, int mGamas, IMWriter mIMWriter, Lista<Cor> mPaleta ) {



        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mIMWriter.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();


            if (mBlocoIM.getTipo() == 1) {


                mIMWriter.escreverPaletavelUm(fu, mPaleta,mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else {

                mIMWriter.escreverPaletavelNormal(fu, mPaleta,mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            }

        }

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(w, h, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mIMWriter.mapearGama(imagem, movendoGama);


            int mTipo = mBlocoIM.getTipoAlpha();
            //System.out.println("Tipo Alfa : " + mTipo);

            if (mTipo == 1) {


                mIMWriter.escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


            } else {

                mIMWriter.escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);

            }

        }

    }

    private void escreverQuadroCinzento(FileBinary fu, BufferedImage imagem, int w, int h, int mBlocosN, int mGamas, IMWriter mIMWriter ) {



        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mIMWriter.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();


            if (mBlocoIM.getTipo() == 1) {


                mIMWriter.escreverCinzentoUm(fu,mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else {

                mIMWriter.escreverCinzentoNormal(fu,mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            }

        }

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(w, h, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mIMWriter.mapearGama(imagem, movendoGama);


            int mTipo = mBlocoIM.getTipoAlpha();
            //System.out.println("Tipo Alfa : " + mTipo);

            if (mTipo == 1) {


                mIMWriter.escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


            } else {

                mIMWriter.escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);

            }

        }

    }

}
