package com.jvapp.listadetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jvapp.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements ITarefaDAO {

    private SQLiteDatabase escreve, le;

    public TarefaDAO(Context context) {
        DbHeelper db = new DbHeelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome ", tarefa.getNomeTarefa());

        try{
            escreve.insert(DbHeelper.TABELA_TAREFAS, null,cv );
            Log.e("INFO", "Tarefa Salva com Sucesso  " );
        }catch (Exception e){
            Log.e("INFO", "Erro ao Salvar Tarefa " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa() );

        try {
            String[] args = {tarefa.getId().toString()};
            escreve.update(DbHeelper.TABELA_TAREFAS, cv, "id=?", args );
            Log.i("INFO", "Tarefa atualizada com sucesso!");
        }catch (Exception e){
            Log.e("INFO", "Erro ao atualizada tarefa " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        try{
            String[] args = {tarefa.getId().toString()};
            escreve.delete(DbHeelper.TABELA_TAREFAS,"id=?", args);
            Log.i("INFO", "Tarefa removida com Sucesso  " );
        }catch (Exception e){
            Log.e("INFO", "Erro1 ao remover Tarefa " + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public List<Tarefa> listar() {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHeelper.TABELA_TAREFAS + ";";
        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext()){
            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);

            tarefas.add(tarefa);


        }

        return tarefas;
    }
}
