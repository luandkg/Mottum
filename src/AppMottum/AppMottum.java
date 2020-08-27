package AppMottum;


import IM.VisualizadorAN;
import AssetContainer.ArquivoImagem;
import AssetContainer.ArquivoTexto;
import AssetContainer.AssetContainer;
import AssetContainer.AssetCreator;
import AssetContainer.Listador;
import AssetContainer.Biblioteca;
import AssetContainer.Arquivo;
import AssetContainer.Pasta;
import AssetContainer.PastaLink;
import AssetContainer.ArquivoLink;

import IM.VisualizadorIM;
import LuanDKG.LuanDKG;
import LuanDKG.Matriz;

import Mottum.Utils.Imaginador;
import Mottum.Utils.Local;
import Mottum.Windows;
import Volume.Volume;

public class AppMottum {

    // CENTRAL DE DESENVOLVIMENTO DE ALGORITMOS - LUAN FREITAS

    public static void main(String[] args) {



        // aAssetContainer();

        aDKG();

        //aVolume();

        //aGE();

        // aIM();

        //aAN();


    }

    public static void aDKG() {


        LuanDKG arq = new LuanDKG();

        arq.Abrir("luan.dkg");


        arq.UnicoPacote("Bloco").Identifique("Nome","Bloco Vermelho");
        arq.UnicoPacote("Bloco").Identifique("Peso",3.60);

        arq.UnicoPacote("Bloco").UnicaLista("Tags").UnicoItem("LUZ");
        arq.UnicoPacote("Bloco").UnicaLista("Tags").UnicoItem("AGUA");

        arq.UnicoPacote("Bloco").UnicoVetor("Posicao").limpar();
        arq.UnicoPacote("Bloco").UnicoVetor("Posicao").adicionar("A","B");



        Matriz m= arq.UnicoPacote("Bloco").UnicaMatriz("Matrix");
        m.limpar();
        m.adicionar("0","1","2");
        m.adicionar("4","3","2");
        m.adicionar("3","6","9");

        arq.Salvar("luan.dkg");


    }

    public static void aGE() {

        Windows mWindows = new Windows("Mottum", 1500, 1020);

        mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

        Alpha Alfador = new Alpha(mWindows);

        mWindows.CriarCenarioAplicavel(Alfador);

        Thread mThread = new Thread(mWindows);
        mThread.start();

    }

    public static void aIM() {

        Windows mWindows = new Windows("Mottum", 1500, 1020);

        mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

        VisualizadorIM VIM = new VisualizadorIM(mWindows);

        mWindows.CriarCenarioAplicavel(VIM);

        Thread mThread = new Thread(mWindows);
        mThread.start();

    }


    public static void aAN() {

        Windows mWindows = new Windows("Mottum", 1500, 1020);

        mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

        VisualizadorAN VAN = new VisualizadorAN(mWindows);

        mWindows.CriarCenarioAplicavel(VAN);

        Thread mThread = new Thread(mWindows);
        mThread.start();

    }

