package AssetContainer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class AssetExtrum {

    private AssetContainer mAssetContainer;
    private boolean mIndexado;

    private ArrayList<Listador> mListadores;
    private ArrayList<Biblioteca> mBibliotecas;

    public AssetExtrum(AssetContainer eAssetContainer) {

        mAssetContainer = eAssetContainer;
        mIndexado = false;

        mListadores = new ArrayList<Listador>();
        mBibliotecas = new ArrayList<Biblioteca>();

    }

    public void ler() {

        if (!mIndexado) {
            mIndexado = true;

            mListadores.clear();

            System.out.println("Iniciar Leitura Extrum");

            try {

                RandomAccessFile raf = new RandomAccessFile(new File(mAssetContainer.getArquivo()), "rw");

                FileBinary fu = new FileBinary(raf);

                if (mAssetContainer.temApendice()) {

                    fu.setPonteiro(mAssetContainer.getApendiceLocal());


                    byte b1 = fu.readByte();
                    b1 = fu.readByte();

                    while (b1 != (byte) 55) {

                        System.out.println("B1 : " + b1);


                        if (b1 == (byte) 51) {

                            Listador eListador = new Listador(mAssetContainer, fu.readString());

                            System.out.println("Dentro Listador : " + eListador.getNome());

                            b1 = fu.readByte();

                            // System.out.println("Dentro Listador : " + b1);

                            while (b1 != (byte) 13) {

                                if (b1 == (byte) 11) {
                                    String eLocal = fu.readStringGrande();
                                    eListador.adicionar(eLocal);

                                    System.out.println("\t - Local " + eLocal);
                                }
                                b1 = fu.readByte();
                                if (b1 == (byte) -1) {
                                    break;
                                }
                            }


                            mListadores.add(eListador);

                        } else if (b1 == (byte) 52) {

                            Biblioteca eListador = new Biblioteca(mAssetContainer, fu.readString());

                            System.out.println("Dentro Biblioteca : " + eListador.getNome());

                            b1 = fu.readByte();

                            // System.out.println("Dentro Biblioteca : " + b1);

                            while (b1 != (byte) 13) {

                                if (b1 == (byte) 11) {
                                    String eLocal = fu.readStringGrande();
                                    eListador.adicionar(eLocal);

                                    System.out.println("\t - Local " + eLocal);
                                }
                                b1 = fu.readByte();
                                if (b1 == (byte) -1) {
                                    break;
                                }
                            }

                            b1 = fu.readByte();

                            // System.out.println("Dentro Biblioteca : " + b1);

                            while (b1 != (byte) 13) {

                                if (b1 == (byte) 11) {
                                    String eLocal = fu.readString();
                                    eListador.adicionarExtensao(eLocal);

                                    System.out.println("\t - Extensao " + eLocal);
                                }
                                b1 = fu.readByte();
                                if (b1 == (byte) -1) {
                                    break;
                                }
                            }

                            mBibliotecas.add(eListador);
                        }

                        b1 = fu.readByte();
                        if (b1 == (byte) -1) {
                            break;
                        }
                    }


                }
                raf.close();

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    public void limpar() {

        ler();

        mListadores.clear();
        mBibliotecas.clear();

    }

    public Listador criarListador(String eNome) {

        ler();

        if (!existe(eNome)) {
            Listador mListadorC = new Listador(mAssetContainer, eNome);

            mListadores.add(mListadorC);

            return mListadorC;
        } else {
            throw new IllegalArgumentException("Ja existe um recurso com esse nome : " + eNome);
        }

    }

    public Biblioteca criarBiblioteca(String eNome) {

        ler();

        if (!existe(eNome)) {
            Biblioteca mBiblioteca = new Biblioteca(mAssetContainer, eNome);

            mBibliotecas.add(mBiblioteca);

            return mBiblioteca;
        } else {
            throw new IllegalArgumentException("Ja existe um recurso com esse nome : " + eNome);
        }

    }

    public ArrayList<Listador> getListadores() {
        ler();
        return mListadores;
    }

    public ArrayList<Biblioteca> getBibliotecas() {
        ler();
        return mBibliotecas;
    }

    public boolean existe(String eNome) {

        ler();

        boolean ret = false;

        for (Listador mListadorC : getListadores()) {
            if (mListadorC.getNome().contentEquals(eNome)) {
                ret = true;
                break;
            }
        }

        for (Biblioteca mListadorC : getBibliotecas()) {
            if (mListadorC.getNome().contentEquals(eNome)) {
                ret = true;
                break;
            }
        }

        return ret;

    }


    public void salvar() {



        try {

            RandomAccessFile raf = new RandomAccessFile(new File(mAssetContainer.getArquivo()), "rw");

            FileBinary fu = new FileBinary(raf);

            if (mAssetContainer.temApendice()) {

                fu.setPonteiro(mAssetContainer.getApendiceLocal());

                fu.writeByte((byte) 50);


                for (Listador mListadorC : getListadores()) {

                    fu.writeByte((byte) 51);
                    fu.writeString(mListadorC.getNome());

                    fu.writeByte((byte) 10);

                    for (String eLocal : mListadorC.getLocais()) {
                        fu.writeByte((byte) 11);
                        fu.writeGrandeString(eLocal);
                    }

                    fu.writeByte((byte) 13);

                }

                for (Biblioteca mListadorC : getBibliotecas()) {

                    //System.out.println("Criando Bib " + mListadorC.getNome() + " : " + mListadorC.getLocais().size() + " : " + mListadorC.getExtensoes().size());

                    fu.writeByte((byte) 52);
                    fu.writeString(mListadorC.getNome());

                    fu.writeByte((byte) 10);

                    for (String eLocal : mListadorC.getLocais()) {
                        fu.writeByte((byte) 11);
                        fu.writeGrandeString(eLocal);
                    }

                    fu.writeByte((byte) 13);

                    for (String eLocal : mListadorC.getExtensoes()) {
                        fu.writeByte((byte) 11);
                        fu.writeString(eLocal);
                        // System.out.println("Salvando ext : " + eLocal);
                    }

                    fu.writeByte((byte) 13);

                }

                fu.writeByte((byte) 55);


            }

            raf.close();

        } catch (IOException e) {

            e.printStackTrace();
        }


    }


}
