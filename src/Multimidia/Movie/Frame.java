package Movie;

import IM.BinaryUtils;

public class Frame {

    private BinaryUtils mArquivo;
    private int mIndice;
    private long mInicio;

    public Frame(BinaryUtils eArquivo, int eIndice, long eInicio) {
        mArquivo = eArquivo;
        mIndice = eIndice;
        mInicio = eInicio;

    }

    public int getIndex() {
        return mIndice;
    }

    public long getInicio() {
        return mInicio;
    }

    public long getFim() {
        return mInicio + 8;
    }

    public long getConteudo() {

        mArquivo.setPonteiro(mInicio);

        return mArquivo.readLong();
    }
}
