package AppMottum;

public class Ponto {

    private int mX;
    private int mY;

    public Ponto(){
        mX=0;
        mY=0;
    }

    public Ponto(int eX,int eY){
        mX=eX;
        mY=eY;
    }

    public int getX(){return mX;}
    public int getY(){return mY;}

    public void setPos(int eX,int eY){
        mX=eX;
        mY=eY;
    }

    public void setX(int eX){
        mX=eX;
    }

    public void setY(int eY){
        mY=eY;
    }

}