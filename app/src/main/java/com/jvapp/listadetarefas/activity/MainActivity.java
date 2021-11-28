package com.jvapp.listadetarefas.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jvapp.listadetarefas.R;
import com.jvapp.listadetarefas.adapter.TarefaAdapter;
import com.jvapp.listadetarefas.helper.DbHeelper;
import com.jvapp.listadetarefas.helper.RecyclerItemClickListener;
import com.jvapp.listadetarefas.helper.TarefaDAO;
import com.jvapp.listadetarefas.model.Tarefa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefa = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);




        //Acidionar Evento de Clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Recupara a tarefa para edicação


                                Tarefa tarefaSelecionada = listaTarefa.get(position);

                                //Envia tarefa para tela adiciona tarefa

                                Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                                intent.putExtra("tarefaSelecionada", tarefaSelecionada );

                                startActivity( intent );


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                //Recupera tarefa para deletar
                                tarefaSelecionada = listaTarefa.get(position);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);


                                //Configura titulo e mensagem
                                dialog.setTitle("Confirma exclusão");
                                dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?");

                                //Configura os buttons
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        if(tarefaDAO.deletar(tarefaSelecionada)){

                                            carregarListaTarrefa();
                                            Toast.makeText(getApplicationContext(), "Sucesso ao Deletar tarefa!", Toast.LENGTH_LONG).show();


                                        }else{
                                            Toast.makeText(getApplicationContext(), "Error ao Deletar tarefa!", Toast.LENGTH_LONG).show();
                                        }



                                    }
                                });
                                //________________________________________________________________________________//
                                dialog.setNegativeButton("Não", null);

                                //Exibir Dialog

                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void carregarListaTarrefa(){

        //ListaTarrefa
            TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
            listaTarefa = tarefaDAO.listar();

        //Exibe Tarefas no RecyclerView



        //Configura Adapter
        tarefaAdapter = new TarefaAdapter(listaTarefa);



        //Configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);



    }

    @Override
    protected void onStart() {
        carregarListaTarrefa();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
