package IM;

import Estruturas.Iterador;
import Estruturas.Lista;
import Estruturas.Vetor;

import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Cronometro;
import Mottum.Utils.Escritor;
import Mottum.Windows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class VisualizadorAN extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;
    private Cronometro mCron;

    BotaoCor BTN_CRIAR;

    Clicavel mClicavel;

    String eArquivoAbrir = "anim.png";
    String eArquivoAN = "a.an";


    Lista<BufferedImage> mImagens;
    private int i;
    private int o;
    private boolean criado = false;

    public VisualizadorAN(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_CRIAR = new BotaoCor(300, 50, 100, 100, new Color(26, 188, 156));

        mImagens = new Lista<BufferedImage>();


        // criarAnimacao_01();

        // criarAnimacao_02();

         criarAnimacao_03();

        abrir();

        Vetor<Cor> mVetor = new Vetor<Cor>(3);
        System.out.println("Capacidade : " + mVetor.getCapacidade());

        mVetor.aumentar(1);
        mVetor.set(0, new Cor(15, 1, 1));
        mVetor.set(1, new Cor(1, 1, 1));
        mVetor.set(2, new Cor(20, 80, 89));
        mVetor.set(3, new Cor(12, 6, 53));

        Iterador<Cor> mIC = new Iterador<Cor>(mVetor);

        for (mIC.iniciar(); mIC.continuar(); mIC.proximo() ){
            System.out.println("\t - Iterando " + mIC.getIndice() + " : " + mIC.getValor());
        }

        System.out.println("Capacidade : " + mVetor.getCapacidade());

        mVetor.reduzir(2);

        System.out.println("Capacidade : " + mVetor.getCapacidade());

        System.out.println("1 : " + mVetor.get(1).toString());

    }

    public void abrir() {

        AN mAN = new AN();
        mAN.abrir(eArquivoAN);

        i = 0;
        mImagens = mAN.getImagens();
        o = mAN.getQuantidade();
        mCron = new Cronometro(mAN.getChrono());


    }

    public void criarAnimacao_01() {

        try {

            BufferedImage mIMGOriginal = ImageIO.read(new File(eArquivoAbrir));

            int tam = 100;
            Lista<BufferedImage> mLista = new Lista<BufferedImage>();

            mLista.adicionar(getSprite(mIMGOriginal, 10, 280, tam, 150));
            mLista.adicionar(getSprite(mIMGOriginal, 10 + (1 * tam), 280, tam, 150));
            mLista.adicionar(getSprite(mIMGOriginal, 10 + (2 * tam), 280, tam, 150));
            mLista.adicionar(getSprite(mIMGOriginal, 10 + (3 * tam), 280, tam, 150));
            mLista.adicionar(getSprite(mIMGOriginal, 10 + (4 * tam), 280, tam, 150));
            mLista.adicionar(getSprite(mIMGOriginal, 10 + (5 * tam), 280, tam, 150));

            AN mAN = new AN();
            mAN.criar(mLista, 200, "a.an");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void criarAnimacao_02() {

        Lista<BufferedImage> mLista = new Lista<BufferedImage>();

        BufferedImage Q1 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q1, Color.WHITE);

        // mLista.adicionar(Q1);

        BufferedImage Q2 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q2, Color.WHITE);
        pintarQuadranteGeral(Q2, Color.BLACK);

        mLista.adicionar(Q2);

        BufferedImage Q3 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q3, Color.WHITE);
        pintarQuadrante(Q3, 1, Color.RED);
        pintarQuadranteGeral(Q3, Color.BLACK);

        mLista.adicionar(Q3);

        BufferedImage Q4 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q4, Color.WHITE);
        pintarQuadrante(Q4, 1, Color.RED);
        pintarQuadrante(Q4, 2, Color.RED);
        pintarQuadranteGeral(Q4, Color.BLACK);

        mLista.adicionar(Q4);

        BufferedImage Q5 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q5, Color.WHITE);
        pintarQuadrante(Q5, 1, Color.RED);
        pintarQuadrante(Q5, 2, Color.RED);
        pintarQuadrante(Q5, 3, Color.RED);

        pintarQuadranteGeral(Q5, Color.BLACK);

        mLista.adicionar(Q5);

        BufferedImage Q6 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q6, Color.WHITE);
        pintarQuadrante(Q6, 1, Color.RED);
        pintarQuadrante(Q6, 2, Color.RED);
        pintarQuadrante(Q6, 3, Color.RED);
        pintarQuadrante(Q6, 4, Color.RED);

        pintarQuadranteGeral(Q6, Color.BLACK);

        mLista.adicionar(Q6);

        BufferedImage Q7 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q7, Color.WHITE);
        pintarQuadrante(Q7, 2, Color.RED);
        pintarQuadrante(Q7, 3, Color.RED);
        pintarQuadrante(Q7, 4, Color.RED);

        pintarQuadranteGeral(Q7, Color.BLACK);

        mLista.adicionar(Q7);

        BufferedImage Q8 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q8, Color.WHITE);
        pintarQuadrante(Q8, 3, Color.RED);
        pintarQuadrante(Q8, 4, Color.RED);

        pintarQuadranteGeral(Q8, Color.BLACK);

        mLista.adicionar(Q8);

        BufferedImage Q9 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q9, Color.WHITE);
        pintarQuadrante(Q9, 4, Color.RED);

        pintarQuadranteGeral(Q9, Color.BLACK);

        mLista.adicionar(Q9);

        BufferedImage Q10 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q10, Color.WHITE);

        pintarQuadranteGeral(Q10, Color.BLACK);

        mLista.adicionar(Q10);

        criado = true;

        i = 0;
        mImagens = mLista;
        o = mLista.getQuantidade();
        mCron = new Cronometro(200);

        AN mAN = new AN();
        mAN.criar(mLista, 200, "a.an");

    }

    public void criarAnimacao_03() {

        Lista<BufferedImage> mLista = new Lista<BufferedImage>();

        BufferedImage Q1 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q1, Color.WHITE);

        // mLista.adicionar(Q1);

        BufferedImage Q2 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q2, Color.WHITE);
        pintarQuadranteGeral(Q2, Color.BLACK);

        mLista.adicionar(Q2);

        BufferedImage Q3 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q3, Color.WHITE);
        pintarQuadrante(Q3, 1, Color.BLACK);
        pintarQuadranteGeral(Q3, Color.BLACK);

        mLista.adicionar(Q3);

        BufferedImage Q4 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q4, Color.WHITE);
        pintarQuadrante(Q4, 1, Color.BLACK);
        pintarQuadrante(Q4, 2, Color.BLACK);
        pintarQuadranteGeral(Q4, Color.BLACK);

        mLista.adicionar(Q4);

        BufferedImage Q5 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q5, Color.WHITE);
        pintarQuadrante(Q5, 1, Color.BLACK);
        pintarQuadrante(Q5, 2, Color.BLACK);
        pintarQuadrante(Q5, 3, Color.BLACK);

        pintarQuadranteGeral(Q5, Color.BLACK);

        mLista.adicionar(Q5);

        BufferedImage Q6 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q6, Color.WHITE);
        pintarQuadrante(Q6, 1, Color.BLACK);
        pintarQuadrante(Q6, 2, Color.BLACK);
        pintarQuadrante(Q6, 3, Color.BLACK);
        pintarQuadrante(Q6, 4, Color.BLACK);

        pintarQuadranteGeral(Q6, Color.BLACK);

        mLista.adicionar(Q6);

        BufferedImage Q7 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q7, Color.WHITE);
        pintarQuadrante(Q7, 2, Color.BLACK);
        pintarQuadrante(Q7, 3, Color.BLACK);
        pintarQuadrante(Q7, 4, Color.BLACK);

        pintarQuadranteGeral(Q7, Color.BLACK);

        mLista.adicionar(Q7);

        BufferedImage Q8 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q8, Color.WHITE);
        pintarQuadrante(Q8, 3, Color.BLACK);
        pintarQuadrante(Q8, 4, Color.BLACK);

        pintarQuadranteGeral(Q8, Color.BLACK);

        mLista.adicionar(Q8);

        BufferedImage Q9 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q9, Color.WHITE);
        pintarQuadrante(Q9, 4, Color.BLACK);

        pintarQuadranteGeral(Q9, Color.BLACK);

        mLista.adicionar(Q9);

        BufferedImage Q10 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        pintarTudo(Q10, Color.WHITE);

        pintarQuadranteGeral(Q10, Color.BLACK);

        mLista.adicionar(Q10);

        criado = true;

        i = 0;
        mImagens = mLista;
        o = mLista.getQuantidade();
        mCron = new Cronometro(200);

        AN mAN = new AN();
        mAN.criar(mLista, 200, "a.an");

    }


    public void pintarTudo(BufferedImage IMG, Color eCor) {

        for (int aqy = 0; aqy < (IMG.getHeight()); aqy++) {

            for (int aqx = 0; aqx < (IMG.getWidth()); aqx++) {

                int pixel = eCor.getRGB();

                IMG.setRGB(aqx, aqy, pixel);
            }

        }

    }


    public void pintarQuadrante(BufferedImage IMG, int eQuadro, Color eCor) {

        if (eQuadro == 1) {
            for (int aqy = 0; aqy < (IMG.getHeight() / 2); aqy++) {

                for (int aqx = 0; aqx < (IMG.getWidth() / 2); aqx++) {

                    int pixel = eCor.getRGB();

                    IMG.setRGB(aqx, aqy, pixel);
                }

            }
        } else if (eQuadro == 2) {
            for (int aqy = 0; aqy < (IMG.getHeight() / 2); aqy++) {

                for (int aqx = (IMG.getWidth() / 2); aqx < (IMG.getWidth()); aqx++) {

                    int pixel = eCor.getRGB();

                    IMG.setRGB(aqx, aqy, pixel);
                }

            }
        } else if (eQuadro == 3) {
            for (int aqy = (IMG.getHeight() / 2); aqy < (IMG.getHeight()); aqy++) {

                for (int aqx = (IMG.getWidth() / 2); aqx < (IMG.getWidth()); aqx++) {

                    int pixel = eCor.getRGB();

                    IMG.setRGB(aqx, aqy, pixel);
                }

            }
        } else if (eQuadro == 4) {
            for (int aqy = (IMG.getHeight() / 2); aqy < (IMG.getHeight()); aqy++) {

                for (int aqx = 0; aqx < (IMG.getWidth() / 2); aqx++) {

                    int pixel = eCor.getRGB();

                    IMG.setRGB(aqx, aqy, pixel);
                }

            }
        }


    }

    public void pintarQuadranteGeral(BufferedImage IMG, Color eCor) {

        for (int aqy = 0; aqy < (IMG.getHeight()); aqy++) {

            for (int aqx = (IMG.getWidth() / 2) - 5; aqx < (IMG.getWidth() / 2) + 5; aqx++) {

                int pixel = eCor.getRGB();

                IMG.setRGB(aqx, aqy, pixel);
            }

        }

        for (int aqy = (IMG.getHeight() / 2) - 5; aqy < (IMG.getHeight() / 2) + 5; aqy++) {

            for (int aqx = 0; aqx < (IMG.getWidth()); aqx++) {

                int pixel = eCor.getRGB();

                IMG.setRGB(aqx, aqy, pixel);
            }

        }


    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Visualizador AN");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        if (mImagens.getQuantidade() > 0) {
            mCron.Esperar();

            if (mCron.Esperado()) {
                i += 1;
                if (i >= o) {
                    i = 0;
                }
            }

            if (mClicavel.getClicado()) {

                int px = (int) mWindows.getMouse().x;
                int py = (int) mWindows.getMouse().y;


                if (BTN_CRIAR.getClicado(px, py)) {

                    System.out.println("Criar");

                    AN mAn = new AN();
                    mAn.criar(mImagens, 150, eArquivoAN);

                }

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


    public BufferedImage getSprite(BufferedImage aImagem, int ax, int ay, int tx, int ty) {

        BufferedImage mSprite = new BufferedImage(tx, ty, BufferedImage.TYPE_INT_ARGB);

        int bx = 0;

        for (int x = ax; x < (ax + tx); x++) {
            int by = 0;
            for (int y = ay; y < (ay + ty); y++) {

                int p = aImagem.getRGB(x, y);

                mSprite.setRGB(bx, by, p);
                by += 1;

            }
            bx += 1;
        }

        return mSprite;
    }


    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);
        BTN_CRIAR.draw(g);

        g.setColor(Color.RED);

        TextoGrande.EscreveNegrito(g, "Visualizador AN", 20, 80);

        if (mImagens.getQuantidade() > 0) {

            BufferedImage mTmp = mImagens.getValor(i);

            g.drawImage(mTmp, 200, 200, mTmp.getWidth(), mTmp.getHeight(), null);


        }

        if (criado) {

            BufferedImage mTmp = mImagens.getValor(i);

            g.drawImage(mTmp, 200, 200, mTmp.getWidth(), mTmp.getHeight(), null);

        }


    }

}
