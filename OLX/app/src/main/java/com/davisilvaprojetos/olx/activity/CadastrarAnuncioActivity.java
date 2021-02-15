package com.davisilvaprojetos.olx.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.davisilvaprojetos.olx.R;
import com.davisilvaprojetos.olx.helper.Permissoes;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity
implements View.OnClickListener{
    private EditText campoTitulo, campoDescricao;
    private ImageView imagem1,imagem2,imagem3;
    private Spinner campoEstado,campoCategoria;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);
        //Validar permisoes
        Permissoes.validarPermissoes(permissoes,this,1);
        inicializarComponentes();
        carregarDadosSpinner();
    }
    public void validarDadosAnuncio(View view){
        String fone = "";
        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = String.valueOf(campoValor.getRawValue());
        String telefone = campoTelefone.getText().toString();
        if(campoTelefone.getUnMasked() != null){
            fone= campoTelefone.getUnMasked();
        }

        String descricao = campoDescricao.getText().toString();
        System.out.println("FONE: "+fone +"Valor: "+valor);
        if(listaFotosRecuperadas.size() != 0){
            if(!estado.isEmpty()){
                if(!categoria.isEmpty()){
                    if(!titulo.isEmpty()){
                        if(!valor.isEmpty() && !valor.equals("0")){
                            if(!telefone.isEmpty() && fone.length() >=10){
                                if(!descricao.isEmpty()){
                                        salvarAnuncio();
                                }else{
                                    exibirMensagemErro("Preencha o campo descrição!");
                                }

                            }else{
                                exibirMensagemErro("Preencha o campo telefone, digite ao menos 10 números!");
                            }

                        }else{
                            exibirMensagemErro("Preencha o campo valor!");
                        }

                    }else{
                        exibirMensagemErro("Preencha o campo título!");
                    }

                }else{
                    exibirMensagemErro("Preencha o campo categoria!");
                }

            }else{
                exibirMensagemErro("Preencha o campo estado!");
            }

        }else{
            exibirMensagemErro("Selecione ao menos uma foto!");
        }

    }
    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    public void salvarAnuncio(){
        String valor = campoValor.getHintString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageCadastro1 :
                escolherImagem(1);
                break;
            case R.id.imageCadastro2 :
                escolherImagem(2);
                break;
            case R.id.imageCadastro3 :
                escolherImagem(3);
                break;

        }
    }
    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            //Recuperar imagem
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configura imagem no ImageView
            if(requestCode == 1){
                imagem1.setImageURI(imagemSelecionada);
            }else if(requestCode == 2){
                imagem2 .setImageURI(imagemSelecionada);
            }else if(requestCode == 3){
                imagem3.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }

    private void carregarDadosSpinner(){
        /*String[] estados = new String[]{
            "SP","MT"
        };*/
        //Configura spinner de estados
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(
                this,   android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoEstado.setAdapter(adapterEstados);

        //Configura spinner de categorias
        String[] categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(
                this,   android.R.layout.simple_spinner_item,
                categorias
        );
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoria.setAdapter(adapterCategoria);


    }
    private void inicializarComponentes(){
        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        campoEstado = findViewById(R.id.spinnerEstado);
        campoCategoria = findViewById(R.id.spinnerCategoria);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);

        //Configura a localidade para PT-BR Portugues Brasil
        Locale locale = new Locale("pt","BR");
        campoValor.setLocale(locale);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado:grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app, é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}