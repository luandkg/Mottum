package AppMottum;


import AssetContainer.ArquivoImagem;
import AssetContainer.ArquivoTexto;
import AssetContainer.AssetContainer;
import AssetContainer.AssetCreator;
import AssetContainer.AssetExtrum;
import AssetContainer.Listador;
import AssetContainer.Arquivo;
import AssetContainer.Biblioteca;

import Volume.Volume;

public class AppMottum {

    public static void main(String[] args) {


        aAssetContainer();
        //aVolume();


        //Windows mWindows = new Windows("Mottum", 1500, 1020);

        //mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

        //Alpha Alfador = new Alpha(mWindows);
        //Alfador.setImagem(ActionAll.getImagem());

        //mWindows.CriarCenarioAplicavel(Alfador);

        // Thread mThread = new Thread(mWindows);
        // mThread.start();

    }

    public static void aAssetContainer() {

        String mArquivo = "asset.dkga";
        String mArquivoCompressed = "asset.dkgax";

        String mLocal = "/home/luan/Downloads/asset";

        AssetContainer mAC = new AssetContainer();
        AssetCreator mACreator = new AssetCreator();


        //mACreator.criar(mArquivo, mLocal);
        // mACreator.criarCompressed(mArquivoCompressed, mLocal);

        System.out.println("\n\n ------------------- ABRIR ------------------------ \n\n");

        mAC.abrir(mArquivo);

        System.out.println(" Cabecalho : " + mAC.getCabecalho());
        System.out.println(" Versao : " + mAC.getVersao());
        System.out.println(" Criado : " + mAC.getCriado());
        System.out.println(" Finalizado : " + mAC.getFinalizado());
        System.out.println(" Tem Apendice : " + mAC.temApendice());

        if (mAC.temApendice()) {
            System.out.println(" Apendice Local : " + mAC.getApendiceLocal());
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

        ArquivoImagem imc = mAC.getArquivoImagem("imc.png");

        System.out.println(" imc Tamanho : " + imc.getTamanho());
        System.out.println(" imc Largura : " + imc.getLargura());
        System.out.println(" imc Altura : " + imc.getAltura());

        ArquivoTexto fis = mAC.getArquivoTexto("fis.txt");

        System.out.println(" Fis Indexado : " + fis.getIndexado());
        System.out.println(" Fis Linhas : " + fis.getLinhas().size());
        System.out.println(" Fis Indexado : " + fis.getIndexado());




        mAC.limparExtrum();

        Listador mRunner = mAC.criarListador("Runner");
        mRunner.adicionar("Gaia\\Run");
        mRunner.adicionar("texto");

        Biblioteca mGeral = mAC.criarBiblioteca("Geral");
        mGeral.adicionar("\\");
        mGeral.adicionarExtensao(".odg");
        // mAC.listarTabelaDeArquivos();

        mAC.salvarExtrum();


        for (Listador eListador : mAC.getListadores()) {

            System.out.println(" -->> Listador : " + eListador.getNome());

            for (String eLocal : eListador.getLocais()) {
                System.out.println("       - " + eLocal + " -->> " + mAC.existePastaCaminho(eLocal));
            }

            for(Arquivo eArquivo : eListador.getArquivos()){
                System.out.println("      ARQUIVO :: " + eArquivo.getNome());
            }

        }

        for (Biblioteca eListador : mAC.getBibliotecas()) {

            System.out.println(" -->> Biblioteca : " + eListador.getNome());

            for (String eLocal : eListador.getLocais()) {
                System.out.println("       - LOCAL :  " + eLocal + " -->> " + mAC.existePastaCaminho(eLocal));
            }
            for (String eLocal : eListador.getExtensoes()) {
                System.out.println("       - EXT : " + eLocal );
            }

            for(Arquivo eArquivo : eListador.getArquivos()){
                System.out.println("      ARQUIVO :: " + eArquivo.getNome());
            }

        }



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
