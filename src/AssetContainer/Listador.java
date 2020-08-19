package AssetContainer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Listador {

    private String mNome;
    private ArrayList<String> mLocais;
    private AssetContainer mAssetContainer;
    private ArrayList<Arquivo> mArquivos;
    private boolean mIndexado;

    public Listador(AssetContainer eAssetContainer, String eNome) {

        mNome = eNome;
        mLocais = new ArrayList<String>();
        mAssetContainer = eAssetContainer;
        mArquivos = new ArrayList<Arquivo>();
        mIndexado = false;
    }


    public String getNome() {
        return mNome;
    }


    public void adicionar(String eLocal) {
        if (!mLocais.contains(eLocal)) {
            mLocais.add(eLocal);
        }
    }

    public ArrayList<String> getLocais() {
        return mLocais;
    }

    public ArrayList<Arquivo> getArquivos() {

        if (!mIndexado) {
            mIndexado = true;
            abrir();
        }

        return mArquivos;
    }

    private void abrir() {

        mArquivos.clear();
        for (String eLocal : getLocais()) {

            if (eLocal.contentEquals("\\")) {

                for (Arquivo eArquivo : mAssetContainer.getArquivos()) {
                    mArquivos.add(eArquivo);
                }
                for (Pasta ePasta : mAssetContainer.getPastas()) {
                    abrirLocal(ePasta);
                }

            } else   if (eLocal.contentEquals("/")){

                for(Arquivo eArquivo : mAssetContainer.getArquivos()){
                    mArquivos.add(eArquivo);
                }
                for (Pasta ePasta : mAssetContainer.getPastas()) {
                    abrirLocal(ePasta);
                }
            }else{
                Pasta ePasta = mAssetContainer.getPastaCaminho(eLocal);

                abrirLocal(ePasta);
            }


        }
    }

    private void abrirLocal(Pasta ePasta){

        try {

            RandomAccessFile raf = new RandomAccessFile(new File(mAssetContainer.getArquivo()), "rw");

            FileBinary fu = new FileBinary(raf);

            fu.setPonteiro(ePasta.getInicio());

            int v = 0;

            while (v != 13) {
                v = (int) fu.readByte();

                if (v == 11) {

                    String s1 = fu.readString();
                    long l2 = fu.readLong();
                    long l3 = fu.readLong();

                    abrirLocal(new Pasta(mAssetContainer, new Ponto(s1, 11, l2, l3)));


                } else if (v == 12) {

                    String s1 = fu.readString();
                    long l2 = fu.readLong();
                    long l3 = fu.readLong();

                    mArquivos.add(new Arquivo(mAssetContainer, new Ponto(s1, 12, l2, l3)));

                }
            }

            raf.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
