package Fisica;

import Mottum.Estrutural.LinhaHorizontal;
import Mottum.Estrutural.LinhaVertical;

import java.awt.*;

public class FitaMetricaHorizontal {

    private int mIntervalo;
    private int mOrigem;

    public FitaMetricaHorizontal(int eOrigem, int eIntervalo) {
        mOrigem = eOrigem;
        mIntervalo = eIntervalo;
    }

    public void render(Graphics g, int eInicio, int eFim, int eY) {

        g.setColor(Color.BLACK);

        int cx = eInicio;
        int eAtual = mOrigem;

        while (cx < eFim) {

             g.drawLine(cx, eY-20, cx, eY);

            g.drawString(eAtual + " ", cx+5, eY);
            cx += mIntervalo;
            eAtual += mIntervalo;
        }


    }

}