    public static void aAssetContainer() {

        String mArquivo = "asset.dkga";
        String mArquivoCompressed = "asset.dkgax";

        String mLocal = "/home/luan/Downloads/asset";

        AssetContainer mAC = new AssetContainer();
        AssetCreator mACreator = new AssetCreator();


        // mACreator.criar(mArquivo, mLocal);
        // mACreator.criarCompressed(mArquivoCompressed, mLocal);

        System.out.println("\n\n ------------------- ABRIR ------------------------ \n\n");

        mAC.abrir(mArquivo);

        System.out.println(" Cabecalho : " + mAC.getCabecalho());
        System.out.println(" Versao : " + mAC.getVersao());
        System.out.println(" Criado : " + mAC.getCriado());
        System.out.println(" Finalizado : " + mAC.getFinalizado());
        System.out.println(" Tem Extrum : " + mAC.temApendice());

        if (mAC.temApendice()) {
            System.out.println(" Extrum Local : " + mAC.getExtrumPonteiro());
        }

        System.out.println(" Tamanho : " + mAC.getTamanho());
        System.out.println(" Arquivos : " + mAC.getArquivos().size());
        System.out.println(" Pastas : " + mAC.getPastas().size());
        System.out.println(" Arquivos Total : " + mAC.getArquivosContagem());
        System.out.println(" Pastas Total : " + mAC.getPastasContagem());
        System.out.println(" Contagem Total : " + mAC.getContagem());

        System.out.println(" -------------- TABELA DE ARQUIVOS --------------- ");

        mAC.listarTabelaDeArquivos();

        System.out.println(" -------------- TABELA DE PASTAS --------------- ");
        mAC.listarTabelaDePastas();

        System.out.println(" -------------- TABELA DE ARQUIVOS LINK --------------- ");

        mAC.listarTabelaDeArquivosLink();


        System.out.println(" -------------- TABELA DE LISTADORES --------------- ");
        mAC.listarTabelaDeListadores();

        System.out.println(" -------------- TABELA DE BIBLIOTECAS --------------- ");

        mAC.listarTabelaDeBibliotecas();


        ArquivoImagem imc = mAC.getArquivoImagem("imc.png");

        System.out.println(" imc Tamanho : " + imc.getTamanho());
        System.out.println(" imc Largura : " + imc.getLargura());
        System.out.println(" imc Altura : " + imc.getAltura());

        ArquivoTexto fis = mAC.getArquivoTexto("fis.txt");

        System.out.println(" Fis Indexado : " + fis.getIndexado());
        System.out.println(" Fis Linhas : " + fis.getLinhas().size());
        System.out.println(" Fis Indexado : " + fis.getIndexado());

        for (Listador aL : mAC.getListadores()) {

            for (Arquivo aa : aL.getArquivos()) {

                System.out.println("Arquivo do " + aL.getNome() + " :: -->> " + aa.getNome());

            }


        }

        for (ArquivoLink aL : mAC.getArquivosLink()) {

            System.out.println("Arquivo Link do " + aL.getNome() + " :: -->> " + aL.getArquivo().getNome());

        }

        for (PastaLink aL : mAC.getPastasLink()) {

            System.out.println("Pasta Link do " + aL.getNome() + " :: -->> " + aL.getPasta().getNome());

        }

        int i = 0;

        if (i == 0) {

            mAC.limparExtrum();

            Pasta eRun = mAC.getPasta("Gaia").getPasta("Run");
            Pasta eTexto = mAC.getPasta("texto");

            Listador mRunner = mAC.criarListador("Runner");
            mRunner.adicionar(eRun);
            mRunner.adicionar(eTexto);

            Biblioteca mGeral = mAC.criarBiblioteca("Geral");
            mGeral.adicionarExtensao(".odg");

            Arquivo gcc = mAC.getPasta("Gaia").getPasta("Compiler").getArquivo("Compiler.java");

            mAC.criarLinkArquivo("FIS.txt", mAC.getArquivo("fis.txt"));
            mAC.criarLinkArquivo("COMPILER.java", gcc);

            mAC.criarLinkPasta("AGaia", mAC.getPasta("Gaia"));

            mAC.salvarExtrum();

        }


        //Windows mWindows = new Windows("AC", 1500, 1020);

        // mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

        //VisualizadorAC Alfador = new VisualizadorAC(mWindows);

        // mWindows.CriarCenarioAplicavel(Alfador);

        // Thread mThread = new Thread(mWindows);
        // mThread.start();

    }


    public static void aVolume() {


        System.out.println("");

        System.out.println("\n\n ------------------- VOLUME ------------------------ \n\n");

        String eVolumeLocal = "volume.vl";

        // VolumeUtils.alocar(eVolumeLocal, 5*1024*1024);

        // VolumeUtils.zerar(eVolumeLocal);

        //VolumeUtils.formatar(eVolumeLocal);

        Volume vl = new Volume(eVolumeLocal);

        System.out.println("\t Volume Cabecalho : " + vl.getCabecalho());

        System.out.println("\t Clusters Inicio : " + vl.getClusters_Inicio());
        System.out.println("\t Clusters Fim : " + vl.getClusters_Fim());
        System.out.println("\t Clusters Quantidade : " + vl.getClusters_Quantidade());
        System.out.println("\t Inicializador : " + vl.getInicializador());

        //	vl.mostrar_mapaSeccionado(0);
        vl.mostrar_mapa();

        for (int t = 0; t < 10; t++) {
            vl.criarArquivo("texto " + (vl.getArquivos().size() + 1) + ".txt");
        }


        //System.out.println("EXPANDIR MUITO...");

        //for (int t = 0; t < 500; t++) {
        //	vl.expandirmuito();
        //}

        System.out.println("LISTAR ARQUIVOS");

        vl.mostrar_arquivos();

        vl.teste();


        System.out.println("\t Quantidade : " + vl.getClusters_Quantidade());
        System.out.println("\t Ocupados : " + vl.getClusters_Ocupados());
        System.out.println("\t Livres : " + vl.getClusters_Livres());

        vl.fechar();

        //System.out.println("Pensando ....");

        int tamanho = 1024 * 4;

        //System.out.println("Tamanho Bloco : " + tamanho);

        tamanho -= 100;

        tamanho -= 8; // DDC
        tamanho -= 8; // DDM
        tamanho -= 8; // TU
        tamanho -= 8; // BLOCOS

        //System.out.println("Tamanho Sobrou : " + tamanho);

    }

}
