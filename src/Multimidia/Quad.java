package IM;

public class Quad {

    private int mX;
    private int mY;

    private int mLargura;
    private int mAltura;

    public Quad(int eX, int eY, int eLargura, int eAltura) {
        this.mX = eX;
        this.mY = eY;
        this.mLargura = eLargura;
        this.mAltura = eAltura;

    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getLargura() {
        return mLargura;
    }

    public int getAltura() {
        return mAltura;
    }

    public void setX(int eX) {
        mX = eX;
    }

    public void setY(int eY) {
        mY = eY;
    }

    public void setLargura(int eLargura) {
        mLargura = eLargura;
    }

    public void setAltura(int eAltura) {
        mAltura = eAltura;
    }

    public int getX2(){return mX+mLargura;}
    public int getY2(){return mY+mAltura;}

    public boolean Igual(Quad q2) {

        boolean ret = false;

        if (this.getX() == q2.getX() && this.getY() == q2.getY() && this.getLargura() == q2.getLargura() && this.getAltura() == q2.getAltura() ) {
            ret = true;
        }

        return ret;

    }

    public String toString() {
        return "X : " + mX + " Y : " + mY + " L : " + mLargura + " A : " + mAltura;
    }
}
