package AssetContainer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

public class Arquivo {

	private Ponto mPonto;
	private AssetContainer mAssetContainer;

	public Arquivo(AssetContainer eAssetContainer, Ponto ePonto) {

		mAssetContainer = eAssetContainer;
		mPonto = ePonto;

	}

	public String getNome() {
		return mPonto.getNome();
	}

	public long getTipo() {
		return mPonto.getTipo();
	}

	public long getInicio() {
		return mPonto.getInicio();
	}

	public long getFim() {
		return mPonto.getFim();
	}

	public long getTamanho() {return getFim()-getInicio();}
	
	public void exportar(String eLocal) {
		try {

			
			RandomAccessFile mLendo = new RandomAccessFile(new File(mAssetContainer.getArquivo()), "r");
			mLendo.seek(getInicio());

			
			RandomAccessFile mExportando = new RandomAccessFile(new File(eLocal), "rw");

			long mAquivandoIndex = 0;
			long mAquivandoTamanho = this.getTamanho();
			
			mExportando.seek(0);
		
			while(mAquivandoIndex<mAquivandoTamanho) {
				
				mExportando.writeByte(mLendo.readByte());
				
				mAquivandoIndex+=1;
			}
			
			mExportando.close();
			mLendo.close();

		} catch (IOException e) {

		}
	
		
	}

	public byte[] getBytes( ) {
		
		byte[] mBytes = new byte[(int) this.getTamanho()];
		
		try {

			
			RandomAccessFile mLendo = new RandomAccessFile(new File(mAssetContainer.getArquivo()), "r");
			mLendo.seek(getInicio());

			
	
			long mAquivandoIndex = 0;
			long mAquivandoTamanho = this.getTamanho();
			
		
			while(mAquivandoIndex<mAquivandoTamanho) {
				
				mBytes[(int) mAquivandoIndex] = mLendo.readByte();
				
				mAquivandoIndex+=1;
			}
			
			mLendo.close();

		} catch (IOException e) {

		}
	
		if (mAssetContainer.getCabecalho().contentEquals(AssetCreator.ASSET_CONTAINER_COMPRESSED)) {
			
			 try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mBytes)) {
			   
				 GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
			       
				 mBytes=gzipInputStream.readAllBytes();
			      
			    } catch(IOException e) {
			      throw new RuntimeException("Failed to unzip content", e);
			    }
			
		}
			
		
		
		return mBytes;
	}
	
}
