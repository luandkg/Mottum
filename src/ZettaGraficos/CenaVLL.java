package ZettaGraficos;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Mottum.Utils.Cronometro;
import Mottum.Windows;
import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;


public class VLL extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;

    BotaoCor BTN_CRIAR;
    BotaoCor BTN_REMOVER;
    BotaoCor BTN_ALEATORIO;

    Clicavel mClicavel;

    private ZettaBarras mZettaBarras;

    Cronometro mCron;
    Cronometro mCronRapido;

    double mVelocidade;
    int mTempo;
    int mContador;

    int mVelocidadeCorrente;
    int mVelocidadeAcumulando;

    int mNivel;
    String mQualidade;

    boolean mAleatorizando;
    int mAleatorioIndice;
    int mVelocidadeAleatoriaBase;

    private int mModo;

    public VLL(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(15, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_CRIAR = new BotaoCor(450, 50, 100, 100, new Color(26, 188, 156));
        BTN_REMOVER = new BotaoCor(550, 50, 100, 100, new Color(200, 130, 50));

        BTN_ALEATORIO = new BotaoCor(400, 160, 300, 50, new Color(140, 80, 120));


        mZettaBarras = new ZettaBarras();

        mZettaBarras.setCorBarra(new Color(30, 50, 80));
        mZettaBarras.setCorFundo(Color.WHITE);

        mZettaBarras.setMaiorDoGrupo(true);
        mZettaBarras.setCorMaiorBarra(new Color(10, 150, 160));

        mZettaBarras.setMenorDoGrupo(true);
        mZettaBarras.setCorMenorBarra(new Color(200, 60, 150));


        mZettaBarras.nivelar(0, 100);

        mCron = new Cronometro(600);
        mCronRapido = new Cronometro(300);

        mVelocidade = 0;
        mTempo = 0;
        mContador = 0;
        mVelocidadeCorrente = 0;
        mVelocidadeAcumulando = 0;
        mNivel = 1;
        mQualidade = "";
        mAleatorizando = true;
        mAleatorioIndice = 0;
        mVelocidadeAleatoriaBase = 0;
        mModo = 0;

    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Algoritmo VLL");
    }


    public void gerarBomba() {

        Random eSorte = new Random();

        mVelocidadeAleatoriaBase = eSorte.nextInt(100);
        mVelocidadeCorrente = mVelocidadeAleatoriaBase;

        mModo = eSorte.nextInt(100);

        String mQualModo = "Diminuindo";

        if (mModo > 50) {
            mQualModo = "Aumentando";
        }

        System.out.println("Gerar Bloco");
        System.out.println("\t - Base = " + mVelocidadeAleatoriaBase);
        System.out.println("\t - Modo = " + mQualModo);


    }

    public void realimentarBomba() {

        Random eSorte = new Random();

        if (mModo > 50) {
            mVelocidadeCorrente = mVelocidadeAleatoriaBase + eSorte.nextInt(20);
        } else {
            mVelocidadeCorrente = mVelocidadeAleatoriaBase - eSorte.nextInt(20);
        }

        if (mVelocidadeCorrente > 100) {
            mVelocidadeCorrente = mVelocidadeCorrente - eSorte.nextInt(30) + 20;
        }

        if (mVelocidadeCorrente < 0) {
            mVelocidadeCorrente = mVelocidadeCorrente + eSorte.nextInt(30) + 20;
        }

    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        //System.out.println("Clicavel : " + mClicavel.getClicado());

        mCronRapido.Esperar();
        if (mCronRapido.Esperado()) {

            Random eSorte = new Random();

            mVelocidadeCorrente = 0;

            if (mAleatorizando) {

                if (mAleatorioIndice == 0) {

                    gerarBomba();


                } else {

                    realimentarBomba();

                }

                if (mVelocidadeCorrente > 100) {
                    mVelocidadeCorrente = 100;
                } else if (mVelocidadeCorrente < 0) {
                    mVelocidadeCorrente = 0;
                }

                mAleatorioIndice += 1;

                if (mAleatorioIndice > 15) {

                    mAleatorizando = true;
                    mAleatorioIndice = 0;
                }

            } else {

                if (mNivel == 1) {

                    mVelocidadeCorrente = eSorte.nextInt(33);

                } else if (mNivel == 2) {

                    mVelocidadeCorrente = 33 + eSorte.nextInt(33);

                } else {

                    mVelocidadeCorrente = 66 + eSorte.nextInt(33);

                }


            }


            mVelocidadeAcumulando += mVelocidadeCorrente;

            mZettaBarras.adicionar(mVelocidadeCorrente);


            mContador += 1;
            if (mContador > 10) {
                mContador = 0;
                mTempo += 1;
                mVelocidade = ((double) mVelocidadeCorrente) / 10.0F;
                mVelocidadeAcumulando = 0;

                if (mVelocidade >= 0 && mVelocidade < 3) {
                    mQualidade = "Baixissima";
                }

                if (mVelocidade >= 3 && mVelocidade < 4) {
                    mQualidade = "Baixa";
                }

                if (mVelocidade >= 4 && mVelocidade < 6) {
                    mQualidade = "Media";
                }

                if (mVelocidade >= 6 && mVelocidade < 8) {
                    mQualidade = "Alta";
                }

                if (mVelocidade >= 8) {
                    mQualidade = "Altissima";
                }

            }
        }


        int epx = (int) mWindows.getMouse().x;
        int epy = (int) mWindows.getMouse().y;

        mCron.Esperar();

        if (mCron.Esperado()) {

            Random gerador = new Random();

            // mZettaBarras.adicionar(100 - gerador.nextInt(200));


        }

        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


            if (BTN_CRIAR.getClicado(px, py)) {
                // System.out.println("Criar");


                // Random gerador = new Random();

                //     mZettaBarras.adicionar(100 - gerador.nextInt(200));

                mNivel += 1;

                if (mNivel > 3) {
                    mNivel = 3;
                }

            } else if (BTN_REMOVER.getClicado(px, py)) {
                //   System.out.println("Remover");

                mNivel -= 1;

                if (mNivel < 1) {
                    mNivel = 1;
                }

            } else if (BTN_ALEATORIO.getClicado(px, py)) {

                mAleatorizando = true;
                mAleatorioIndice = 0;

            }


        } else {

            //mRect.setTamanho(100, 100);
        }


    }

    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);

        if (!mAleatorizando) {
            if (mNivel < 3) {
                BTN_CRIAR.draw(g);
            }

            if (mNivel > 1) {
                BTN_REMOVER.draw(g);
            }

            BTN_ALEATORIO.draw(g);
        }


        g.setColor(Color.RED);


        mZettaBarras.onDraw(g, 100, 400, 600, 300);

        TextoGrande.Escreve(g, "Nivel = " + mNivel, 50, 100);
        TextoGrande.Escreve(g, "Contador = " + mContador, 50, 150);
        TextoGrande.Escreve(g, "Tempo = " + mTempo + " s", 50, 200);
        TextoGrande.Escreve(g, "Velocidade Corrente = " + getComCasas(mVelocidadeCorrente, 2) + " m/s", 50, 250);
        TextoGrande.Escreve(g, "Velocidade Media = " + getComCasas(mVelocidade, 2) + " m/s", 50, 300);

        if (mQualidade.length() == 0) {
            TextoGrande.Escreve(g, "Qualidade = esperando analise para calcular...", 50, 350);
        } else {
            TextoGrande.Escreve(g, "Qualidade = " + mQualidade, 50, 350);
        }


    }

    public String getComCasas(double d, int c) {

        String ret = "";

        String v = String.valueOf(d);

        int i = 0;
        int o = v.length();


        boolean mPontuou = false;
        int p = 0;

        while (i < o) {
            String l = String.valueOf(v.charAt(i));

            if (mPontuou) {
                if (p < c) {
                    ret += l;
                }
                p += 1;
            } else {
                if (l.contentEquals(".")) {
                    mPontuou = true;
                    if (c > 0) {
                        ret += l;
                    }
                } else {
                    ret += l;
                }
            }


            i += 1;
        }

        return ret;

    }

}
