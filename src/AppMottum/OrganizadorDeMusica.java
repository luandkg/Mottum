package AppMottum;

import java.io.File;

public class Y2Mate {

    public void organizar(String eLocal) {


        System.out.println("---------- ORGANIZADOR DE MUSICAS -------------------");
        System.out.println();

        File file = new File(eLocal);
        File afile[] = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivo = afile[i];

            String eNome = arquivo.getName();
            if (eNome.startsWith("y2mate.com - ") && eNome.endsWith(".mp3")) {

                String mRetirar = "";

                int o = eNome.length()-1;
                while (o >= 0) {
                    String l = String.valueOf(eNome.charAt(o));

                    mRetirar=l + mRetirar;

                    if (l.contentEquals("_")){
                        break;
                    }
                    o -= 1;
                }

                eNome=eNome.replace("y2mate.com - ","");
                eNome=eNome.replace(mRetirar,"");
                eNome=eNome.replace("_"," ");

                eNome=eNome + ".mp3";

                File file2 = new File(eLocal + "/" + eNome);
                arquivo.renameTo(file2);

                System.out.println("\t - " + eNome );
            }
        }


    }
}
