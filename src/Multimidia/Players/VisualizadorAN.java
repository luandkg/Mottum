package VisulizadorMultimidia;

import Estruturas.Iterador;
import Estruturas.Lista;
import Estruturas.Vetor;

import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Cronometro;
import Mottum.Utils.Escritor;
import Mottum.Windows;
import Multimidia.An.AN;
import Multimidia.An.AnimadorCriador;
import Multimidia.Cor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class VisualizadorAN extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;
    private Cronometro mCron;

    BotaoCor BTN_P1;
    BotaoCor BTN_P2;

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

        BTN_P1 = new BotaoCor(300, 50, 100, 100, new Color(26, 188, 156));
        BTN_P2 = new BotaoCor(500, 50, 100, 100, new Color(26, 188, 156));

        mImagens = new Lista<BufferedImage>();


        // criarAnimacao_01();

        // criarAnimacao_02();

        AnimadorCriador mAnimadorCriador = new AnimadorCriador();
        mAnimadorCriador.criarAnimacao_01();

        criado = true;

        i = 0;
        o = 0;
        mCron = new Cronometro(200);

        abrir();

        Vetor<Cor> mVetor = new Vetor<Cor>(3);
        System.out.println("Capacidade : " + mVetor.getCapacidade());

        mVetor.aumentar(1);
        mVetor.set(0, new Cor(15, 1, 1));
        mVetor.set(1, new Cor(1, 1, 1));
        mVetor.set(2, new Cor(20, 80, 89));
        mVetor.set(3, new Cor(12, 6, 53));

        Iterador<Cor> mIC = new Iterador<Cor>(mVetor);

        for (mIC.iniciar(); mIC.continuar(); mIC.proximo()) {
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


                if (BTN_P1.getClicado(px, py)) {

                    System.out.println("Criar 1");

                    AnimadorCriador mAnimadorCriador = new AnimadorCriador();
                    mAnimadorCriador.criarAnimacao_01();

                    criado = true;


                    abrir();
                } else if (BTN_P2.getClicado(px, py)) {

                    System.out.println("Criar 2");

                    AnimadorCriador mAnimadorCriador = new AnimadorCriador();
                    mAnimadorCriador.criarAnimacao_02();

                    criado = true;


                    abrir();
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
        BTN_P1.draw(g);
        BTN_P2.draw(g);

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
