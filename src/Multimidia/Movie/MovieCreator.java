package Movie;

import IM.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class MovieCreator {

    private BinaryUtils mArquivo;
    private Quadrum mQuadrumCorrente;
    private boolean mExisteQuadroAnterior;
    private Utils mUtils;
    private SetoresEscrever mSetoresEscrever;

    private int mLargura;
    private int mAltura;
    private int mBlocosN;
    private int mGamas;

    private int eGuardandoFrame;

    public MovieCreator(BinaryUtils eArquivo, int eLargura, int eAltura) {

        mArquivo = eArquivo;
        mLargura = eLargura;
        mAltura = eAltura;

        mExisteQuadroAnterior = false;
        mUtils = new Utils();
        mSetoresEscrever = new SetoresEscrever();


        mQuadrumCorrente = criarQuadroVazio();


        mBlocosN = mUtils.getBlocosNum(mLargura, mAltura);
        mGamas = mUtils.getGamasNum(mLargura, mAltura);
        eGuardandoFrame = 0;


        // for (int i = 0; i < 3; i++) {
        //     guardarFrame(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB));
        // }


    }

    private Quadrum criarQuadroVazio() {


        long ePonteiro = 0;


        if (mExisteQuadroAnterior) {

            mArquivo.setPonteiro(mArquivo.getLength());
            ePonteiro = mArquivo.getPonteiro();

            mArquivo.writeByte((byte) 30);

            mArquivo.writeLong(mQuadrumCorrente.getPonteiro());
        } else {

            ePonteiro = mArquivo.getPonteiro();

            mArquivo.writeByte((byte) 30);

            mArquivo.writeLong(0);
        }

        mArquivo.writeLong((byte) 0);
        mArquivo.writeByte((byte) 31);


        for (int i = 0; i < 100; i++) {
            mArquivo.writeLong((byte) 0);
        }

        mArquivo.writeByte((byte) 32);

        if (mExisteQuadroAnterior) {

            mArquivo.setPonteiro(mQuadrumCorrente.getPonteiro());
            mArquivo.readByte();
            mArquivo.readLong();
            mArquivo.writeLong(ePonteiro);

        } else {
            mExisteQuadroAnterior = true;
        }

        return new Quadrum(mArquivo, ePonteiro);
    }

    public void guardarFrame(BufferedImage eFrame) {

        //  System.out.println("Guardando Frame " + eGuardandoFrame);
        eGuardandoFrame += 1;

        if (mQuadrumCorrente.getFramesLivreContagem() == 0) {
            mQuadrumCorrente = criarQuadroVazio();
            //     System.out.println("Alocar novo Quadro");
        }


        mArquivo.setPonteiro(mArquivo.getLength());

        long ePonteiro = mArquivo.getPonteiro();

        // System.out.println("Guardando Frame em " + ePonteiro);

        escreverQuadro(ePonteiro,eFrame);


        mQuadrumCorrente.guardar(ePonteiro);

    }

    private void escreverQuadro(long eLugar, BufferedImage imagem) {

        System.out.println("Escrever quadro em " + mArquivo.getPonteiro());

        mArquivo.setPonteiro(eLugar);

        mArquivo.writeByte((byte) 50);

        for (int mBlocoID = 0; mBlocoID < mBlocosN; mBlocoID++) {


            Quad movendoBloco = mUtils.getBloco(mLargura, mAltura, mBlocoID);

            int mModo = mUtils.getModo(movendoBloco.getLargura(), movendoBloco.getAltura());


            BlocoIM mBlocoIM = mSetoresEscrever.mapearBloco(imagem, movendoBloco);


            int mTipo = mBlocoIM.getTipo();

           System.out.println("Bloco Tipo = " + mTipo);

            if (mBlocoIM.getTipo() == 1) {

                mSetoresEscrever.escreverBlocoUm(mArquivo, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else if (mBlocoIM.getTipo() == 10) {

                mSetoresEscrever.escreverBlocoComPaleta(mArquivo, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            } else {

                mSetoresEscrever.escreverBlocoNormal(mArquivo, mBlocoID, movendoBloco, mBlocoIM, mTipo, mModo);

            }

        }

        for (int mGamaID = 0; mGamaID < mGamas; mGamaID++) {

            Quad movendoGama = mUtils.getGama(mLargura, mAltura, mGamaID);
            int mModo = mUtils.getAlphaModo(movendoGama.getLargura(), movendoGama.getAltura());

            BlocoIM mBlocoIM = mSetoresEscrever.mapearGama(imagem, movendoGama);

            int mTipo = mBlocoIM.getTipoAlpha();

            if (mTipo == 1) {
                mSetoresEscrever.escreverGamaUm(mArquivo, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);
            } else {
                mSetoresEscrever.escreverGamaNormal(mArquivo, mGamaID, movendoGama, mBlocoIM, mTipo, mModo);
            }

        }

        mArquivo.writeByte((byte) 51);

    }

    public void fechar() {

        try {
            mArquivo.fechar();
        } catch (IOException e) {
        }


    }
}
