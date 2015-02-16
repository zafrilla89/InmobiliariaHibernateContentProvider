package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;


public class Hacerfotos extends Activity {

    private int IDACTIVIDADFOTO=1;
    private long idinmueble;
    private Adaptador ad;
    private ArrayList<Bitmap> lis;
    private ListView lv;

    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/

    public void onActivityResult(int pet, int res, Intent i) {
        if (res == RESULT_OK && pet == IDACTIVIDADFOTO) {
            Bitmap foto = (Bitmap)i.getExtras().get("data");
            String id=idinmueble+"_";
            Date horaActual=new Date();
            String hora=(horaActual.getYear()+1900)+"_"+(horaActual.getMonth()+1)+"_"+horaActual.getDate()+
                    "_"+horaActual.getHours()+"_"+horaActual.getMinutes()+"_"+horaActual.getSeconds();
            FileOutputStream salida;
            try {
                salida = new FileOutputStream(getExternalFilesDir(null) +"/inmobiliaria_"+id+hora+".jpg");
                foto.compress(Bitmap.CompressFormat.JPEG, 90, salida);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            lis=new ArrayList<Bitmap>();
            lis=cargarfotos(lis);
            ad = new Adaptador(Hacerfotos.this,R.layout.itemfoto,lis);
            lv= (ListView)findViewById(R.id.lvfotos);
            lv.setAdapter(ad);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hacerfotos);
        Bundle b = getIntent().getExtras();
        if(b !=null ) {
            idinmueble = b.getLong("id");
        }
        lis=new ArrayList<Bitmap>();
        lis=cargarfotos(lis);
        ad = new Adaptador(Hacerfotos.this,R.layout.itemfoto,lis);
        lv= (ListView)findViewById(R.id.lvfotos);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int num, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Hacerfotos.this);
                alertDialog.setMessage("¿Estas seguro que desea eliminar la foto?");
                alertDialog.setTitle("Eliminar foto");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        borrarfoto(num);
                        lis.remove(num);
                        ad.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
                alertDialog.show();            }
        });
        lv.setAdapter(ad);
     }

    /***********************************************************************/
    /*                                                                     */
    /*                        METODOS AUXILIARES                           */
    /*                                                                     */
    /***********************************************************************/

    public void borrarfoto(int i){
        int c=0;
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        String id=""+idinmueble;
        String[] listaDirectorio = archivo.list();
        for (int x=0;x<listaDirectorio.length;x++){
            String archi=listaDirectorio[x];
            if (archi.indexOf("inmobiliaria_"+id) != -1){
                if (c==i) {
                   File f = new File(getExternalFilesDir(null), archi);
                   f.delete();
                }
                c++;
            }
        }
    }

    public ArrayList<Bitmap> cargarfotos(ArrayList<Bitmap> lis){
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        String id=""+idinmueble;
        String[] listaDirectorio = archivo.list();
        Bitmap myBitmap;
        for (int x=0;x<listaDirectorio.length;x++){
            String archi=listaDirectorio[x];
            if (archi.indexOf("inmobiliaria_"+id) != -1){
                myBitmap = BitmapFactory.decodeFile(archivo.getAbsolutePath() + "/" + archi);
                lis.add(myBitmap);
            }
        }
        return lis;
    }

    /***********************************************************************/
    /*                                                                     */
    /*                           METODOS ONCLICK                           */
    /*                                                                     */
    /***********************************************************************/

    public void fotos(View view){
        Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, IDACTIVIDADFOTO);

    }

    public void guardar(View v){
        Intent i =new Intent(this,Principal.class);
        startActivity(i);
        finish();
    }




}
