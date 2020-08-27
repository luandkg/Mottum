package IM;

public class Vetor{

    private int mX;
    private int mY;

    public Vetor(int eX, int eY) {
        this.mX = eX;
        this.mY = eY;

    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public void setX(int eX) {
        mX = eX;
    }

    public void setY(int eY) {
        mY = eY;
    }

    public boolean Igual(Vetor v2) {

        boolean ret = false;

        if (this.getX() == v2.getX() && this.getY() == v2.getY()) {
            ret = true;
        }

        return ret;

    }

    public String toString() {
        return "X : " + mX + " Y : " + mY;
    }
}
