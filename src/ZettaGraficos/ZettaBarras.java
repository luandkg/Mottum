package AppMottum;

import java.awt.*;
import java.util.ArrayList;

public class ZettaBarras {

    private ArrayList<Integer> mValores;

    private int mLimite;
    private int mMinimo;
    private int mMaximo;
    private int mEspaco;

    private int mBase;
    private Color mCorFundo;
    private Color mCorBarra;

    private Color mCorMaiorBarra;
    private boolean mMaiorDoGrupo;

    private Color mCorMenorBarra;
    private boolean mMenorDoGrupo;


    public ZettaBarras() {

        mValores = new ArrayList<Integer>();
        mLimite = 10;

        mMinimo = 0;
        mMaximo = 100;
        mEspaco = 100;
        mBase = 0;

        mCorBarra = Color.BLUE;
        mCorFundo = Color.RED;

        mCorMaiorBarra = Color.YELLOW;
        mMaiorDoGrupo = false;

        mCorMenorBarra = Color.RED;
        mMenorDoGrupo = false;

    }

    public void limitar(int eLimite) {
        if (eLimite < 1) {
            throw new IllegalArgumentException("O limitador nao pode ser menor que 2");
        }
        mLimite = eLimite;
    }

    public void definirBase(int eBase) {

        mBase = eBase;
    }

    public void setMaiorDoGrupo(boolean eMaiorMaior) {
        mMaiorDoGrupo = eMaiorMaior;
    }

    public void setMenorDoGrupo(boolean eMenorDoGrupo) {
        mMenorDoGrupo = eMenorDoGrupo;
    }

    public void nivelar(int eMin, int eMax) {

        if (eMin >= eMax) {
            throw new IllegalArgumentException("O minimo deve ser menor que o maximo ");
        }


        mMinimo = eMin;
        mMaximo = eMax;
        mEspaco = mMaximo - mMinimo;

    }

    public void adicionar(int eValor) {
        mValores.add(eValor);
        if (mValores.size() > mLimite) {
            while (mValores.size() > mLimite) {
                mValores.remove(0);
            }
        }
    }

    public int obter(int eIndice) {
        return mValores.get(eIndice);
    }

    public int getQuantidade() {
        return mValores.size();
    }

    public int getEspaco() {
        return mEspaco;
    }

    public void setCorBarra(Color eCorBarra) {
        mCorBarra = eCorBarra;
    }

    public void setCorFundo(Color eCorFundo) {
        mCorFundo = eCorFundo;
    }

    public void setCorMaiorBarra(Color eCorMaiorBarra) {
        mCorMaiorBarra = eCorMaiorBarra;
    }

    public void setCorMenorBarra(Color eCorMenorBarra) {
        mCorMenorBarra = eCorMenorBarra;
    }


    public void onDraw(Graphics g, int eX, int eY, int eLargura, int eAltura) {

        g.setColor(mCorFundo);
        g.fillRect(eX, eY, eLargura, eAltura);

        int l = eLargura / mLimite;
        int a = eAltura / mEspaco;


        int ls = 0;

        if (getQuantidade() > 0) {


            int eMaior = obter(0);
            int eMenor = obter(0);

            for (int i = getQuantidade() - 1; i >= 0; i--) {
                if (obter(i) > eMaior) {
                    eMaior = obter(i);
                } else if (obter(i) < eMenor) {
                    eMenor = obter(i);
                }
            }


            for (int i = getQuantidade() - 1; i >= 0; i--) {
                int eValor = obter(i);

                int eOrginal = eValor;

                if (eValor < mMinimo) {
                    eValor = mMinimo;
                } else if (eValor > mMaximo) {
                    eValor = mMaximo;
                } else {

                    eValor -= mMinimo;

                }

                int eTamanho = (a * eValor);
                int comecar = eY + eAltura - eTamanho;

                g.setColor(mCorBarra);


                if (mMaiorDoGrupo) {
                    if (eOrginal == eMaior) {
                        g.setColor(mCorMaiorBarra);
                    }
                }

                if (mMenorDoGrupo) {
                    if (eOrginal == eMenor) {
                        g.setColor(mCorMenorBarra);
                    }
                }

                g.fillRect((eX + eLargura) - ls - l, comecar, l - 1, eTamanho);

                g.drawString(eOrginal + "", (eX + eLargura) - ls - (l / 2), eY + eAltura + 20);

                ls += l;
            }
        }

        g.setColor(mCorBarra);

        g.fillRect(eX, eY + eAltura + 30, eLargura, 10);


    }

    public void onDrawRect(Graphics g, int eX, int eY, int eLargura, int eAltura) {

        g.setColor(mCorFundo);
        g.fillRect(eX, eY, eLargura, eAltura);

        int l = eLargura / mLimite;
        int a = eAltura / mEspaco;

        int ls = 0;

        if (getQuantidade() > 0) {



            int eMaior = obter(0);
            int eMenor = obter(0);

            for (int i = getQuantidade() - 1; i >= 0; i--) {
                if (obter(i) > eMaior) {
                    eMaior = obter(i);
                } else if (obter(i) < eMenor) {
                    eMenor = obter(i);
                }
            }


            for (int i = getQuantidade() - 1; i >= 0; i--) {
                int eValor = obter(i);

                int eOrginal = eValor;

                if (eValor < mMinimo) {
                    eValor = mMinimo;
                } else if (eValor > mMaximo) {
                    eValor = mMaximo;
                } else {

                    eValor -= mMinimo;

                }

                int eTamanho = (a * eValor);

                if (eTamanho < 10) {
                    eTamanho = 10;
                }

                int comecar = eY + eAltura - eTamanho;

                if (eTamanho < 10) {

                } else if (eTamanho > 10) {
                    eTamanho = 10;
                }

                g.setColor(mCorBarra);


                if (mMaiorDoGrupo) {
                    if (eOrginal == eMaior) {
                        g.setColor(mCorMaiorBarra);
                    }
                }

                if (mMenorDoGrupo) {
                    if (eOrginal == eMenor) {
                        g.setColor(mCorMenorBarra);
                    }
                }

                g.fillRect((eX + eLargura) - ls - l, comecar, l - 1, eTamanho);

                g.drawString(eOrginal + "", (eX + eLargura) - ls - (l / 2), eY + eAltura + 20);

                ls += l;
            }
        }

        g.setColor(mCorBarra);

        g.fillRect(eX, eY + eAltura + 30, eLargura, 10);

    }

