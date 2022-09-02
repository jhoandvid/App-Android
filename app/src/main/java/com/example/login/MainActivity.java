package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
public static final String USU_USUARIO="usuario";

    EditText edtUsuario, edtPassword;
    Button btnLogin;
    String usuario="", password="";

    String Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsuario=findViewById(R.id.edtUsuario);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);


        recuperarPreferencias();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario=edtUsuario.getText().toString();
                password=edtPassword.getText().toString();

                if(!usuario.isEmpty() && !usuario.isEmpty()){
                    validarUsuario("http://172.17.80.1:41062/developeru/validar_usuario.php");



                }else{
                    Toast.makeText(MainActivity.this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public static String fecha(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        return  sdf.format(date);
    }

    private void validarUsuario(String URL){

        //Colombia time zone
        //TimeZone myTimezone=TimeZone.getTimeZone("GMT-5");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //simpleDateFormat.setTimeZone(myTimezone);
        //String fDate=simpleDateFormat.format(new Date());
        //Log.d("Fecha", fDate);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));




        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               guardarPreferencias();
                if(!response.isEmpty()){
                   // Intent intent=new Intent(getApplicationContext(), Principal.class);
                    Intent intent=new Intent(getApplicationContext(), PrincipalProductos.class);
                    intent.putExtra(USU_USUARIO,usuario);


                    startActivity(intent);
                    Log.d("Respuesta", sdf.format(date));
                    finish();

                }else{
                    Toast.makeText(MainActivity.this, "usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
               // parametros.put("usuario", edtUsuario.getText().toString());
               // parametros.put("password", edtPassword.getText().toString());
                 parametros.put("usuario", usuario);
                 parametros.put("password", password);
                 parametros.put("fecha_actualizada",fecha());







                return parametros;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        editor.putString("usuario",usuario);
        editor.putString("password",password);
        editor.putBoolean("sesion",true);

        editor.commit();



    }

    private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edtUsuario.setText(preferences.getString("usuario", "aroncal@gmail.com"));
        edtPassword.setText(preferences.getString("password", "12346"));
        Log.i("nombre del mensaje", preferences.getString("usuario","333333"));




    }

    private void actualizarFecha(String usuario){

    }
}