package com.example.jan.flipflop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jan on 19/10/2015.
 */
public class NomesSQLHelper extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "dbNomes";
    private static  final int VERSÃO_DO_BANCO = 1;

    public static final String TABELA_NOMES = "nomes";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";

    public NomesSQLHelper(Context context) {
        super(context, NOME_BANCO, null, VERSÃO_DO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABELA_NOMES + " (" +
                        COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COLUNA_NOME + " TEXT UNIQUE NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
