package com.example.jan.flipflop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, Runnable {
    TextToSpeech tts;
    List<String> nomes;
    ArrayList<String> nomesDigitados;
    ArrayAdapter<String> adapterList;
    AutoCompleteTextView autoTextNome;
    NomesRepositorio mNomeRepositorio;
    ArrayAdapter<String> mAdapter;
    TextView txtResultado;
    ListView listView;
    Button btSortear;
    boolean flagSortear;
    String textChoice;
    ProgressDialog mProgress;
    boolean flagVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tts = new TextToSpeech(this, this);

        mNomeRepositorio = new NomesRepositorio(this);

        txtResultado = (TextView) findViewById(R.id.txtResultado);

        nomes = mNomeRepositorio.listaNomes();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);

        // Flag utilizada para saber se já houve um sorteio
        flagSortear = false;

        autoTextNome = (AutoCompleteTextView) findViewById(R.id.acText);
        //Esconde o teclado virtual ao iniciar o aplicativo
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(autoTextNome.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        autoTextNome.setAdapter(mAdapter);
        autoTextNome.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                if (flagSortear) { // Se já houve um sorteio, prepara os componentes para um novo sorteio
                    txtResultado.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    adapterList.clear();
                    adapterList.notifyDataSetChanged();

                    flagSortear = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        if (savedInstanceState != null) {
            nomesDigitados = savedInstanceState.getStringArrayList("nomes");
        } else {
            nomesDigitados = new ArrayList<String>();
        }

        listView = (ListView) findViewById(R.id.listView);
        adapterList = new ArrayAdapter<String>(this, R.layout.item_1, nomesDigitados);
        listView.setAdapter(adapterList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("nomes", nomesDigitados);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        flagVolume = false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mute) {
            if (flagVolume) {
                item.setIcon(R.mipmap.ic_action_volume_on);
                flagVolume = false;
            } else {
                item.setIcon(R.mipmap.ic_action_volume_muted);
                flagVolume = true;
            }
            return true;
        } else if (id == R.id.action_info) {

            SobreDialogFragment sobreDialogFragment =
                    new SobreDialogFragment();
            sobreDialogFragment.show(getSupportFragmentManager(), SobreDialogFragment.DIALOG_TAG);

        }


        return super.onOptionsItemSelected(item);
    }

    public void cadastraNoBanco(View v) {
        String texto = autoTextNome.getText().toString();
        if (!texto.equals("")) {
            Nome n = new Nome(texto);
            autoTextNome.setText("");

            mNomeRepositorio.inserirNome(n);

            nomesDigitados.add(texto);
            adapterList.notifyDataSetChanged();
            // Se existir mais de um nome confirmado, habilita o botão de Sortear
            if (nomesDigitados.size() > 1) {
                btSortear = (Button) findViewById(R.id.button);
                btSortear.setEnabled(true);
            }
            //atualizaBanco();
        } else {
            // Envia uma mensagem de erro caso o AutoText esteja vazio
            Toast.makeText(this, "Campo vazio. Digite novamente!", Toast.LENGTH_LONG).show();
        }

    }

    /*public void atualizaBanco() {

        nomes = mNomeRepositorio.listaNomes();
        for (String n : nomes) {
            Log.i("JRX", n);
        }
    }*/

    public void sortearNome(View v) {
        //Austa o volume do TTS para o máximo
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int amStreamMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
        am.setStreamVolume(am.STREAM_MUSIC, amStreamMaxVol, 0);


        // Sorteia o nome
        int numSorteado = (int) (Math.random() * (nomesDigitados.size()));

        textChoice = nomesDigitados.get(numSorteado);
        txtResultado.setText(textChoice);

        //Esconde o teclado virtual
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(autoTextNome.getWindowToken(), 0);


        //Exibe uma caixa de progresso durante o sorteio
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Sorteio");
        mProgress.setMessage("Sorteando, aguarde...");
        mProgress.setCancelable(false);
        mProgress.show();

        // Implementa um atraso de 3 segundos
        new Handler().postDelayed(this, 3000);

        btSortear.setEnabled(false);
        flagSortear = true;
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void run() {

        if (flagVolume == false) {
            tts.speak("O sorteado foi " + textChoice, TextToSpeech.QUEUE_FLUSH, null);
        }

        txtResultado.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        mProgress.dismiss();

    }
}
