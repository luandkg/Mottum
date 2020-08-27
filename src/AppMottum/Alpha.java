package AppMottum;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import Mottum.Windows;
import Mottum.Cenarios.Cena;
import Mottum.Estrutural.Rect;
import Mottum.UI.BotaoCor;
import Mottum.UI.Clicavel;
import Mottum.Utils.Escritor;
import Mottum.Utils.Imaginador;
import Mottum.Utils.Local;
import Mottum.Utils.Recurso;
import Volume.Volume;
import Volume.Arquivo;


public class Alpha extends Cena {

    private Windows mWindows;

    private Escritor TextoGrande;

    BotaoCor BTN_CRIAR;
    BotaoCor BTN_REMOVER;

    Clicavel mClicavel;
    byte[] mMapa;

    String eVolumeLocal = "volume.vl";

    public Alpha(Windows eWindows) {
        mWindows = eWindows;

        TextoGrande = new Escritor(30, Color.BLACK);

        mClicavel = new Clicavel();

        BTN_CRIAR = new BotaoCor(400, 50, 100, 100, new Color(26, 188, 156));
        BTN_REMOVER = new BotaoCor(550, 50, 100, 100, new Color(26, 188, 156));


        Volume vl = new Volume(eVolumeLocal);

        mMapa = vl.getMapa();

        vl.fechar();

    }


    @Override
    public void iniciar() {
        mWindows.setTitle("Alpha");
    }

    @Override
    public void update(double dt) {

        mClicavel.update(dt, mWindows.getMouse().Pressed());

        //System.out.println("Clicavel : " + mClicavel.getClicado());

        int epx = (int) mWindows.getMouse().x;
        int epy = (int) mWindows.getMouse().y;


        if (mClicavel.getClicado()) {

            int px = (int) mWindows.getMouse().x;
            int py = (int) mWindows.getMouse().y;


            if (BTN_CRIAR.getClicado(px, py)) {
                System.out.println("Criar");


                Volume vl = new Volume(eVolumeLocal);
                UUID uuid = UUID.randomUUID();

                for (int t = 0; t < 10; t++) {
                    vl.criarArquivo("TEXTO " + uuid.toString() + ".txt");
                }

                mMapa = vl.getMapa();

                vl.fechar();


            } else if (BTN_REMOVER.getClicado(px, py)) {
                System.out.println("Remover");


                Volume vl = new Volume(eVolumeLocal);

                int r = 0;
                int rm = 10;

                for (Arquivo mA : vl.getArquivos()) {
                    if (r < rm) {
                     // vl.remover_arquivo(mA.getNome());
                      mA.limpar();
                    }
                    r += 1;
                }


                mMapa = vl.getMapa();

                vl.fechar();


            }


        } else {

            //mRect.setTamanho(100, 100);
        }

    }

    @Override
    public void draw(Graphics g) {

        mWindows.Limpar(g);
        BTN_CRIAR.draw(g);
        BTN_REMOVER.draw(g);

        g.setColor(Color.RED);

        TextoGrande.EscreveNegrito(g, "MAPA DE CLUSTER", 20, 80);

        System.out.println();

        int oX = 100;

        int eX = oX;
        int eY = 200;

        int e = 0;
        int t = 0;
        int pagina = 100;
        int paginas = 0;

        int o = mMapa.length;

        while (e < o) {

            if (t > pagina) {
                t = 0;
                eX = oX;
                eY += 1;
                paginas += 1;
            }

            int v = mMapa[e];

            if (v == 1) {
                g.setColor(Color.GREEN);

            } else {
                g.setColor(Color.RED);

            }

            g.fillRect(eX + (t * 10), eY + (paginas * 10), 10, 10);

            eX += 1;

            e += 1;
            t += 1;

        }

    }


}
