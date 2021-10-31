package Ambiente;

import Mottum.Estrutural.Rect;

import java.awt.*;

public class Renderizador {

    private Graphics g;

    public Renderizador() {
        g = null;
    }

    public void draw(Graphics eg) {
        g = eg;
    }


    public void drawRect(Rect mRetangulo) {

        g.drawRect(mRetangulo.getX(), mRetangulo.getY(), mRetangulo.getLargura(), mRetangulo.getAltura());


    }

    public void drawRect(Rect mRetangulo, Color eCor) {
        g.setColor(eCor);
        g.drawRect(mRetangulo.getX(), mRetangulo.getY(), mRetangulo.getLargura(), mRetangulo.getAltura());
    }


    public void drawFillRect(Rect mRetangulo, Color eCor) {
        g.setColor(eCor);
        g.fillRect(mRetangulo.getX(), mRetangulo.getY(), mRetangulo.getLargura(), mRetangulo.getAltura());
    }
}
