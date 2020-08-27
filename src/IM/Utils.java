package IM;

import Estruturas.Iterador;
import Estruturas.Lista;

import java.awt.image.BufferedImage;

public class Utils {

    public boolean isPaletavel(BufferedImage imagem) {

        int w = imagem.getWidth();
        int h = imagem.getHeight();

        final int mImagem_Largura = w;
        final int mImagem_Altura = h;

        //  System.out.println("W : " + w);
        //  System.out.println("H : " + h);


        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Bloco_Tamanho) {
            w -= Constantes.Bloco_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
            //    System.out.println("Sob X : " + w);
        }

        while (h >= Constantes.Bloco_Tamanho) {
            h -= Constantes.Bloco_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;
            //     System.out.println("Sob Y : " + h);

        }
        //  System.out.println("PW : " + PW);
        // System.out.println("PH : " + PH);

        int p = PW * PH;

        // System.out.println("PARTES : " + p);


        int mCores = 0;
        Lista<Cor> lsCores = new Lista<Cor>();
        Iterador<Cor> mIterador = new Iterador<Cor>(lsCores);

        for (int aqx = 0; aqx < mImagem_Largura; aqx++) {
            for (int aqy = 0; aqy < mImagem_Altura; aqy++) {

                int pixel = imagem.getRGB(aqx, aqy);

                int blue = pixel & 0xff;
                int green = (pixel & 0xff00) >> 8;
                int red = (pixel & 0xff0000) >> 16;

                Cor mNovaCor = new Cor(red, green, blue);
                boolean tem = false;

                for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                    Cor mCorrente = mIterador.getValor();

                    if (mCorrente.igual(mNovaCor)) {
                        tem = true;
                        break;
                    }
                }

                if (tem == false) {
                    lsCores.adicionar(mNovaCor);
                }

                if (lsCores.getQuantidade() > 255) {
                    break;
                }

            }

