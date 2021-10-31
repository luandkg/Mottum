package Movie;

import AssetContainer.FileBinary;
import IM.BinaryUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;

public class Moviettor {


    public static void init() {

        boolean mCriar = true;

        if (mCriar) {

            System.out.println("");
            System.out.println("------------------------- MOVIETTOR --------------------------");
            System.out.println("");


            String eLocal = "/home/luan/IdeaProjects/Azzal/res/ecossistema";

            File folder = new File(eLocal);
            File[] listOfFiles = folder.listFiles();

            ArrayList<String> mArquivos = new ArrayList<String>();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    mArquivos.add(listOfFiles[i].getAbsolutePath());
                }
            }


            Collections.sort(mArquivos, new OrdenadorAlfaNum());

            //for (String eArquivo : mArquivos) {
            //      System.out.println("\t - " + eArquivo);
            //  }

            Moviettor eMoviettor = new Moviettor();

            MovieCreator eMovieCreator = eMoviettor.criar("movie.mv", 800, 801);


            for (String eArquivo : mArquivos) {
                System.out.println("\t - " + eArquivo);
                try {
                    eMovieCreator.guardarFrame(ImageIO.read(new File(eArquivo)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            eMovieCreator.fechar();

            System.out.println("");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println("");
        }

        String eAbrir = "movie.mv";

        System.out.println("");
        System.out.println("------------------------------------ MOVIETTOR - ABRIR -----------------------------------------------------");
        System.out.println("");


        System.out.println("\t - Arquivo : " + eAbrir);
        System.out.println("");

        Movie eMovie = new Movie();
        eMovie.abrir(eAbrir);


        System.out.println("\t - Largura : " + eMovie.getLargura());
        System.out.println("\t - Altura : " + eMovie.getAltura());
        System.out.println("\t - Taxa : " + eMovie.getTaxa());
        System.out.println("");

        System.out.println("\t - Quadros : " + eMovie.getQuadrosContagem());
        System.out.println("");

        for (Quadrum eQuadro : eMovie.getQuadros()) {

            System.out.println("\t\t QUADRUM " + getN8(eQuadro.getPonteiro()) + " - " + getN8(eQuadro.getAnterior()) + " - " + getN8(eQuadro.getProximo()) + "     :::     Frames = " + getN3(eQuadro.getFramesContagem()) + " Usados = " + getN3(eQuadro.getFramesUsadosContagem()) + " Livres = " + getN3(eQuadro.getFramesLivreContagem()));

            for (Frame eFrame : eQuadro.getFrames()) {

                System.out.println("\t\t\t Frame " + getN2(eFrame.getIndex()) + " " + getN8(eFrame.getInicio()) + " - " + getN8(eFrame.getFim()) + " -->> " + eFrame.getConteudo());

            }


        }

        eMovie.fechar();

        System.out.println("");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("");

    }

    public static String getN8(long e) {
        String v = String.valueOf(e);
        while (v.length() < 8) {
            v = "0" + v;
        }
        return v;
    }

    public static String getN3(long e) {
        String v = String.valueOf(e);
        while (v.length() < 3) {
            v = "0" + v;
        }
        return v;
    }

    public static String getN2(long e) {
        String v = String.valueOf(e);
        while (v.length() < 2) {
            v = "0" + v;
        }
        return v;
    }

    public MovieCreator criar(String eArquivo, int eLargura, int eAltura) {

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(new File(eArquivo), "rw");

            BinaryUtils fu = new BinaryUtils(raf);

            fu.writeByte((byte) 77);
            fu.writeByte((byte) 86);
            fu.writeByte((byte) 49);

            fu.writeByte((byte) 1);

            fu.writeLong((long) eLargura);
            fu.writeLong((long) eAltura);

            fu.writeByte((byte) 1);

            fu.writeLong((long) 200);

            fu.writeByte((byte) 1);


            return new MovieCreator(fu, eLargura, eAltura);

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

    }

}
