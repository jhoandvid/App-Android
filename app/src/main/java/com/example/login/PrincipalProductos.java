package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrincipalProductos extends AppCompatActivity {

    EditText edtCodigo, edtProducto, edtPrecio, edtFabricante, id_usuario;
    Button btnAgregar;
    Button btnCerrar;
    Button btnBuscar;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_productos);

        btnCerrar=findViewById(R.id.btnCerrar);


        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edtCodigo=(EditText) findViewById(R.id.edtcodigo);
        edtProducto=(EditText) findViewById(R.id.edtProducto);
        edtPrecio=(EditText) findViewById(R.id.edtPrecio);
        edtFabricante=(EditText) findViewById(R.id.edtFabricante);
        id_usuario=(EditText) findViewById(R.id.edtUsuario);
        btnAgregar=(Button) findViewById(R.id.btnAgregar);
        btnBuscar=(Button) findViewById(R.id.btnBuscar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://172.17.80.1:41062/developeru/insertar_producto.php");
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProductos("http://172.18.3.172:41062/developeru/buscar_producto.php?codigo="+edtCodigo.getText()+"");
            }
        });

    }
    private void ejecutarServicio(String URL){
        Intent intent=getIntent();

        String usu_usuario=intent.getStringExtra(MainActivity.USU_USUARIO);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.getString("usuario", "micorreo@gmail.com");

                String usuario=preferences.getString("usuario", "micorreo@gmail.com");

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("codigo", edtCodigo.getText().toString()  );
                parametros.put("producto", edtProducto.getText().toString());
                parametros.put("precio", edtPrecio.getText().toString()  );
                parametros.put("fabricante", edtFabricante.getText().toString());
                parametros.put("id_usuario", usuario);

                return parametros;
            }
        };
         requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getProductos(String URL){

       JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {
               JSONObject jsonObject=null;
               for(int i=0; i<response.length(); i++){
                   try {

                       jsonObject=response.getJSONObject(i);
                       edtFabricante.setText(jsonObject.getString("fabricante"));
                       edtCodigo.setText(jsonObject.getString("codigo"));
                       edtPrecio.setText(jsonObject.getString("precio"));
                       edtProducto.setText(jsonObject.getString("producto"));




                   }catch (JSONException e){
                       Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                   }
               }
           }
       },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

}