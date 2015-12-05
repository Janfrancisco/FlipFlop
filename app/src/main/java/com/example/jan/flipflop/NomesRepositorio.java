package com.example.jan.flipflop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan on 19/10/2015.
 */
public class NomesRepositorio {
    private NomesSQLHelper helper;
    private ContentValues cv;

    public NomesRepositorio(Context ctx) {
        helper  = new NomesSQLHelper(ctx);
        cv = new ContentValues();
    }
    public Long inserirNome( Nome nome){
        SQLiteDatabase sql = helper.getWritableDatabase();

        cv.put(NomesSQLHelper.COLUNA_NOME, nome.getNome());

        long id = sql.insert(NomesSQLHelper.TABELA_NOMES, null, cv);
        if( id != -1){
            nome.setId(id);
        }
        sql.close();

        return id;
    }

    public List<String> listaNomes(){
        SQLiteDatabase sql = helper.getReadableDatabase();
        String sqlString = "SELECT * FROM " + NomesSQLHelper.TABELA_NOMES + " ORDER BY " + NomesSQLHelper.COLUNA_NOME;

        Cursor cursor = sql.rawQuery(sqlString, null);
        List<String> nomes = new ArrayList<String>();
        while (cursor.moveToNext()){
            String nome = cursor.getString(
                    cursor.getColumnIndex(NomesSQLHelper.COLUNA_NOME));
            nomes.add(nome);

        }
        cursor.close();
        sql.close();

        return nomes;
    }
}
