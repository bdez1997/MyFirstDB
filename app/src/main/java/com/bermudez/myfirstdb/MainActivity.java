package com.bermudez.myfirstdb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtStock;
    private Spinner spinSeleccion;
    private TextView txtResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtNombre =findViewById(R.id.txtNombre);
        txtStock=findViewById(R.id.txtStock);
        spinSeleccion =findViewById(R.id.spinSeleccion);
        txtResultados=findViewById(R.id.txtResultados);

        List<String> secciones = Arrays.asList("Monitor","Memoria","Ratón","Impresora","DiscoDuro");
        spinSeleccion.setAdapter(new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,secciones));
    }

    public void mostrarMensaje(String mensaje){
        Toast.makeText(this,mensaje, Toast.LENGTH_LONG).show();
    }
    public void insertarProducto(View view){

        String strNombre=txtNombre.getText().toString();
        String strStock=txtStock.getText().toString();
        String strSeleccion=spinSeleccion.getSelectedItem().toString();

        if(strNombre.equals("")||strStock.equals("")){
            mostrarMensaje("Todos los campos son obligatorios ");
        }else{
            DB_SQLite db = new DB_SQLite(this);
            SQLiteDatabase conn = db.getWritableDatabase();
            //INSERT INTO PRODUCTO (nombre,cantidad,seccion) values ("intel",3,"Micros")

            /*String sql="INSERT INTO PRODUCTO (nombre,cantidad,seccion) values "
            +"("
            +"'"+strNombre+"'"+","+
            "'"+Integer.parseInt(strStock)+"'"+","+
            "'"+strSeleccion+"'"+")";*/

            ContentValues content = new ContentValues();
            content.put("nombre",strNombre);
            content.put("cantidad",strStock);
            content.put("seccion",strSeleccion);

            conn.insert("PRODUCTO",null,content);



            mostrarMensaje("Se ha añadidio a la base de datos");
            //conn.execSQL(sql);
            conn.close();
        }


    }
    public void eliminarProducto(View view){
        String strNombre=txtNombre.getText().toString();


        if(strNombre.equals("")){
            mostrarMensaje("Debes indicar el nombre");
        }else {
            DB_SQLite db = new DB_SQLite(this);
            SQLiteDatabase conn = db.getWritableDatabase();
            // DELETE FROM PRODUCTO WHERE nombre LIKE 'xxxxxxx'
            String condicion="nombre LIKE '" + strNombre + "'";
            int filasBorradas = conn.delete("PRODUCTO",condicion,null);

            conn.close();
            mostrarMensaje("Se han eliminado " + filasBorradas + "del producto" + strNombre);
            limpiarCuadros();
        }
    }
    @SuppressLint("Range")
    public void buscarProducto(View view){

        String strNombre=txtNombre.getText().toString();
        String strStock=txtStock.getText().toString();
        String strSeleccion=spinSeleccion.getSelectedItem().toString();

            DB_SQLite db = new DB_SQLite(this);
            SQLiteDatabase conn = db.getWritableDatabase();

            String sql = "SELECT * FROM PRODUCTO WHERE nombre LIKE '"+strNombre+"'";
            Cursor cursor = conn.rawQuery(sql, null);

            if (cursor.getCount() == 0) {
                mostrarMensaje("La tabla está vacía ");

            } else {
                String sResultado = "";
                cursor.moveToFirst();
                do {
                    long dataIdProducto = cursor.getLong(cursor.getColumnIndex("id_producto"));
                    String dataNombreProducto = cursor.getString(cursor.getColumnIndex("nombre"));
                    int dataCantidadProducto = cursor.getInt(cursor.getColumnIndex("cantidad"));
                    String dataSelectedProducto = cursor.getString(cursor.getColumnIndex("seccion"));

                    sResultado += "\n" + dataIdProducto + " " + dataNombreProducto + " " + " " + dataCantidadProducto + " " + dataSelectedProducto;

                } while (cursor.moveToNext());
                txtResultados.setText(sResultado);
            }
            conn.close();
    }
    public void ActualizarProducto(View view) {
        String strNombre = txtNombre.getText().toString();
        String strCantidad = txtStock.getText().toString();
        String strSeccion = spinSeleccion.getSelectedItem().toString();

        if(strNombre.equals("") || strCantidad.equals("")){
            mostrarMensaje("Todos los campos son obligatorios");
        }else {
            DB_SQLite db = new DB_SQLite(this);
            SQLiteDatabase conn = db.getWritableDatabase();
            // UPDATE PRODUCTO SET cantidad = xxxx, seccion='xxxxx' WHERE nombre='xxxx'

            ContentValues content = new ContentValues();
            content.put("nombre",strNombre);
            content.put("cantidad",strCantidad);
            content.put("seccion",strSeccion);

            String condicion = "nombre LIKE'" + strNombre + "'";
            conn.update("PRODUCTO",content,condicion,null);
            conn.close();
            mostrarMensaje("El producto " + strNombre + " ha sido insertado");
            limpiarCuadros();
        }
    }

    private void limpiarCuadros() {
        txtNombre.setText("");
        txtStock.setText("");
    }
    @SuppressLint("Range")
    public void listarProducto(View view){

        DB_SQLite db = new DB_SQLite(this);
        SQLiteDatabase conn = db.getWritableDatabase();

        String sqlTableName="PRODUCTO";
        String[] sqlFields = {"nombre","cantidad"};
        String sqlWhere = "";
        String sqlGroupBy="" ;
        String sqlHaving="" ;
        String sqlOrderBy ="nombre ASC";

        //String sql="SELECT * FROM PRODUCTO";
        Cursor cursor = conn.query(sqlTableName,sqlFields,sqlWhere,null,sqlGroupBy,sqlHaving,sqlOrderBy);

        if(cursor.getCount()==0){
            mostrarMensaje("La tabla está vacía ");

        }else{
            String sResultado="";
            cursor.moveToFirst();
            do{
                //long dataIdProducto = cursor.getLong(cursor.getColumnIndex("id_producto"));
                String dataNombreProducto = cursor.getString(cursor.getColumnIndex("nombre"));
                int dataCantidadProducto = cursor.getInt(cursor.getColumnIndex("cantidad"));
                //String dataSelectedProducto = cursor.getString(cursor.getColumnIndex("seccion"));

                sResultado+="\n"+" "+dataNombreProducto+" "+" "+dataCantidadProducto+" ";

            }while(cursor.moveToNext());
            txtResultados.setText(sResultado);
        }
        conn.close();
    }



}