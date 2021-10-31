package IM;

import AssetContainer.FileBinary;
import Estruturas.Iterador;
import Estruturas.Lista;
import Multimidia.Quad;
import Multimidia.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SetoresLer {

    private Utils mUtils;

    public SetoresLer() {
        mUtils = new Utils();

    }

    public void lerGamaUm(FileBinary fu, int mImagemLargura, int mImagemAltura, int gamageral, BufferedImage mImagem) {


        Quad movendoGama = mUtils.getGama(mImagemLargura, mImagemAltura, gamageral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        int mAlpha = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");


            mLargura = Constantes.Gama_Tamanho;
            mAltura = Constantes.Gama_Tamanho;

            mAlpha = fu.organizarByteInt(fu.readByte());


            System.out.println("\t GAMA " + gamageral + " UM { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> NORMAL -->> ALFA UNICO : " + mAlpha);


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            mAlpha = fu.organizarByteInt(fu.readByte());

            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

          System.out.println("\t GAMA " + gamageral + " UM { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " } -->> ALFA UNICO : " + mAlpha);

        }

        // System.out.println("\t\t - GAMA UNICO : " + mAlpha);

        int mQuantidade = mLargura * mAltura;


        int pi = 0;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoGama.getX2() - movendoGama.getX(), movendoGama.getY2() - movendoGama.getY(), BufferedImage.TYPE_INT_RGB);


        while (pi < mQuantidade) {

            //    System.out.println("-->> x " +(movendoBloco.getX()+bx )+ " e " + (movendoBloco.getY()+by) );

            int pixel = mImagem.getRGB((movendoGama.getX() + bx), (movendoGama.getY() + by));

            int blue = pixel & 0xff;
            int green = (pixel & 0xff00) >> 8;
            int red = (pixel & 0xff0000) >> 16;

            mImagem.setRGB((movendoGama.getX() + bx), (movendoGama.getY() + by), new Color(red, green, blue, mAlpha).getRGB());


            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {

                bx = 0;
                by += 1;

            }


        }


        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }

    public Lista<Cor> lerPaleta(FileBinary fu) {


        //System.out.println("\t BLOCO PALETA");
        int p2 = fu.organizarByteInt(fu.readByte());
        //  System.out.println("P1 : " + (int)bc);
        //  System.out.println("P2 : "+ p2);

        Lista<Cor> mPaleta = new Lista<Cor>();

        Iterador<Cor> mIteradorPaleta = new Iterador<Cor>(mPaleta);


        int pi = 0;
        while (pi < p2) {

            int red = fu.organizarByteInt(fu.readByte());
            int green = fu.organizarByteInt(fu.readByte());
            int blue = fu.organizarByteInt(fu.readByte());

            mPaleta.adicionar(new Cor(red, green, blue));

            pi += 1;
        }

        int itd = 0;
        for (mIteradorPaleta.iniciar(); mIteradorPaleta.continuar(); mIteradorPaleta.proximo()) {
           // System.out.println("\t\t - PALETA : " + itd + " -->> " + mIteradorPaleta.getValor().toString());
            itd += 1;
        }

        return mPaleta;
    }

    public void lerGamaNormal(FileBinary fu, int mImagemLargura, int mImagemAltura, int gamageral, BufferedImage mImagem) {



        Quad movendoGama = mUtils.getGama(mImagemLargura, mImagemAltura, gamageral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Gama_Tamanho;
            mAltura = Constantes.Gama_Tamanho;

           // System.out.println("\t GAMA " + gamageral + " NORMAL  { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

        //    System.out.println("\t GAMA " + gamageral + " NORMAL  { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }


        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_ARGB);

        int pi = 0;
        while (pi < mQuantidade) {

            int mAlpha = fu.organizarByteInt(fu.readByte());

            if ((movendoGama.getX() + bx) < mImagem.getWidth()) {

                if ((movendoGama.getY() + by) < mImagem.getHeight()) {


                    int pixel = mImagem.getRGB((movendoGama.getX() + bx), (movendoGama.getY() + by));

                    int blue = pixel & 0xff;
                    int green = (pixel & 0xff00) >> 8;
                    int red = (pixel & 0xff0000) >> 16;


                    mImagem.setRGB((movendoGama.getX() + bx), (movendoGama.getY() + by), new Color(red, green, blue, mAlpha).getRGB());

                }

            }

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {
                bx = 0;
                by += 1;
            }

        }



    }

    public void lerPaletavelUm(FileBinary fu, Lista<Cor> mPaletaDaImagem,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {




        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;
        int mCor = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");


            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


            mCor = fu.organizarByteInt(fu.readByte());


          //  System.out.println("\t BLOCO " + blocogeral + " PALETAVEL UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL -->> COR UNICA : " + mCor);


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            mCor = fu.organizarByteInt(fu.readByte());

            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

        //    System.out.println("\t BLOCO " + blocogeral + " PALETAVEL UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " } -->> COR UNICA : " + mCor);

        }

        // System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

        int mQuantidade = mLargura * mAltura;

        Cor eCor = mPaletaDaImagem.getValor(mCor);

        int pi = 0;

        int bx = 0;
        int by = 0;

        BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);


        while (pi < mQuantidade) {

               // System.out.println("-->> x " +(movendoBloco.getX()+bx )+ " e " + (movendoBloco.getY()+by) );

            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            imagemBloco.setRGB(bx, by, new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {

                bx = 0;
                by += 1;

            }


        }


        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }


    public void lerPaletavelNormal(FileBinary fu, Lista<Cor> mPaletaDaImagem,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {

        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);

        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


          //  System.out.println("\t BLOCO " + blocogeral + " PALETAVEL NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

         //   System.out.println("\t BLOCO " + blocogeral + " PALETAVEL NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }


        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        int pi = 0;
        while (pi < mQuantidade) {

            int mCor = fu.organizarByteInt(fu.readByte());

            Cor eCor = mPaletaDaImagem.getValor(mCor);

            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {
                bx = 0;
                by += 1;
            }

        }



    }

    public void lerBlocoUm(FileBinary fu,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {

        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;
        Cor eCor = new Cor(0, 0, 0);

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");


            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


            eCor.setRed(fu.organizarByteInt(fu.readByte()));
            eCor.setGreen(fu.organizarByteInt(fu.readByte()));
            eCor.setBlue(fu.organizarByteInt(fu.readByte()));


       //     System.out.println("\t BLOCO " + blocogeral + " UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL COR UNICA : " + eCor.toString());


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            eCor.setRed(fu.organizarByteInt(fu.readByte()));
            eCor.setGreen(fu.organizarByteInt(fu.readByte()));
            eCor.setBlue(fu.organizarByteInt(fu.readByte()));
            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

       //     System.out.println("\t BLOCO " + blocogeral + " UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " } -->> COR UNICA : " + eCor.toString());

        }

        //System.out.println("\t\t - COR UNICA : " + eCor.toString());

        int mQuantidade = mLargura * mAltura;


        int pi = 0;

        int bx = 0;
        int by = 0;

        BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);


        while (pi < mQuantidade) {

            //    System.out.println("-->> x " +(movendoBloco.getX()+bx )+ " e " + (movendoBloco.getY()+by) );

            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            imagemBloco.setRGB(bx, by, new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {

                bx = 0;
                by += 1;

            }


        }


        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }


    public void lerBlocoPaletavel(FileBinary fu,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {



        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;
        int mPaletaCom = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;
            mAltura = Constantes.Bloco_Tamanho;
            mPaletaCom = fu.organizarByteInt(fu.readByte());


           // System.out.println("\t BLOCO " + blocogeral + " PALETAVEL de " + mPaletaCom + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());
            mPaletaCom = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

      //      System.out.println("\t BLOCO " + blocogeral + " PALETAVEL de " + mPaletaCom + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }

        Lista<Cor> mPaleta = new Lista<Cor>();

        int mi = 0;
        while (mi < mPaletaCom) {

            Cor mCor = new Cor(0, 0, 0);

            mCor.setRed(fu.organizarByteInt(fu.readByte()));
            mCor.setGreen(fu.organizarByteInt(fu.readByte()));
            mCor.setBlue(fu.organizarByteInt(fu.readByte()));

            mPaleta.adicionar(mCor);

            mi += 1;
        }

        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);

        int pi = 0;
        while (pi < mQuantidade) {

            Cor mCor = mPaleta.getValor(fu.organizarByteInt(fu.readByte()));


            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(mCor.getRed(), mCor.getGreen(), mCor.getBlue()).getRGB());
            //  imagemBloco.setRGB(bx, by, new Color(mCor.getRed(), mCor.getGreen(), mCor.getBlue()).getRGB());


            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {
                bx = 0;
                by += 1;
            }

        }



    }

    public void lerCinzentoUm(FileBinary fu,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {



        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;
        int mCor = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");


            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


            mCor = fu.organizarByteInt(fu.readByte());


         //   System.out.println("\t BLOCO " + blocogeral + " CINZENTO UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            mCor = fu.organizarByteInt(fu.readByte());

            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

       //     System.out.println("\t BLOCO " + blocogeral + " CINZENTO UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }

      //  System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

        int mQuantidade = mLargura * mAltura;

        Cor eCor = new Cor(mCor, mCor, mCor);

        int pi = 0;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);


        while (pi < mQuantidade) {

            //    System.out.println("-->> x " +(movendoBloco.getX()+bx )+ " e " + (movendoBloco.getY()+by) );

            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            // imagemBloco.setRGB(bx, by, new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());

            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {

                bx = 0;
                by += 1;

            }


        }


    }

    public void lerCinzentoNormal(FileBinary fu,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {


        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


         //   System.out.println("\t BLOCO " + blocogeral + " CINZENTO NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

        //    System.out.println("\t BLOCO " + blocogeral + " CINZENTO NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }


        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_ARGB);

        int pi = 0;
        while (pi < mQuantidade) {

            int mCor = fu.organizarByteInt(fu.readByte());

            Cor eCor = new Cor(mCor, mCor, mCor);

            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());
            // imagemBloco.setRGB(bx, by, new Color(eCor.getRed(), eCor.getGreen(), eCor.getBlue()).getRGB());


            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {
                bx = 0;
                by += 1;
            }

        }


    }


    public void lerBlocoNormal(FileBinary fu,int mImagemLargura, int mImagemAltura, int blocogeral, BufferedImage mImagem) {


        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;
            mAltura = Constantes.Bloco_Tamanho;


        //    System.out.println("\t BLOCO " + blocogeral + " NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

         //   System.out.println("\t BLOCO " + blocogeral + " NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }


        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        //BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_RGB);

        int pi = 0;
        while (pi < mQuantidade) {

            Cor mCor = new Cor(0, 0, 0);

            mCor.setRed(fu.organizarByteInt(fu.readByte()));
            mCor.setGreen(fu.organizarByteInt(fu.readByte()));
            mCor.setBlue(fu.organizarByteInt(fu.readByte()));


            mImagem.setRGB((movendoBloco.getX() + bx), (movendoBloco.getY() + by), new Color(mCor.getRed(), mCor.getGreen(), mCor.getBlue()).getRGB());
            // imagemBloco.setRGB(bx, by, new Color(mCor.getRed(), mCor.getGreen(), mCor.getBlue()).getRGB());


            // System.out.print(mCor + " ");

            pi += 1;

            bx += 1;
            if (bx >= mLargura) {
                bx = 0;
                by += 1;
            }

        }



    }


}
