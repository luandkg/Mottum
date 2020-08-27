package IM;

import AssetContainer.FileBinary;
import Estruturas.Iterador;
import Estruturas.Lista;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IMWriter {

    private IM mIM;
    private Utils mUtils;

    public IMWriter() {

        mIM = new IM();
        mUtils = new Utils();

    }

    public void salvar(BufferedImage imagem, String eArquivo) {


        System.out.println("Imagem IM - Criando");

        File Arq = new File(eArquivo);
        if (Arq.exists()) {
            Arq.delete();
        }


        int w = imagem.getWidth();
        int h = imagem.getHeight();

        //mBlocosGravados.limpar();

        try {

            System.out.println("Imagem IM - Cabecalho");

            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "rw");

            FileBinary fu = new FileBinary(raf);

            fu.writeByte((byte) 73);
            fu.writeByte((byte) 77);

            fu.writeByte((byte) 1);

            fu.writeLong((long) w);
            fu.writeLong((long) h);

            System.out.println("Imagem IM - Blocos");


            int mBlocosN = mUtils.getBlocosNum(w, h);
            int mGamas = mUtils.getGamasNum(w, h);

            boolean mPaletavel = mUtils.isPaletavel(imagem);

            if (mPaletavel) {


                Lista<Cor> mPaleta = mUtils.getPaleta(imagem);


                if (mUtils.isCinzeta(mPaleta)) {

                    escreverCinzento(fu, mPaleta.getQuantidade());

                    for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


                        Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

                        int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


                        BlocoIM mBlocoIM = mapearBloco(imagem, movendoBloco);

                        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);

                        // int iy = 0;


                        //  mBlocosGravados.adicionar(imagemBloco);

                        int mTipo = mBlocoIM.getTipo();

                        if (mTipo == 10) {
                            mTipo = 0;
                        }


                        if (mBlocoIM.getTipo() == 1) {

                            escreverCinzentoUm(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                        } else {

                            escreverCinzentoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                        }

                    }

                } else {

                    escreverPaletavel(fu, mPaleta);

                    for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


                        Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

                        int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());

                        BlocoIM mBlocoIM = mapearBloco(imagem, movendoBloco);


                        // mBlocosGravados.adicionar(imagemBloco);

                        int mTipo = mBlocoIM.getTipo();

                        if (mTipo == 10) {
                            mTipo = 0;
                        }


                        if (mBlocoIM.getTipo() == 1) {

                            escreverPaletavelUm(fu, mPaleta, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                        } else {

                            escreverPaletavelNormal(fu, mPaleta, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                        }

                    }

                }


                for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

                    Quad movendoGama = mUtils.getGama(w, h, mGamaID);
                    int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

                    BlocoIM mBlocoIM = mapearGama(imagem, movendoGama);

                    int mTipo = mBlocoIM.getTipoAlpha();

                    if (mTipo == 1) {

                        escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


                    } else {

                        escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


                    }

                }

            } else {


                System.out.println("PALETAVEL : NAO ");

                fu.writeByte((byte) Constantes.IMAGEM_NORMAL);


                for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


                    Quad movendoBloco = mUtils.getBloco(w, h, mBlocoID);

                    int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


                    BlocoIM mBlocoIM = mapearBloco(imagem, movendoBloco);


                    int mTipo = mBlocoIM.getTipo();


                    if (mBlocoIM.getTipo() == 1) {


                        escreverBlocoUm(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                    } else if (mBlocoIM.getTipo() == 10) {

                        escreverBlocoComPaleta(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                    } else {

                        escreverBlocoNormal(fu, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

                    }

                }

                for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

                    Quad movendoGama = mUtils.getGama(w, h, mGamaID);
                    int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

                    BlocoIM mBlocoIM = mapearGama(imagem, movendoGama);


                    int mTipo = mBlocoIM.getTipoAlpha();
                    //System.out.println("Tipo Alfa : " + mTipo);

                    if (mTipo == 1) {


                        escreverGamaUm(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);


                    } else {

                        escreverGamaNormal(fu, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);

                    }

                }

            }


            fu.writeByte((byte) Constantes.IMAGEM_FINALIZADOR);


            raf.close();

            System.out.println("Imagem IM - Terminada");


        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    public BlocoIM mapearBloco(BufferedImage imagem, Quad movendoBloco) {

        BlocoIM mBlocoIM = new BlocoIM();

        for (int aqy = movendoBloco.getY(); aqy < (movendoBloco.getY2()); aqy++) {
            //    int ix = 0;
            for (int aqx = movendoBloco.getX(); aqx < (movendoBloco.getX2()); aqx++) {


                int pixel = imagem.getRGB(aqx, aqy);

                int blue = pixel & 0xff;
                int green = (pixel & 0xff00) >> 8;
                int red = (pixel & 0xff0000) >> 16;

                Cor mCor = new Cor(red, green, blue);
                mBlocoIM.mapear(mCor);

                //  imagemBloco.setRGB(ix, iy, pixel);

                //   System.out.println("\t\t - Pixel " + aqx +":" + aqy + " { " + red + ":" + green + "." + blue + "} -->> " +  mIM.getCorDaPaleta(mPaleta,mCor) );
                //    ix += 1;
            }
            //iy += 1;

        }

        return mBlocoIM;
    }

    public BlocoIM mapearGama(BufferedImage imagem, Quad movendoGama) {

        BlocoIM mBlocoIM = new BlocoIM();

        for (int aqy = movendoGama.getY(); aqy < (movendoGama.getY2()); aqy++) {
            for (int aqx = movendoGama.getX(); aqx < (movendoGama.getX2()); aqx++) {


                int pixel = imagem.getRGB(aqx, aqy);

                int alpha = (pixel >> 24) & 0xff;

                Cor mCor = new Cor();
                mCor.setAlpha(alpha);

                mBlocoIM.mapear(mCor);
            }

        }
        return mBlocoIM;
    }

    public void escreverBlocoUm(FileBinary fu, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {


        Cor eCor = mBlocoIM.getCorUnica();

        //System.out.println("Cor Unica : " + eCor.toString());

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.BLOCO_UM);
            fu.writeByte((byte) Constantes.MODO_NORMAL);

        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.BLOCO_UM);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());


        }

        System.out.println("\t - Bloco " + mBlocoID + " UM -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> COR UNICA : " + eCor.toString());


        fu.writeByte((byte) eCor.getRed());
        fu.writeByte((byte) eCor.getGreen());
        fu.writeByte((byte) eCor.getBlue());
    }

    public void escreverBlocoNormal(FileBinary fu, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.BLOCO_NORMAL);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.BLOCO_NORMAL);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());

        }

        Lista<Cor> mBlocoCores = mBlocoIM.getCores();
        Iterador<Cor> mIteradorCores = new Iterador<Cor>(mBlocoCores);

        for (mIteradorCores.iniciar(); mIteradorCores.continuar(); mIteradorCores.proximo()) {

            Cor eCor = mIteradorCores.getValor();

            fu.writeByte((byte) eCor.getRed());
            fu.writeByte((byte) eCor.getGreen());
            fu.writeByte((byte) eCor.getBlue());

        }

        System.out.println("\t - Bloco " + mBlocoID + " Normal -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " }");


    }


    public void escreverBlocoComPaleta(FileBinary fu, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.BLOCO_PALETAVEL);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.BLOCO_PALETAVEL);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());

        }

        Lista<Cor> mBlocoPaleta = mBlocoIM.getPaleta();
        Iterador<Cor> mIteradorPaleta= new Iterador<Cor>(mBlocoPaleta);

        fu.writeByte((byte) mBlocoPaleta.getQuantidade());


        for (mIteradorPaleta.iniciar(); mIteradorPaleta.continuar(); mIteradorPaleta.proximo()) {
            Cor eCor = mIteradorPaleta.getValor();

            fu.writeByte((byte) eCor.getRed());
            fu.writeByte((byte) eCor.getGreen());
            fu.writeByte((byte) eCor.getBlue());
        }


        Lista<Cor> mBlocoCores = mBlocoIM.getCores();
        Iterador<Cor> mIteradorCores = new Iterador<Cor>(mBlocoCores);

        for (mIteradorCores.iniciar(); mIteradorCores.continuar(); mIteradorCores.proximo()) {

            int mCor = mUtils.getCorDaPaleta(mBlocoPaleta,mIteradorCores.getValor());

            fu.writeByte((byte) mCor);


        }

        System.out.println("\t - Bloco " + mBlocoID + " PALETA de " + mBlocoIM.getPaleta().getQuantidade() + " -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " }");



    }

    public void escreverCinzentoUm(FileBinary fu, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {

        int qTomCinzento = mBlocoIM.getCorUnica().getRed();

        //  System.out.println("Cor Unica : " + mCorUnica);

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.CINZENTO_UM);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.CINZENTO_UM);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());


        }

        System.out.println("\t - Bloco " + mBlocoID + " CINZENTO -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } :: COR UNICA { " + qTomCinzento + " } ");


        fu.writeByte((byte) qTomCinzento);

    }

    public void escreverCinzentoNormal(FileBinary fu, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {


        if (mModo == 0) {

            fu.writeByte((byte) Constantes.CINZENTO_NORMAL);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.CINZENTO_NORMAL);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());

        }

        Lista<Cor> mBlocoCores = mBlocoIM.getCores();
        Iterador<Cor> mIteradorCores = new Iterador<Cor>(mBlocoCores);

        for (mIteradorCores.iniciar(); mIteradorCores.continuar(); mIteradorCores.proximo()) {

            int qTomCinzento = mIteradorCores.getValor().getRed();

            fu.writeByte((byte) qTomCinzento);

        }

        System.out.println("\t - Bloco " + mBlocoID + " CINZENTO -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " }");


    }

    public void escreverPaletavelUm(FileBinary fu, Lista<Cor> mPaleta, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {

        int mCorUnica = mUtils.getCorDaPaleta(mPaleta, mBlocoIM.getCorUnica());

        //  System.out.println("Cor Unica : " + mCorUnica);

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.PALETAVEL_UM);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.PALETAVEL_UM);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());


        }

        System.out.println("\t - Bloco " + mBlocoID + " Paletavel -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } :: COR UNICA { " + mCorUnica + " } ");


        fu.writeByte((byte) mCorUnica);

    }

    public void escreverPaletavelNormal(FileBinary fu, Lista<Cor> mPaleta, int mBlocoID, Quad movendoBloco, BlocoIM mBlocoIM, int mTipo, int mModo) {

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.PALETAVEL_NORMAL);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.PALETAVEL_NORMAL);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoBloco.getLargura());
            fu.writeByte((byte) movendoBloco.getAltura());

        }

        Lista<Cor> mBlocoCores = mBlocoIM.getCores();
        Iterador<Cor> mIteradorCores = new Iterador<Cor>(mBlocoCores);

        for (mIteradorCores.iniciar(); mIteradorCores.continuar(); mIteradorCores.proximo()) {

            int qCorPaleta = mUtils.getCorDaPaleta(mPaleta, mIteradorCores.getValor());

            fu.writeByte((byte) qCorPaleta);

        }

        System.out.println("\t - Bloco " + mBlocoID + " Paletavel -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{ " + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " }");


    }

    public void escreverGamaUm(FileBinary fu, int mGamaID, Quad movendoGama, BlocoIM mBlocoIM, int mTipo, int mModo) {


        int eAlpha = mBlocoIM.getAlphaUnico();

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.GAMA_UM);
            fu.writeByte((byte) Constantes.MODO_NORMAL);

        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.GAMA_UM);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoGama.getLargura());
            fu.writeByte((byte) movendoGama.getAltura());


        }

        System.out.println("\t - Gama " + mGamaID + " UM -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{ " + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> ALPHA UNICO : " + eAlpha);


        fu.writeByte((byte) eAlpha);


    }

    public void escreverGamaNormal(FileBinary fu, int mGamaID, Quad movendoGama, BlocoIM mBlocoIM, int mTipo, int mModo) {

        if (mModo == 0) {

            fu.writeByte((byte) Constantes.GAMA_NORMAL);
            fu.writeByte((byte) Constantes.MODO_NORMAL);


        } else if (mModo == 1) {

            fu.writeByte((byte) Constantes.GAMA_NORMAL);
            fu.writeByte((byte) Constantes.MODO_PERSONALIZADO);

            fu.writeByte((byte) movendoGama.getLargura());
            fu.writeByte((byte) movendoGama.getAltura());

        }

        Lista<Cor> mBlocoCores = mBlocoIM.getCores();
        Iterador<Cor> mIteradorCores = new Iterador<Cor>(mBlocoCores);

        for (mIteradorCores.iniciar(); mIteradorCores.continuar(); mIteradorCores.proximo()) {

            Cor eCor = mIteradorCores.getValor();

            fu.writeByte((byte) eCor.getAlpha());

        }

        System.out.println("\t - Gama " + mGamaID + " Normal -->> Tipo : " + mTipo + " Modo : " + mModo + " { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{ " + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " }");


    }

    public void escreverCinzento(FileBinary fu, int mTons) {

        System.out.println("CINZETO : SIM ");
        System.out.println("TONS " + mTons);

        fu.writeByte((byte) Constantes.IMAGEM_CINZA);

    }


    public void escreverPaletavel(FileBinary fu, Lista<Cor> mPaleta) {

        Iterador<Cor> mIterador = new Iterador<Cor>(mPaleta);

        int mCores = mPaleta.getQuantidade();

        System.out.println("PALETAVEL : SIM ");
        System.out.println("PALETA " + mCores);

        fu.writeByte((byte) Constantes.IMAGEM_PALETAVEL);


        int it = 0;

        fu.writeByte((byte) Constantes.PALETA);
        fu.writeByte((byte) mCores);


        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

            Cor eCor = mIterador.getValor();
            System.out.println("\t - Cor : " + it + " -->> " + eCor.toString());

            fu.writeByte((byte) eCor.getRed());
            fu.writeByte((byte) eCor.getGreen());
            fu.writeByte((byte) eCor.getBlue());

            it += 1;
        }


    }
}
