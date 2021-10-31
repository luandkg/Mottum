package Movie;

import IM.BinaryUtils;

import java.util.ArrayList;

public class Quadrum {

    private BinaryUtils mArquivo;
    private long mLocal;

    public Quadrum(BinaryUtils eArquivo, long eLocal) {

        mArquivo = eArquivo;
        mLocal = eLocal;

    }

    public long getPonteiro() {
        return mLocal;
    }

    public long getAnterior() {
        mArquivo.setPonteiro(mLocal + 1);
        return mArquivo.readLong();
    }

    public long getProximo() {
        mArquivo.setPonteiro(mLocal + 9);
        return mArquivo.readLong();
    }

    public ArrayList<Frame> getFrames() {

        ArrayList<Frame> mFrames = new ArrayList<Frame>();

        int eContador = 0;

        mArquivo.setPonteiro(mLocal + 18);

        for (int i = 0; i < 100; i++) {

            long eAntes = mArquivo.getPonteiro();
            mFrames.add(new Frame(mArquivo,i, eAntes));
            mArquivo.setPonteiro(eAntes);

            long eFramePonteiro = mArquivo.readLong();
            eContador += 1;

        }

        return mFrames;

    }


    public int getFramesContagem() {

        int eContador = 0;

        mArquivo.setPonteiro(mLocal + 18);

        for (int i = 0; i < 100; i++) {

            long eFramePonteiro = mArquivo.readLong();
            eContador += 1;


        }

        return eContador;


    }

    public int getFramesUsadosContagem() {

        int eContador = 0;

        mArquivo.setPonteiro(mLocal + 18);

        for (int i = 0; i < 100; i++) {

            long eFramePonteiro = mArquivo.readLong();
            if (eFramePonteiro != 0) {
                eContador += 1;
            }

        }

        return eContador;


    }

    public int getFramesLivreContagem() {

        int eContador = 0;

        mArquivo.setPonteiro(mLocal + 18);

        for (int i = 0; i < 100; i++) {

            long eFramePonteiro = mArquivo.readLong();
            if (eFramePonteiro == 0) {
                eContador += 1;
            }

        }

        return eContador;


    }


    public void guardar(long e) {

        mArquivo.setPonteiro(mLocal + 18);

        boolean guardou = false;

        for (int i = 0; i < 100; i++) {

            long ePonteiroAntes = mArquivo.getPonteiro();

          //  System.out.println("Guardar na posicao : " + ePonteiroAntes);

            long eFramePonteiro = mArquivo.readLong();

            if (eFramePonteiro == 0) {

                mArquivo.setPonteiro(ePonteiroAntes);
                mArquivo.writeLong(e);
                guardou = true;
                break;
            }

        }

        if (!guardou) {
            System.out.println("Falta de pagina no quadro detectada !");
        }


    }
}
