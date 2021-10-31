package AppMottum;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import Mottum.Utils.Cronometro;
import Mottum.Windows;
import Mottum.Cenarios.Cena;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;


public class ZettaCena extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;

    BotaoCor BTN_CRIAR;
    BotaoCor BTN_REMOVER;

    Clicavel mClicavel;

    private ZettaBarras mZettaBarras;

    Cronometro mCron;

    public ZettaCena(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_CRIAR = new BotaoCor(400, 50, 100, 100, new Color(26, 188, 156));
        BTN_REMOVER = new BotaoCor(550, 50, 100, 100, new Color(26, 188, 156));


        mZettaBarras = new ZettaBarras();

        mZettaBarras.setCorBarra(new Color(30,50,80));
        mZettaBarras.setCorFundo(Color.WHITE);

        mZettaBarras.setMaiorDoGrupo(true);
        mZettaBarras.setCorMaiorBarra(new Color(10,150,160));

        mZettaBarras.setMenorDoGrupo(true);
        mZettaBarras.setCorMenorBarra(new Color(200,60,150));


        mZettaBarras.nivelar(-100,100);

        mCron = new Cronometro(600);

    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Zetta Graficos - Luan Freitas");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        //System.out.println("Clicavel : " + mClicavel.getClicado());

        int epx = (int) mWindows.getMouse().x;
        int epy = (int) mWindows.getMouse().y;

        mCron.Esperar();

        if (mCron.Esperado()){

            Random gerador = new Random();

            mZettaBarras.adicionar(100-gerador.nextInt(200));


        }

        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


            if (BTN_CRIAR.getClicado(px, py)) {
                System.out.println("Criar");


                Random gerador = new Random();

                mZettaBarras.adicionar(100-gerador.nextInt(200));

            } else if (BTN_REMOVER.getClicado(px, py)) {
                System.out.println("Remover");


            }


        } else {

            //mRect.setTamanho(100, 100);
        }

    }

    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);
     //   BTN_CRIAR.draw(g);
      //  BTN_REMOVER.draw(g);

        g.setColor(Color.RED);

        for (int i = 0; i < mZettaBarras.getQuantidade(); i++) {
            //System.out.println("Valor " + i + " :: " + mZettaBarras.obter(i));
        }


        mZettaBarras.onDraw(g, 100, 200, 600, 300);

        mZettaBarras.onDrawRect(g, 800, 200, 600, 300);

        mZettaBarras.onDrawTower(g, 100, 600, 600, 300);

        mZettaBarras.onDrawQuad(g, 800, 600, 600, 300);


    }


}