    public void onDrawTower(Graphics g, int eX, int eY, int eLargura, int eAltura) {

        g.setColor(mCorFundo);
        g.fillRect(eX, eY, eLargura, eAltura);

        int l = eLargura / mLimite;
        int a = eAltura / mEspaco;

        int ls = 0;
        if (getQuantidade() > 0) {



            int eMaior = obter(0);
            int eMenor = obter(0);

            for (int i = getQuantidade() - 1; i >= 0; i--) {
                if (obter(i) > eMaior) {
                    eMaior = obter(i);
                } else if (obter(i) < eMenor) {
                    eMenor = obter(i);
                }
            }


            for (int i = getQuantidade() - 1; i >= 0; i--) {
                int eValor = obter(i);

                int eOrginal = eValor;

                if (eValor < mMinimo) {
                    eValor = mMinimo;
                } else if (eValor > mMaximo) {
                    eValor = mMaximo;
                } else {

                    eValor -= mMinimo;

                }

                int eTamanho = (a * eValor);

                if (eTamanho < 10) {
                    eTamanho = 10;
                }

                int comecar = eY + eAltura - eTamanho;

                int aAntes = eTamanho;

                if (eTamanho < 10) {

                } else if (eTamanho > 10) {
                    eTamanho = 10;
                }

                g.setColor(mCorBarra);


                if (mMaiorDoGrupo) {
                    if (eOrginal == eMaior) {
                        g.setColor(mCorMaiorBarra);
                    }
                }

                if (mMenorDoGrupo) {
                    if (eOrginal == eMenor) {
                        g.setColor(mCorMenorBarra);
                    }
                }

                g.fillRect((eX + eLargura) - ls - l, comecar, l - 1, eTamanho);

                g.fillRect(((eX + eLargura) - ls) - (l / 2) - 5, comecar, 10, aAntes);

                g.drawString(eOrginal + "", (eX + eLargura) - ls - (l / 2), comecar - 10);

                ls += l;
            }


        }

        g.setColor(mCorBarra);

        g.fillRect(eX, eY + eAltura + 30, eLargura, 10);

    }

    public void onDrawQuad(Graphics g, int eX, int eY, int eLargura, int eAltura) {

        g.setColor(mCorFundo);
        g.fillRect(eX, eY, eLargura, eAltura);

        int l = eLargura / mLimite;
        int a = eAltura / mEspaco;

        int ls = 0;
        if (getQuantidade() > 0) {



            int eMaior = obter(0);
            int eMenor = obter(0);

            for (int i = getQuantidade() - 1; i >= 0; i--) {
                if (obter(i) > eMaior) {
                    eMaior = obter(i);
                } else if (obter(i) < eMenor) {
                    eMenor = obter(i);
                }
            }


            boolean primeiro = true;
            Ponto ePonto = new Ponto();

            for (int i = getQuantidade() - 1; i >= 0; i--) {
                int eValor = obter(i);

                int eOrginal = eValor;

                if (eValor < mMinimo) {
                    eValor = mMinimo;
                } else if (eValor > mMaximo) {
                    eValor = mMaximo;
                } else {

                    eValor -= mMinimo;

                }

                int eTamanho = (a * eValor);

                if (eTamanho < 10) {
                    eTamanho = 10;
                }

                int comecar = eY + eAltura - eTamanho;

                int aAntes = eTamanho;

                if (eTamanho < 10) {

                } else if (eTamanho > 10) {
                    eTamanho = 10;
                }

                g.setColor(mCorBarra);


                if (mMaiorDoGrupo) {
                    if (eOrginal == eMaior) {
                        g.setColor(mCorMaiorBarra);
                    }
                }

                if (mMenorDoGrupo) {
                    if (eOrginal == eMenor) {
                        g.setColor(mCorMenorBarra);
                    }
                }

                g.fillRect(((eX + eLargura) - ls - l), comecar, 10, 10);

                if (primeiro) {
                    primeiro = false;
                } else {

                    g.setColor(mCorBarra);

                    g.drawLine(ePonto.getX(), ePonto.getY(), (eX + eLargura) - ls - l, comecar);

                }

                ePonto.setPos((eX + eLargura) - ls - l, comecar);

                if (mMaiorDoGrupo) {
                    if (eOrginal == eMaior) {
                        g.setColor(mCorMaiorBarra);
                    }
                }

                if (mMenorDoGrupo) {
                    if (eOrginal == eMenor) {
                        g.setColor(mCorMenorBarra);
                    }
                }

                g.drawString(eOrginal + "", (eX + eLargura) - ls - (l / 2), eY + eAltura + 20);

                ls += l;
            }


        }

        g.setColor(mCorBarra);

        g.fillRect(eX, eY + eAltura + 30, eLargura, 10);

    }


}
