package VisulizadorMultimidia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Estruturas.Iterador;
import Estruturas.Lista;
import Mottum.Windows;
import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;
import Multimidia.Chunk;
import Multimidia.Cor;
import Multimidia.Im.IM;
import Multimidia.Im.IMChunker;
import Multimidia.Quad;
import Multimidia.Utils;

import javax.imageio.ImageIO;


public class VisualizadorIM extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;

    private BotaoCor BTN_CRIAR;
    private BotaoCor BTN_ABRIR;

    private BotaoCor BTN_PROCESSO;

    private Clicavel mClicavel;

    private String eArquivoAbrir = "imagem3.png";
    private String eArquivoIM = "imagem.im";

    private BufferedImage imagem;
    private boolean mTemImagem;

    private int mParte = 0;

    private IM mIM;
    private Utils mUtils;

    boolean mProcessando = false;
    int mProcessandoID = 0;
    boolean salvo;
    boolean aberto;
    private int mProcessandoMaximo;

    private Lista<Chunk> mChunks;

    public VisualizadorIM(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_CRIAR = new BotaoCor(300, 50, 100, 100, new Color(26, 188, 156));
        BTN_ABRIR = new BotaoCor(450, 50, 100, 100, new Color(200, 200, 50));
        BTN_PROCESSO = new BotaoCor(600, 50, 100, 100, new Color(70, 40, 156));

        salvo = false;
        aberto = false;
        mProcessandoMaximo -= 1;
        mIM = new IM();
        mUtils = new Utils();

        mTemImagem = false;
        imagem = null;

        mChunks = new Lista<Chunk>();

        try {

            imagem = ImageIO.read(new File(eArquivoAbrir));

            //toEscalaDeCinza(imagem);

            //  mIM.salvarAntigo(imagem, eArquivoIM);
            mIM.salvar(imagem, eArquivoIM);


        } catch (IOException e) {
            e.printStackTrace();
        }

        // mIM = IM.Criar(100,100);
        // mIM.mudarTudo(Color.RED);
        //  imagem=mIM.getImagem();

    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Visualizador IM");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


            if (BTN_CRIAR.getClicado(px, py)) {


                System.out.println(" -->> Salvar IM");


                mIM.salvar(imagem, eArquivoIM);

                salvo = true;


            } else if (BTN_ABRIR.getClicado(px, py)) {

                mProcessando = false;
                mProcessandoID = 0;
                mProcessandoID = -1;

                System.out.println(" -->> Abrir IM");

                mIM.abrir(eArquivoIM);

                IMChunker eIMChunker = new IMChunker();

                mChunks.limpar();

                eIMChunker.abrir(eArquivoIM);
                mChunks = eIMChunker.getChunks();

                System.out.println("Largura : " + mIM.getLargura());
                System.out.println("Altura : " + mIM.getAltura());
                System.out.println("Blocos : " + mIM.getImagemBlocos());
                System.out.println("Gamas : " + mIM.getImagemGamas());
                System.out.println("Processos : " + mIM.getImagemProcessos());


                imagem = mIM.getImagem();
                mTemImagem = true;

                mProcessandoMaximo = mIM.getImagemProcessos();
                mProcessandoID = 0;

                aberto = false;

            } else if (BTN_PROCESSO.getClicado(px, py)) {


                if (mProcessandoID < mProcessandoMaximo) {
                    mProcessandoID += 1;
                }

                mIM.abrirAte(eArquivoIM, mProcessandoID);

                imagem = mIM.getImagem();
            }


        } else {

            //mRect.setTamanho(100, 100);
        }

    }

    public void alterar() {

        for (int xmeio = 0; xmeio < mIM.getLargura(); xmeio++) {
            for (int ymeio = 0; ymeio < mIM.getAltura(); ymeio++) {


                // Cor aCor = mIM.getPixel(xmeio,ymeio);
                //aCor = aCor.aumentar(100,-100,40);

                //mIM.setPixel(xmeio,ymeio,aCor);
            }
        }

    }

    public void toEscalaDeCinza(BufferedImage aImagem) {

        for (int x = 0; x < aImagem.getWidth(); x++) {
            for (int y = 0; y < aImagem.getHeight(); y++) {

                int p = aImagem.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                aImagem.setRGB(x, y, p);

            }
        }

    }

    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);
        BTN_CRIAR.draw(g);
        BTN_ABRIR.draw(g);
        BTN_PROCESSO.draw(g);

        g.setColor(Color.RED);

        TextoGrande.EscreveNegrito(g, "Visualizador IM", 20, 80);

        if (mTemImagem) {
            g.drawImage(imagem, 200, 200, imagem.getWidth(), imagem.getHeight(), null);


            int w = imagem.getWidth();
            int h = imagem.getHeight();

            IM mIM = new IM();


            Quad mBloco = mUtils.getBloco(w, h, mParte);


            //TextoGrande.EscreveNegrito(g, "IMAGEM { " + w + ":" + h + " } -->> " + mBlocosN, 800, 100);


            int modo = mUtils.getModo(mBloco.getLargura(), mBloco.getAltura());


            // TextoGrande.EscreveNegrito(g, "BLOCO " + mParte + " -->> " + mBloco.getX() + ":" + mBloco.getY() + "{" + mBloco.getLargura() + ":" + mBloco.getAltura() + "} -->> " + modo, 800, 200);

            boolean mPaletavel = mUtils.isPaletavel(imagem);

            if (mPaletavel) {

                int mCores = mUtils.getPaletaMin(imagem);

                // TextoGrande.EscreveNegrito(g, "PALETAVEL : SIM ", 900, 300);
                //TextoGrande.EscreveNegrito(g, "PALETA " + mCores, 900, 400);

                Lista<Cor> lsCores = mUtils.getPaleta(imagem);
                Iterador<Cor> mIterador = new Iterador<Cor>(lsCores);
                int it = 0;
                for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {
                    // System.out.println("Cor : " + it + " -->> " + mIterador.getValor().toString());
                    it += 1;
                }

            } else {


                // TextoGrande.EscreveNegrito(g, "PALETAVEL : NAO ", 900, 300);

            }

            int mX = 900;
            int mY = 200;

            int mQuad = 10;
            int mi = 0;
            int mimax = 60;

            Iterador<Chunk> mIteradorChunk = new Iterador<Chunk>(mChunks);

            for (mIteradorChunk.iniciar(); mIteradorChunk.continuar(); mIteradorChunk.proximo()) {

                Chunk eChunk = mIteradorChunk.getValor();

                Color eCor = Color.BLACK;

                if (eChunk == Chunk.PALETA) {
                    eCor = Color.YELLOW;
                } else if (eChunk == Chunk.BLOCO_UM) {
                    eCor = Color.GREEN;
                } else if (eChunk == Chunk.BLOCO_NORMAL) {
                    eCor = Color.RED;
                } else if (eChunk == Chunk.BLOCO_PALETAVEL) {
                    eCor = Color.ORANGE;

                } else if (eChunk == Chunk.PALETAVEL_UM) {
                    eCor = new Color(150, 80, 140);
                } else if (eChunk == Chunk.PALETAVEL_NORMAL) {
                    eCor = new Color(150, 120, 140);

                } else if (eChunk == Chunk.GAMA_UM) {
                    eCor = new Color(10, 120, 140);
                } else if (eChunk == Chunk.GAMA_NORMAL) {
                    eCor = new Color(10, 60, 120);
                }

                g.setColor(eCor);

                g.fillRect(mX + (mi * mQuad), mY, mQuad - 2, mQuad - 2);
                mi += 1;
                if (mi > mimax) {
                    mi = 0;
                    mY += mQuad;
                }
            }


            if (salvo) {

                draw_salvos(g);

            }

            if (aberto) {

                draw_abertos(g);

            }


        }
    }


    public void draw_bloco(Graphics g) {

        IM mIM = new IM();

        int w = imagem.getWidth();
        int h = imagem.getHeight();

        Quad copiarBloco = mUtils.getBloco(w, h, mParte);

        for (int aqx = copiarBloco.getX(); aqx < (copiarBloco.getX2()); aqx++) {

            for (int aqy = copiarBloco.getY(); aqy < (copiarBloco.getY2()); aqy++) {

                int pixel = imagem.getRGB(aqx, aqy);

                int blue = pixel & 0xff;
                int green = (pixel & 0xff00) >> 8;
                int red = (pixel & 0xff0000) >> 16;

                g.setColor(new Color(red, green, blue));
                g.fillRect(900 + (aqx - copiarBloco.getX()), 500 + (aqy - copiarBloco.getY()), 1, 1);
            }

        }

    }


    public void draw_salvos(Graphics g) {


        int mBlocosN = mIM.getBlocosGravados().getQuantidade();

        int TGA = 100;

        // System.out.println("Imagem  " + " { " + imagem.getWidth() + ":" + imagem.getHeight() + "}");

        for (int a = 0; a < mBlocosN; a++) {


            BufferedImage mQuad = mIM.getBlocosGravados().getValor(a);

            //   System.out.println("Bloco " + a + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + "} -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + "}");

            int dx = 0;

            for (int aqx = 0; aqx < (mQuad.getWidth()); aqx++) {
                int dy = 0;
                for (int aqy = 0; aqy < (mQuad.getHeight()); aqy++) {

                    int pixel = mQuad.getRGB(aqx, aqy);

                    int blue = pixel & 0xff;
                    int green = (pixel & 0xff00) >> 8;
                    int red = (pixel & 0xff0000) >> 16;

                    g.setColor(new Color(red, green, blue));
                    g.fillRect(1350 + dx, TGA + dy, 1, 1);
                    dy += 1;
                }
                dx += 1;
            }

            TGA += 100 + 10;

        }

    }

    public void draw_abertos(Graphics g) {


        int mBlocosN = mIM.getBlocosGravados().getQuantidade();

        int TGA = 100;

        // System.out.println("Imagem  " + " { " + imagem.getWidth() + ":" + imagem.getHeight() + "}");

        for (int a = 0; a < mBlocosN; a++) {


            BufferedImage mQuad = mIM.getBlocosGravados().getValor(a);

            //   System.out.println("Bloco " + a + " { " + movendoBloco.getX() + ":" + movendoBloco.getY() + "} -->> " + "{" + movendoBloco.getLargura() + ":" + movendoBloco.getAltura() + "}");

            int dx = 0;

            for (int aqx = 0; aqx < (mQuad.getWidth()); aqx++) {
                int dy = 0;
                for (int aqy = 0; aqy < (mQuad.getHeight()); aqy++) {

                    int pixel = mQuad.getRGB(aqx, aqy);

                    int blue = pixel & 0xff;
                    int green = (pixel & 0xff00) >> 8;
                    int red = (pixel & 0xff0000) >> 16;

                    g.setColor(new Color(red, green, blue));
                    g.fillRect(1350 + dx, TGA + dy, 1, 1);
                    dy += 1;
                }
                dx += 1;
            }

            TGA += 100 + 10;

        }

    }


}
