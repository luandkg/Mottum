package VisulizadorMultimidia;

import Estruturas.Lista;
import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Cronometro;
import Mottum.Utils.Escritor;
import Mottum.Windows;
import Multimidia.Movie.Movie;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VisualizadorMV extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;
    private Cronometro mCron;

    BotaoCor BTN_P1;
    BotaoCor BTN_P2;

    Clicavel mClicavel;

    String eArquivoAbrir = "movie.mv";


    Lista<BufferedImage> mImagens;
    private int i;
    private int o;
    private boolean mAberto = false;
    private boolean mCarregado = false;

    private Movie mMovie;
    private boolean mPausado;

    private int mTotal;

    public VisualizadorMV(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_P1 = new BotaoCor(300, 50, 50, 50, new Color(26, 188, 156));
        BTN_P2 = new BotaoCor(500, 50, 50, 50, new Color(255, 50, 80));

        mImagens = new Lista<BufferedImage>();

        mPausado = false;
        mTotal = 0;

        i = 0;
        o = 0;
        mCron = new Cronometro(200);

        System.out.println("Carregar video...");

        abrir();

        System.out.println("Iniciar video...");

        mTotal = mMovie.getFramesTotal();

        System.out.println("\t - Quadros = " + mMovie.getQuadros().size());
        System.out.println("\t - Frames = " + mTotal);
        System.out.println("\t - Duracao = " + toSegundos(mMovie.getDuracao()) + " s");
        System.out.println("\t - Duracao = " + getTempoFormatado(toSegundos(mMovie.getDuracao())));

        int mIrAte = 25;

        if (mTotal > 0) {
            if (mIrAte > 0) {

                double mTaxinha = (double) mTotal / 100.0;
                double mPorcentagem = (double) mMovie.getFrameCorrente() / mTaxinha;
                int mEstou = (int) mPorcentagem;

                while (mEstou < mIrAte) {
                    mPorcentagem = (double) mMovie.getFrameCorrente() / mTaxinha;
                    mEstou = (int) mPorcentagem;
                    mMovie.avancar();
                }

                mMovie.lerFrame();


            }

        }

    }

    public String getTempoFormatado(int eTotalSegundos) {

        int eHoras = 0;
        int eMinutos = 0;
        int eSegundos = eTotalSegundos;


        while (eSegundos >= 60) {
            eSegundos -= 60;
            eMinutos += 1;
        }

        while (eMinutos >= 60) {
            eMinutos -= 60;
            eHoras += 1;
        }


        return eHoras + ":" + eMinutos + ":" + eSegundos;
    }

    public int toSegundos(int eNumero) {
        int eContando = 0;
        while (eNumero >= 1000) {
            eNumero -= 1000;
            eContando += 1;
        }
        return eContando;
    }

    public void abrir() {

        mMovie = new Movie();
        mMovie.abrir(eArquivoAbrir);

        i = 0;

        mCron = new Cronometro(mMovie.getTaxa());

        mAberto = true;




    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Visualizador MV");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        if (!mPausado) {
            mCron.Esperar();

            if (mCron.Esperado()) {

                if (!mMovie.getAcabou()) {
                    mMovie.proximo();
                    mCarregado = true;
                }

            }

        }

        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


            if (BTN_P1.getClicado(px, py)) {

                if (!mMovie.getAcabou()) {
                    mMovie.proximo();
                    mCarregado = true;
                }


            } else if (BTN_P2.getClicado(px, py)) {

                mPausado = !mPausado;

            }

        }


    }


    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);
        BTN_P1.draw(g);
        BTN_P2.draw(g);

        g.setColor(Color.RED);

        TextoGrande.EscreveNegrito(g, "Visualizador MV", 20, 80);


        if (mAberto && mCarregado) {
            g.drawImage(mMovie.getImagemCorrente(), 100, 100, mMovie.getImagemCorrente().getWidth(), mMovie.getImagemCorrente().getHeight(), null);
        }

        int mAlargador = 5;

        g.setColor(Color.BLACK);
        g.fillRect(300, 950, 100 * mAlargador, 10);

        if (mAberto && mCarregado) {

            if (mTotal > 0) {

                double mTaxinha = (double) mTotal / 100.0;

                double mPorcentagem = (double) mMovie.getFrameCorrente() / mTaxinha;
                int iPorcento = (int) mPorcentagem;

                int eFrame = iPorcento * 5;

                // System.out.println("Porcentagem - " +iPorcento );

                g.setColor(Color.RED);
                g.fillRect(300 + eFrame, 950, 5, 10);

            }

        }

    }

}
