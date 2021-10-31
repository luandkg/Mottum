package IM;

import Estruturas.Iterador;
import Estruturas.Lista;

public class BlocoIM {

    private Lista<Cor> mPaleta;

    private Lista<Cor> mCores;

    private Iterador<Cor> mIterador;

    private int mTipo;
    private int mMinimo;

    public BlocoIM() {


        mCores = new Lista<Cor>();
        mPaleta = new Lista<Cor>();

        mIterador = new Iterador<Cor>(mPaleta);

        mTipo = 0;
        mMinimo=50;

    }

    public void mapear(Cor mCor) {

        boolean tem = false;

        mCores.adicionar(mCor);

        if (mPaleta.getQuantidade() < mMinimo+1) {

            for (mIterador.iniciar(); mIterador.continuar(); mIterador.proximo()) {

                Cor mCorrente = mIterador.getValor();

                if (mCorrente.igual(mCor)) {
                    tem = true;
                    break;
                }
            }

            if (tem == false) {
                mPaleta.adicionar(mCor);
            }

        }


        mTipo = 0;

        if (mPaleta.getQuantidade() <= mMinimo) {

            mTipo = 10;

            if (mPaleta.getQuantidade() == 1) {
                mTipo = 1;
            }

        }


    }

    public int getTipo() {
        return mTipo;
    }

    public int getTipoAlpha() {

        int mTipoAlpha = 1;

        boolean primeiro = true;
        int ultimo = 0;

        Iterador<Cor> mIteradorDecimal = new Iterador<Cor>(getCores());

        for (mIteradorDecimal.iniciar(); mIteradorDecimal.continuar(); mIteradorDecimal.proximo()) {
            if (primeiro) {
                primeiro = false;
                ultimo = mIteradorDecimal.getValor().getAlpha();
            } else {
                if (mIteradorDecimal.getValor().getAlpha() == ultimo) {

                } else {
                    mTipoAlpha = 0;
                    break;
                }
            }
        }

        return mTipoAlpha;
    }

    public Lista<Cor> getPaleta() {
        return mPaleta;
    }

    public Lista<Cor> getCores() {
        return mCores;
    }

    public int getAlphaUnico() {
        Cor mCorUnica = new Cor(0, 0, 0);

        Iterador<Cor> mIteradorDecimal = new Iterador<Cor>(getCores());

        for (mIteradorDecimal.iniciar(); mIteradorDecimal.continuar(); mIteradorDecimal.proximo()) {
            mCorUnica = mIteradorDecimal.getValor();
            break;
        }
        return mCorUnica.getAlpha();
    }


    public Cor getCorUnica() {
        Cor mCorUnica = new Cor(0, 0, 0);

        Iterador<Cor> mIteradorDecimal = new Iterador<Cor>(getCores());

        for (mIteradorDecimal.iniciar(); mIteradorDecimal.continuar(); mIteradorDecimal.proximo()) {
            // System.out.println("\t\t - Cor Unica : " + itd + " -->> " + mIM.getCorDaPaleta(mPaleta, mIteradorDecimal.getValor()));
            mCorUnica = mIteradorDecimal.getValor();
        }
        return mCorUnica;
    }

    public boolean isDecimal() {
        if (mPaleta.getQuantidade() <= mMinimo) {
            return true;
        } else {
            return false;
        }
    }
}
