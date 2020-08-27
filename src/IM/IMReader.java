package IM;

import AssetContainer.FileBinary;
import Estruturas.Iterador;
import Estruturas.Lista;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IMReader {

    private IM mIM;
    private Utils mUtils;

    private BufferedImage mImagem;

    private int mImagemLargura;
    private int mImagemAltura;

    private int mImagemBlocos;
    private int mImagemGamas;
    private int mProcessandoMaximo;

    private int blocogeral;
    private int gamageral;
    private Lista<Cor> mPaletaDaImagem;

    public IMReader() {

        mIM = new IM();
        mUtils = new Utils();

    }


    public void abrirEmFluxo(String eArquivo, long eInicio) {

        try {

            RandomAccessFile raf = new RandomAccessFile(new File(eArquivo), "r");

            //  mProcessando.limpar();
            // mBlocosGravados.limpar();

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

            System.out.println("Cabecalho : " + b1 + "." + b2 + "." + b3);

            long w = fu.readLong();
            long h = fu.readLong();

            System.out.println("Tamanho : " + w + " :: " + h);

            mImagem = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);

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

            mPaletaDaImagem = new Lista<Cor>();


            byte bc = fu.readByte();
            while (bc != -1) {

                int mTipoBloco = fu.organizarByteInt(bc);

                if (mTipoBloco == Constantes.PALETA) {

                    mPaletaDaImagem = lerPaleta(fu);

                } else if (mTipoBloco == Constantes.CINZENTO_UM) {

                    lerCinzentoUm(fu);

                } else if (mTipoBloco == Constantes.CINZENTO_NORMAL) {

                    lerCinzentoNormal(fu);

                } else if (mTipoBloco == Constantes.GAMA_UM) {

                    lerGamaUm(fu);

                } else if (mTipoBloco == Constantes.GAMA_NORMAL) {

                    lerGamaNormal(fu);


                } else if (mTipoBloco == Constantes.PALETAVEL_UM) {

                    lerPaletavelUm(fu, mPaletaDaImagem);

                } else if (mTipoBloco == Constantes.PALETAVEL_NORMAL) {

                    lerPaletavelNormal(fu, mPaletaDaImagem);

                } else if (mTipoBloco == Constantes.BLOCO_UM) {

                    lerBlocoUm(fu);

                } else if (mTipoBloco == Constantes.BLOCO_PALETAVEL) {

                    lerBlocoPaletavel(fu);

                } else if (mTipoBloco == Constantes.BLOCO_NORMAL) {

                    lerBlocoNormal(fu);

                } else if (mTipoBloco == Constantes.IMAGEM_FINALIZADOR) {

                    break;

                }


                bc = fu.readByte();

                if (fu.organizarByteInt(bc) == Constantes.IMAGEM_FINALIZADOR) {
                    break;
                }

            }

            raf.close();

            System.out.println("Imagem IM - TERMINADO");

        } catch (
                IOException e) {

            e.printStackTrace();
        }
    }

    public void abrir(String eArquivo) {


        abrirEmFluxo(eArquivo, 0);


    }

    public void setPaleta(Lista<Cor> ePaleta) {
        mPaletaDaImagem = ePaleta;
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


    public Lista<Cor> lerPaleta(FileBinary fu) {


        System.out.println("\t BLOCO PALETA");
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
            System.out.println("\t\t - PALETA : " + itd + " -->> " + mIteradorPaleta.getValor().toString());
            itd += 1;
        }

        return mPaleta;
    }

    public void lerGamaUm(FileBinary fu) {

        mProcessandoMaximo += 1;
        mImagemGamas += 1;

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

        gamageral += 1;

        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerGamaNormal(FileBinary fu) {

        mProcessandoMaximo += 1;
        mImagemGamas += 1;

        Quad movendoGama = mUtils.getGama(mImagemLargura, mImagemAltura, gamageral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Gama_Tamanho;
            mAltura = Constantes.Gama_Tamanho;

            System.out.println("\t GAMA " + gamageral + " NORMAL  { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

            System.out.println("\t GAMA " + gamageral + " NORMAL  { " + movendoGama.getX() + ":" + movendoGama.getY() + " } -->> " + "{" + movendoGama.getLargura() + ":" + movendoGama.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

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


        gamageral += 1;
        //  mProcessando.adicionar(getCopia(mImagem));
        //  mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerCinzentoUm(FileBinary fu) {


        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

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


            System.out.println("\t BLOCO " + blocogeral + " CINZENTO UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            mCor = fu.organizarByteInt(fu.readByte());

            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

            System.out.println("\t BLOCO " + blocogeral + " CINZENTO UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }

        System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

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

        blocogeral += 1;

        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerCinzentoNormal(FileBinary fu) {

        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


            System.out.println("\t BLOCO " + blocogeral + " CINZENTO NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

            System.out.println("\t BLOCO " + blocogeral + " CINZENTO NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

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


        blocogeral += 1;
        //  mProcessando.adicionar(getCopia(mImagem));
        //  mBlocosGravados.adicionar(imagemBloco);

    }


    public void lerBlocoUm(FileBinary fu) {

        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

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


            System.out.println("\t BLOCO " + blocogeral + " UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL COR UNICA : " + eCor.toString());


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

            System.out.println("\t BLOCO " + blocogeral + " UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " } -->> COR UNICA : " + eCor.toString());

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

        blocogeral += 1;

        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerPaletavelUm(FileBinary fu, Lista<Cor> mPaletaDaImagem) {


        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

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


            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL -->> COR UNICA : " + mCor);


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //  System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            //  System.out.println("\t\t - LARGURA : " + mLargura);
            //  System.out.println("\t\t - ALTURA : " + mAltura);

            mCor = fu.organizarByteInt(fu.readByte());

            //   System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL UM { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " } -->> COR UNICA : " + mCor);

        }

        // System.out.println("\t\t - COR UNICA DA PALETA : " + mCor);

        int mQuantidade = mLargura * mAltura;

        Cor eCor = mPaletaDaImagem.getValor(mCor);

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

        blocogeral += 1;

        //mProcessando.adicionar(getCopia(mImagem));
        //mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerPaletavelNormal(FileBinary fu, Lista<Cor> mPaletaDaImagem) {

        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;

            mAltura = Constantes.Bloco_Tamanho;


            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

        }


        int mQuantidade = mLargura * mAltura;

        int bx = 0;
        int by = 0;

        // BufferedImage imagemBloco = new BufferedImage(movendoBloco.getX2() - movendoBloco.getX(), movendoBloco.getY2() - movendoBloco.getY(), BufferedImage.TYPE_INT_ARGB);

        int pi = 0;
        while (pi < mQuantidade) {

            int mCor = fu.organizarByteInt(fu.readByte());

            Cor eCor = mPaletaDaImagem.getValor(mCor);

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


        blocogeral += 1;
        //  mProcessando.adicionar(getCopia(mImagem));
        //  mBlocosGravados.adicionar(imagemBloco);

    }

    public void lerBlocoNormal(FileBinary fu) {


        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

        Quad movendoBloco = mUtils.getBloco(mImagemLargura, mImagemAltura, blocogeral);


        int mTipoModo = fu.organizarByteInt(fu.readByte());

        int mLargura = 0;
        int mAltura = 0;

        if (mTipoModo == Constantes.MODO_NORMAL) {
            // System.out.println("\t\t - MODO : NORMAL ");

            mLargura = Constantes.Bloco_Tamanho;
            mAltura = Constantes.Bloco_Tamanho;


            System.out.println("\t BLOCO " + blocogeral + " NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

            System.out.println("\t BLOCO " + blocogeral + " NORMAL  { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

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


        blocogeral += 1;
        //  mProcessando.adicionar(getCopia(mImagem));
        //  mBlocosGravados.adicionar(imagemBloco);

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

    public void lerBlocoPaletavel(FileBinary fu) {


        mProcessandoMaximo += 1;
        mImagemBlocos += 1;

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


            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL de " + mPaletaCom + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> NORMAL ");


        } else if (mTipoModo == Constantes.MODO_PERSONALIZADO) {
            //System.out.println("\t\t - MODO : PERSONALIZADO ");

            mLargura = fu.organizarByteInt(fu.readByte());
            mAltura = fu.organizarByteInt(fu.readByte());
            mPaletaCom = fu.organizarByteInt(fu.readByte());

            // System.out.println("\t\t - LARGURA : " + mLargura);
            // System.out.println("\t\t - ALTURA : " + mAltura);

            System.out.println("\t BLOCO " + blocogeral + " PALETAVEL de " + mPaletaCom + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + " } -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + " } -->> PERSONALIZADO { " + mLargura + "." + mAltura + " }");

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


        blocogeral += 1;
        //  mProcessando.adicionar(getCopia(mImagem));
        //  mBlocosGravados.adicionar(imagemBloco);

    }

    public void setImagem(BufferedImage aImagem) {
        mImagem = aImagem;
    }
}
