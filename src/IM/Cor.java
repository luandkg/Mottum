package IM;

import java.awt.*;

public class Cor {

    private int mAlpha;

    private int mRed;
    private int mGreen;
    private int mBlue;

    public Cor() {

        mAlpha = 0;

        mRed = 0;
        mGreen = 0;
        mBlue = 0;
    }

    public Cor(int eRed, int eGreen, int eBlue) {

        mAlpha = 0;

        mRed = eRed;
        mGreen = eGreen;
        mBlue = eBlue;
    }

    public void setAlpha(int eAlpha) {
        mAlpha = eAlpha;
    }

    public int getAlpha() {
        return mAlpha;
    }


    public void setRed(int eRed) {
        mRed = eRed;
    }

    public void setGreen(int eGreen) {
        mGreen = eGreen;
    }

    public void setBlue(int eBlue) {
        mBlue = eBlue;
    }

    public int getRed() {
        return mRed;
    }

    public int getGreen() {
        return mGreen;
    }

    public int getBlue() {
        return mBlue;
    }

    public boolean igual(Cor eCor) {
        if (this.getRed() == eCor.getRed() && this.getGreen() == eCor.getGreen() && this.getBlue() == eCor.getBlue()) {
            return true;
        } else {
            return false;
        }
    }


    public boolean alphaIgual(Cor eCor) {
        if (this.getAlpha() == eCor.getAlpha() ) {
            return true;
        } else {
            return false;
        }
    }


    // MODIFICADORES RGB

    public Cor aumentar(int eVermelho,int eVerde,int eAzul) {
        int tmp_vermelho = this.getRed() + eVermelho;
        if (tmp_vermelho > 255) {
            tmp_vermelho = 255;
        }
        if (tmp_vermelho < 0) {
            tmp_vermelho = 0;
        }

        int tmp_verde = this.getGreen() + eVerde;
        if (tmp_verde > 255) {
            tmp_verde = 255;
        }
        if (tmp_verde < 0) {
            tmp_verde = 0;
        }

        int tmp_azul = this.getBlue() + eAzul;
        if (tmp_azul > 255) {
            tmp_azul = 255;
        }
        if (tmp_azul < 0) {
            tmp_azul = 0;
        }

        Cor aCor = new Cor(tmp_vermelho,tmp_verde,tmp_azul);
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor aumentarVermelho(int eVermelho) {
        int tmp = this.getRed() + eVermelho;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(tmp,this.getGreen(),this.getBlue());
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor aumentarVerde(int eVerde) {
        int tmp = this.getGreen() + eVerde;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(this.getRed(),tmp,this.getBlue());
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor aumentarAzul(int eAzul) {
        int tmp = this.getBlue() + eAzul;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(this.getRed(),this.getGreen(),eAzul);
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor reduzir(int eVermelho,int eVerde,int eAzul) {
        int tmp_vermelho = this.getRed() - eVermelho;
        if (tmp_vermelho > 255) {
            tmp_vermelho = 255;
        }
        if (tmp_vermelho < 0) {
            tmp_vermelho = 0;
        }

        int tmp_verde = this.getGreen() -eVerde;
        if (tmp_verde > 255) {
            tmp_verde = 255;
        }
        if (tmp_verde < 0) {
            tmp_verde = 0;
        }

        int tmp_azul = this.getBlue() - eAzul;
        if (tmp_azul > 255) {
            tmp_azul = 255;
        }
        if (tmp_azul < 0) {
            tmp_azul = 0;
        }

        Cor aCor = new Cor(tmp_vermelho,tmp_verde,tmp_azul);
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }


    public int redux(int aValor){
        while(aValor>255){
            aValor-=255;
        }
        while(aValor<0){
            aValor+=255;
        }
        return aValor;
    }

    public Cor reduzirVermelho(int eVermelho) {
        int tmp = this.getRed() - eVermelho;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(tmp,this.getGreen(),this.getBlue());
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor reduzirVerde(int eVerde) {
        int tmp = this.getGreen() - eVerde;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(this.getRed(),tmp,this.getBlue());
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Cor reduzirAzul(int eAzul) {
        int tmp = this.getBlue() - eAzul;
        if (tmp > 255) {
            tmp = 255;
        }
        if (tmp < 0) {
            tmp = 0;
        }

        Cor aCor = new Cor(this.getRed(),this.getGreen(),eAzul);
        aCor.setAlpha(this.getAlpha());

        return aCor;
    }

    public Color toColor() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
    }

    public String toString() {
        return "{" + mRed + "." + mGreen + "." + mBlue + "}";
    }
}