            if (lsCores.getQuantidade() > 255) {
                break;
            }
        }

        mCores = lsCores.getQuantidade();
        if (mCores > 255) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAnimacaoPaletavel(Lista<BufferedImage> mImagens) {


        if (mImagens.getQuantidade() > 0) {

            int mCores = 0;
            Lista<Cor> lsCores = new Lista<Cor>();
            Iterador<Cor> mIterador = new Iterador<Cor>(lsCores);


            Iterador<BufferedImage> mIteradorImagens = new Iterador<BufferedImage>(mImagens);
            for (mIteradorImagens.iniciar(); mIteradorImagens.continuar(); mIteradorImagens.proximo()) {

                BufferedImage imagem = mIteradorImagens.getValor();

                int w = imagem.getWidth();
                int h = imagem.getHeight();

                final int mImagem_Largura = w;
                final int mImagem_Altura = h;

                //  System.out.println("W : " + w);
                //  System.out.println("H : " + h);


                int PW = 0;
                int PH = 0;

                while (w >= Constantes.Bloco_Tamanho) {
                    w -= Constantes.Bloco_Tamanho;
                    PW += 1;
                }

                if (w > 0) {
                    PW += 1;
                    //    System.out.println("Sob X : " + w);
                }

                while (h >= Constantes.Bloco_Tamanho) {
                    h -= Constantes.Bloco_Tamanho;
                    PH += 1;
                }
                if (h > 0) {
                    PH += 1;
                    //     System.out.println("Sob Y : " + h);

                }
                //  System.out.println("PW : " + PW);
                // System.out.println("PH : " + PH);

                int p = PW * PH;

                // System.out.println("PARTES : " + p);



                for (int aqx = 0; aqx < mImagem_Largura; aqx++) {
                    for (int aqy = 0; aqy < mImagem_Altura; aqy++) {

                        int pixel = imagem.getRGB(aqx, aqy);

                        int blue = pixel & 0xff;
                        int green = (pixel & 0xff00) >> 8;
                        int red = (pixel & 0xff0000) >> 16;

                        Cor mNovaCor = new Cor(red, green, blue);
                        boolean tem = false;

                        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                            Cor mCorrente = mIterador.getValor();

                            if (mCorrente.igual(mNovaCor)) {
                                tem = true;
                                break;
                            }
                        }

                        if (tem == false) {
                            lsCores.adicionar(mNovaCor);
                        }

                        if (lsCores.getQuantidade() > 255) {
                            break;
                        }

                    }

                    if (lsCores.getQuantidade() > 255) {
                        break;
                    }
                }

            }

            mCores = lsCores.getQuantidade();
            if (mCores > 255) {
                return false;
            } else {
                return true;
            }

        }else{
            return false;
        }

    }

    public Lista<Cor> getAnimacaoPaleta(Lista<BufferedImage> mImagens) {

        Lista<Cor> lsCores = new Lista<Cor>();

        if (mImagens.getQuantidade() > 0) {

            int mCores = 0;

            Iterador<Cor> mIterador = new Iterador<Cor>(lsCores);


            Iterador<BufferedImage> mIteradorImagens = new Iterador<BufferedImage>(mImagens);
            for (mIteradorImagens.iniciar(); mIteradorImagens.continuar(); mIteradorImagens.proximo()) {

                BufferedImage imagem = mIteradorImagens.getValor();

                int w = imagem.getWidth();
                int h = imagem.getHeight();

                final int mImagem_Largura = w;
                final int mImagem_Altura = h;

                //  System.out.println("W : " + w);
                //  System.out.println("H : " + h);


                int PW = 0;
                int PH = 0;

                while (w >= Constantes.Bloco_Tamanho) {
                    w -= Constantes.Bloco_Tamanho;
                    PW += 1;
                }

                if (w > 0) {
                    PW += 1;
                    //    System.out.println("Sob X : " + w);
                }

                while (h >= Constantes.Bloco_Tamanho) {
                    h -= Constantes.Bloco_Tamanho;
                    PH += 1;
                }
                if (h > 0) {
                    PH += 1;
                    //     System.out.println("Sob Y : " + h);

                }
                //  System.out.println("PW : " + PW);
                // System.out.println("PH : " + PH);

                int p = PW * PH;

                // System.out.println("PARTES : " + p);



                for (int aqx = 0; aqx < mImagem_Largura; aqx++) {
                    for (int aqy = 0; aqy < mImagem_Altura; aqy++) {

                        int pixel = imagem.getRGB(aqx, aqy);

                        int blue = pixel & 0xff;
                        int green = (pixel & 0xff00) >> 8;
                        int red = (pixel & 0xff0000) >> 16;

                        Cor mNovaCor = new Cor(red, green, blue);
                        boolean tem = false;

                        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                            Cor mCorrente = mIterador.getValor();

                            if (mCorrente.igual(mNovaCor)) {
                                tem = true;
                                break;
                            }
                        }

                        if (tem == false) {
                            lsCores.adicionar(mNovaCor);
                        }

                        if (lsCores.getQuantidade() > 255) {
                            break;
                        }

                    }

                    if (lsCores.getQuantidade() > 255) {
                        break;
                    }
                }

            }



        }else{

        }

        return lsCores;

    }


    public int getPaletaMin(BufferedImage imagem) {

        int w = imagem.getWidth();
        int h = imagem.getHeight();

        final int mImagem_Largura = w;
        final int mImagem_Altura = h;

        //   System.out.println("W : " + w);
        //   System.out.println("H : " + h);


        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Bloco_Tamanho) {
            w -= Constantes.Bloco_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
            //    System.out.println("Sob X : " + w);
        }

        while (h >= Constantes.Bloco_Tamanho) {
            h -= Constantes.Bloco_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;
            //   System.out.println("Sob Y : " + h);

        }
        // System.out.println("PW : " + PW);
        // System.out.println("PH : " + PH);

        int p = PW * PH;

        //System.out.println("PARTES : " + p);


        int mCores = 0;
        Lista<Cor> lsCores = new Lista<Cor>();
        Iterador<Cor> mIterador = new Iterador<Cor>(lsCores);

        for (int aqx = 0; aqx < mImagem_Largura; aqx++) {
            for (int aqy = 0; aqy < mImagem_Altura; aqy++) {

                int pixel = imagem.getRGB(aqx, aqy);

                int blue = pixel & 0xff;
                int green = (pixel & 0xff00) >> 8;
                int red = (pixel & 0xff0000) >> 16;

                Cor mNovaCor = new Cor(red, green, blue);
                boolean tem = false;

                for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                    Cor mCorrente = mIterador.getValor();

                    if (mCorrente.igual(mNovaCor)) {
                        tem = true;
                        break;
                    }
                }

                if (tem == false) {
                    lsCores.adicionar(mNovaCor);
                }

                if (lsCores.getQuantidade() > 255) {
                    break;
                }

            }

            if (lsCores.getQuantidade() > 255) {
                break;
            }
        }

        mCores = lsCores.getQuantidade();
        return mCores;
    }

    public Lista<Cor> getPaleta(BufferedImage imagem) {

        int w = imagem.getWidth();
        int h = imagem.getHeight();

        final int mImagem_Largura = w;
        final int mImagem_Altura = h;


        Lista<Cor> mPaleta = new Lista<Cor>();
        Iterador<Cor> mIterador = new Iterador<Cor>(mPaleta);

        for (int aqx = 0; aqx < mImagem_Largura; aqx++) {
            for (int aqy = 0; aqy < mImagem_Altura; aqy++) {

                int pixel = imagem.getRGB(aqx, aqy);

                int blue = pixel & 0xff;
                int green = (pixel & 0xff00) >> 8;
                int red = (pixel & 0xff0000) >> 16;

                Cor mNovaCor = new Cor(red, green, blue);
                boolean tem = false;

                for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                    Cor mCorrente = mIterador.getValor();

                    if (mCorrente.igual(mNovaCor)) {
                        tem = true;
                        break;
                    }
                }

                if (tem == false) {
                    mPaleta.adicionar(mNovaCor);
                }

                if (mPaleta.getQuantidade() > 255) {
                    break;
                }

            }

            if (mPaleta.getQuantidade() > 255) {
                break;
            }
        }

        return mPaleta;
    }

    public int getCorDaPaleta(Lista<Cor> mPaleta, Cor eCor) {

        Iterador<Cor> mIterador = new Iterador<Cor>(mPaleta);

        boolean tem = false;
        int mIndex = 0;

        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

            Cor mCorrente = mIterador.getValor();

            if (mCorrente.igual(eCor)) {
                tem = true;
                break;
            }
            mIndex += 1;
        }
        if (tem) {
            return mIndex;
        } else {
            throw new IllegalArgumentException("Cor nao encontrada na paleta");
        }
    }

    public boolean getExisteCorDaPaleta(Lista<Cor> mPaleta, Cor eCor) {

        Iterador<Cor> mIterador = new Iterador<Cor>(mPaleta);

        boolean tem = false;

        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

            Cor mCorrente = mIterador.getValor();

            if (mCorrente.igual(eCor)) {
                tem = true;
                break;
            }
        }
        return tem;
    }


    public boolean isCinzeta(Lista<Cor> mPaleta) {

        boolean ret = true;

        Iterador<Cor> mIterador = new Iterador<Cor>(mPaleta);

        for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

            Cor mCorrente = mIterador.getValor();
            if (mCorrente.getRed() == mCorrente.getGreen() && mCorrente.getGreen() == mCorrente.getBlue()) {

            } else {
                ret = false;
                break;
            }


        }

        return ret;
    }


    public Vetor getBlocos(int w, int h) {


        final int mImagem_Largura = w;
        final int mImagem_Altura = h;

        //System.out.println("W : " + w);
        // System.out.println("H : " + h);


        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Bloco_Tamanho) {
            w -= Constantes.Bloco_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
            //   System.out.println("Sob X : " + w);
        }

        while (h >= Constantes.Bloco_Tamanho) {
            h -= Constantes.Bloco_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;
            //   System.out.println("Sob Y : " + h);

        }
        // System.out.println("PW : " + PW);
        // System.out.println("PH : " + PH);


        return new Vetor(PW, PH);
    }

    public Vetor getGamas(int w, int h) {

        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Gama_Tamanho) {
            w -= Constantes.Gama_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
        }

        while (h >= Constantes.Gama_Tamanho) {
            h -= Constantes.Gama_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;

        }


        return new Vetor(PW, PH);
    }

    public int getBlocosNum(int w, int h) {


        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Bloco_Tamanho) {
            w -= Constantes.Bloco_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
        }

        while (h >= Constantes.Bloco_Tamanho) {
            h -= Constantes.Bloco_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;
        }

        return (PW * PH);
    }

    public int getGamasNum(int w, int h) {


        int PW = 0;
        int PH = 0;

        while (w >= Constantes.Gama_Tamanho) {
            w -= Constantes.Gama_Tamanho;
            PW += 1;
        }

        if (w > 0) {
            PW += 1;
        }

        while (h >= Constantes.Gama_Tamanho) {
            h -= Constantes.Gama_Tamanho;
            PH += 1;
        }
        if (h > 0) {
            PH += 1;
        }

        return (PW * PH);
    }

    public Quad getBloco(int w, int h, int mBloco) {


        Vetor mBlocos = getBlocos(w, h);


        int PW = mBlocos.getX();

        int Y = 0;

        int mPartindo = mBloco;
        while (mPartindo >= PW) {
            mPartindo -= PW;
            Y += 1;
        }


        int mBlocoX_Inicio = mPartindo * Constantes.Bloco_Tamanho;
        int mBlocoY_Inicio = Y * Constantes.Bloco_Tamanho;

        // System.out.println("Mostrar " + mBloco + " -->> " + mBlocoX_Inicio + ":" + mBlocoY_Inicio);

        int mBlocoX_Fim = mBlocoX_Inicio + Constantes.Bloco_Tamanho;
        int mBlocoY_Fim = mBlocoY_Inicio + Constantes.Bloco_Tamanho;

        if (mBlocoX_Fim < Constantes.Bloco_Tamanho) {
            mBlocoX_Fim = w;
        } else {
            if (mBlocoX_Fim < w) {
                mBlocoX_Fim = Constantes.Bloco_Tamanho;
            } else {
                mBlocoX_Fim = (w - mBlocoX_Inicio);
                if (mBlocoX_Inicio == (mBlocoX_Inicio + mBlocoX_Fim)) {

                    mBlocoX_Fim = 0;

                }
            }
        }


        if (mBlocoX_Inicio > w) {
            mBlocoX_Fim = 0;
        }

        if (mBlocoY_Fim < Constantes.Bloco_Tamanho) {
            mBlocoY_Fim = h;
        } else {
            if (mBlocoY_Fim < h) {
                mBlocoY_Fim = Constantes.Bloco_Tamanho;
            } else {
                mBlocoY_Fim = (h - mBlocoY_Inicio);
            }
        }

        if (mBlocoY_Inicio == (mBlocoY_Inicio + mBlocoY_Fim)) {

            mBlocoY_Fim = 0;

        }


        if (mBlocoY_Fim > h) {
            mBlocoY_Fim = 0;
        }

        return new Quad(mBlocoX_Inicio, mBlocoY_Inicio, mBlocoX_Fim, mBlocoY_Fim);

    }

    public Quad getGama(int w, int h, int mBloco) {


        Vetor mGamas = getGamas(w, h);


        int PW = mGamas.getX();

        int Y = 0;

        int mPartindo = mBloco;
        while (mPartindo >= PW) {
            mPartindo -= PW;
            Y += 1;
        }


        int mBlocoX_Inicio = mPartindo * Constantes.Gama_Tamanho;
        int mBlocoY_Inicio = Y * Constantes.Gama_Tamanho;

        // System.out.println("Mostrar " + mBloco + " -->> " + mBlocoX_Inicio + ":" + mBlocoY_Inicio);

        int mBlocoX_Fim = mBlocoX_Inicio + Constantes.Gama_Tamanho;
        int mBlocoY_Fim = mBlocoY_Inicio + Constantes.Gama_Tamanho;

        if (mBlocoX_Fim < Constantes.Gama_Tamanho) {
            mBlocoX_Fim = w;
        } else {
            if (mBlocoX_Fim < w) {
                mBlocoX_Fim = Constantes.Gama_Tamanho;
            } else {
                mBlocoX_Fim = (w - mBlocoX_Inicio);
                if (mBlocoX_Inicio == (mBlocoX_Inicio + mBlocoX_Fim)) {

                    mBlocoX_Fim = 0;

                }
            }
        }


        if (mBlocoX_Inicio > w) {
            mBlocoX_Fim = 0;
        }

        if (mBlocoY_Fim < Constantes.Gama_Tamanho) {
            mBlocoY_Fim = h;
        } else {
            if (mBlocoY_Fim < h) {
                mBlocoY_Fim = Constantes.Gama_Tamanho;
            } else {
                mBlocoY_Fim = (h - mBlocoY_Inicio);
            }
        }

        if (mBlocoY_Inicio == (mBlocoY_Inicio + mBlocoY_Fim)) {

            mBlocoY_Fim = 0;

        }


        if (mBlocoY_Fim > h) {
            mBlocoY_Fim = 0;
        }

        return new Quad(mBlocoX_Inicio, mBlocoY_Inicio, mBlocoX_Fim, mBlocoY_Fim);

    }

    public int getModo(int eLargura, int eAltura) {

        int modo = 0;
        if (eLargura == Constantes.Bloco_Tamanho && eAltura == Constantes.Bloco_Tamanho) {

        } else {
            modo = 1;
        }
        return modo;
    }

    public int getAlphaModo(int eLargura, int eAltura) {

        int modo = 0;
        if (eLargura == Constantes.Gama_Tamanho && eAltura == Constantes.Gama_Tamanho) {

        } else {
            modo = 1;
        }
        return modo;
    }
}
