package AppMottum;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import AssetContainer.Arquivo;
import AssetContainer.ArquivoImagem;
import AssetContainer.ArquivoTexto;
import AssetContainer.AssetContainer;
import AssetContainer.AssetCreator;
import Mottum.Windows;
import Mottum.Utils.Imaginador;
import Mottum.Utils.Local;
import Volume.Volume;
import Volume.VolumeUtils;

public class AppMottum {

	public static void main(String[] args) {

		String mArquivo = "asset.dkga";
		String mArquivoCompressed = "asset.dkgax";


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
		System.out.println(" Tamanho : " + mAC.getTamanho());
		System.out.println(" Arquivos : " + mAC.getArquivos().size());
		System.out.println(" Pastas : " + mAC.getPastas().size());
		System.out.println(" Arquivos Total : " + mAC.getArquivosContagem());
		System.out.println(" Pastas Total : " + mAC.getPastasContagem());
		System.out.println(" Contagem Total : " + mAC.getContagem());

		mAC.listarTabelaDeArquivos();

		ArquivoImagem ActionAll = mAC.getArquivoImagem("imc.png");

		System.out.println(" action_all Tamanho : " + ActionAll.getNome());

		// mAC.listarTabelaDeArquivos();

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
		//vl.criarArquivo("texto " + (vl.getArquivos().size() + 1) + ".txt");
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
		
	int tamanho = 1024*4;
		
		//System.out.println("Tamanho Bloco : " + tamanho);
		
		tamanho-=100;
		
		tamanho-=8; // DDC
		tamanho-=8; // DDM
		tamanho-=8; // TU
		tamanho-=8; // BLOCOS

		//System.out.println("Tamanho Sobrou : " + tamanho);

		
		//Windows mWindows = new Windows("Mottum", 1500, 1020);

		//mWindows.setIconImage(Imaginador.CarregarStream(Local.Carregar("editor.png")));

		//Alpha Alfador = new Alpha(mWindows);
		//Alfador.setImagem(ActionAll.getImagem());

		//mWindows.CriarCenarioAplicavel(Alfador);

		// Thread mThread = new Thread(mWindows);
		// mThread.start();

	}

}
