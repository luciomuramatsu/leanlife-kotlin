package com.br.leanlife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Exercise;
import com.br.leanlife.models.Group_exercise;
import com.br.leanlife.models.Muscular_group;
import com.br.leanlife.models.Treino;
import com.br.leanlife.models.Workout;

/**
 * Created by Raphael on 19/04/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 10;

    // Database Name
    private static final String DATABASE_NAME = "leanlife";

    // Ofertas table name
    private static final String TABLE1 = "treinos";
    private static final String TABLE2 = "exercicio";
    private static final String TABLE3 = "workout";
    private static final String TABLE4 = "group_exercise";
    private static final String TABLE5 = "muscular_group";
    private static final String TABLE6 = "exercise";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE1 = "CREATE TABLE treinos ("
                +"id INTEGER PRIMARY KEY, training_1_id INTEGER, training_2_id INTEGER, users_id INTEGER,"
                +"cycle_number INTEGER, stage_number INTEGER, session_number INTEGER, flag_status INTEGER, date_ini DATETIME, date_final DATETIME,"
                +"free_or_paid CHAR(20),created_at CHAR(50), updated_at CHAR(50), train_type CHAR(10), max_exercises INTEGER )";

        String CREATE_TABLE2 = "CREATE TABLE exercicio ("
                +"id INTEGER PRIMARY KEY, routine_id INTEGER, group_exercise_id INTEGER , exercise_id INTEGER, rest_seconds INTEGER, series INTEGER,"
                +"date_ini DATETIME, date_final DATETIME, flag_status INTEGER,"
                +"created_at CHAR(50), updated_at CHAR(50), train_type CHAR(255) DEFAULT '0', estatreino1 INT(11) DEFAULT '0',"
                +"estatreino2 INT(11) DEFAULT '0', estatreino3 INT(11) DEFAULT '0', estatreino4 INT(11) DEFAULT '0')";

        String CREATE_TABLE3 = "CREATE TABLE workout ("
                +"id INTEGER PRIMARY KEY, exercise_routine_id INTEGER,"
                +"reps_estimated CHAR(20), reps_real CHAR(20), velocity INTEGER, weight FLOAT(9,2), type_weight INTEGER, status INTEGER, date_ini DATETIME, date_final DATETIME, flag_status INTEGER, created_at CHAR(50), updated_at CHAR(50) )";


        String CREATE_TABLE4 = "CREATE TABLE group_exercise ("
                +"id INTEGER PRIMARY KEY, muscular_group_id INTEGER,"
                +" name_port CHAR(255), name_english CHAR(255), name_espanish CHAR(255), nickname CHAR(255), ranking CHAR(50), repititions_session CHAR(50), existsx CHAR(20), created_at CHAR(50), updated_at CHAR(50) )";


        String CREATE_TABLE5 = "CREATE TABLE muscular_group ("
                +"id INTEGER PRIMARY KEY,"
                +" name_port CHAR(255), name_english CHAR(255), name_espanish CHAR(255), nickname CHAR(255), existsx CHAR(20), created_at CHAR(50), updated_at CHAR(50), genericmuscular CHAR(255), urlicon CHAR(255) )";


        String CREATE_TABLE6 = "CREATE TABLE exercise ("
                +"id INTEGER PRIMARY KEY, group_exercise_id INTEGER,"
                +" name_port CHAR(255), name_english CHAR(255), name_espanish CHAR(255), image_mini CHAR(255), image_medium CHAR(255), image_big CHAR(255), gif CHAR(255), nickname CHAR(255), existsx CHAR(20), created_at CHAR(50), updated_at CHAR(50), gif2 CHAR(255), gif3 CHAR(255), ranking CHAR(50), efficiency CHAR(50), difficulty CHAR(50), find_exercise CHAR(255) )";

        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
        db.execSQL(CREATE_TABLE6);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE5);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE6);

        // Create tables again
        onCreate(db);
    }
    public void deletaTudo()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE1, null, null);
        db.delete(TABLE2, null, null);
        db.delete(TABLE3, null, null);
        db.delete(TABLE4, null, null);
        db.delete(TABLE5, null, null);
        db.delete(TABLE6, null, null);
        db.close();
    }
    public String convertDate(String data)
    {
        DateFormat sdf1, sdf2;
        sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date2 = null;
        try {
            date2 = sdf2.format( sdf1.parse( data ) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }
    // Adding new oferta
    void addTreinos(Treino treino) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE1, "id = ?", new String[] {"" + treino.id});

        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", treino.id);
        values.put("training_1_id", treino.training_1_id);
        values.put("training_2_id", treino.training_2_id);
        values.put("users_id", treino.users_id);
        values.put("cycle_number", treino.cycle_number);
        values.put("stage_number", treino.stage_number);
        values.put("session_number", treino.session_number);
        values.put("flag_status", treino.flag_status);
        values.put("date_ini", treino.date_ini);
        values.put("date_final", treino.date_final);
        values.put("free_or_paid", treino.free_or_paid);
        values.put("created_at", treino.created_at);
        values.put("updated_at", treino.updated_at);
        values.put("train_type", treino.train_type);
        values.put("max_exercises", treino.max_exercises);

        // Inserting Row
        db.insert(TABLE1, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Ofertas
    public List<Treino> getAllTreinossimplebase() {
        List<Treino> Ofertalista = new ArrayList<Treino>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE1 + " ORDER BY cycle_number desc, stage_number asc, session_number asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Treino treino = new Treino(cursor.getInt(0),cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getInt(14), null);
                // Adding contact to list
                Ofertalista.add(treino);


            } while (cursor.moveToNext());
        }

        // return oferta list
        return Ofertalista;
    }
    // Getting All Ofertas
    public List<Treino> getAlltreinosimple2(String id) {
        List<Treino> Ofertalista = new ArrayList<Treino>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE1 + " WHERE id = "+id+" ORDER BY cycle_number desc, stage_number asc, session_number asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                List<Exercicio> lista1 = new ArrayList<Exercicio>();
                String selectQuery2 = "SELECT  * FROM " + TABLE2 + " WHERE routine_id = "+cursor.getString(0)+" ORDER BY id ASC";
                Cursor cursor2 = db.rawQuery(selectQuery2, null);
                if (cursor2.moveToFirst()) {
                    do {

                        List<Workout> lista2 = new ArrayList<Workout>();
                        Group_exercise lista3 = null;
                        Exercise lista4 = null;

                        String selectQuery3 = "SELECT  * FROM " + TABLE4 + " WHERE id = "+cursor2.getString(2)+" ORDER BY id ASC";
                        Cursor cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                Muscular_group lista3b = null;

                                String selectQuery4 = "SELECT  * FROM " + TABLE5 + " WHERE id = "+cursor3.getString(1)+" ORDER BY id ASC";
                                Cursor cursor4 = db.rawQuery(selectQuery4, null);
                                if (cursor4.moveToFirst()) {
                                    do {
                                        lista3b = new Muscular_group(cursor4.getInt(0),cursor4.getString(1), cursor4.getString(2), cursor4.getString(3),
                                                cursor4.getString(4), cursor4.getString(5), cursor4.getString(6), cursor4.getString(7), cursor4.getString(8), cursor4.getString(9));
                                    } while (cursor4.moveToNext());
                                }

                                lista3 = new Group_exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), lista3b);


                            } while (cursor3.moveToNext());
                        }

                        selectQuery3 = "SELECT  * FROM " + TABLE6 + " WHERE id = "+cursor2.getString(3)+" ORDER BY id ASC";
                        cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                lista4 = new Exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), cursor3.getString(11), cursor3.getString(12), cursor3.getString(13), cursor3.getString(14), cursor3.getString(15), cursor3.getString(16), cursor3.getString(17), cursor3.getString(18));


                            } while (cursor3.moveToNext());
                        }


                        Exercicio ex1 = new Exercicio(cursor2.getInt(0),cursor2.getInt(1), cursor2.getInt(2), cursor2.getInt(3),
                                cursor2.getInt(4), cursor2.getInt(5), cursor2.getString(6), cursor2.getString(7), cursor2.getInt(8), cursor2.getString(9), cursor2.getString(10),cursor2.getString(11),cursor2.getString(12),cursor2.getString(13),cursor2.getString(14),cursor2.getString(15),lista2,lista3,lista4);
                        lista1.add(ex1);


                    } while (cursor2.moveToNext());
                }


                Treino treino = new Treino(cursor.getInt(0),cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),cursor.getInt(14), lista1);
                // Adding contact to list
                Ofertalista.add(treino);


            } while (cursor.moveToNext());
        }

        // return oferta list
        return Ofertalista;
    }
    // Getting All Ofertas
    public List<Treino> getAlltreinosimple(String id) {
        List<Treino> Ofertalista = new ArrayList<Treino>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE1 + " WHERE id = "+id+" ORDER BY cycle_number desc, stage_number asc, session_number asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                List<Exercicio> lista1 = new ArrayList<Exercicio>();
                String selectQuery2 = "SELECT  * FROM " + TABLE2 + " WHERE routine_id = "+cursor.getString(0)+" ORDER BY id ASC";
                Cursor cursor2 = db.rawQuery(selectQuery2, null);
                if (cursor2.moveToFirst()) {
                    do {

                        List<Workout> lista2 = new ArrayList<Workout>();
                        Group_exercise lista3 = null;
                        Exercise lista4 = null;

                        String selectQuery3 = "SELECT  * FROM " + TABLE3 + " WHERE exercise_routine_id = "+cursor2.getString(0)+" ORDER BY id ASC";
                        Cursor cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                Workout wk1 = new Workout(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getInt(4), cursor3.getFloat(5), cursor3.getInt(6), cursor3.getInt(7), cursor3.getString(8), cursor3.getString(9), cursor3.getInt(10), cursor3.getString(11), cursor3.getString(12));
                                lista2.add(wk1);

                            } while (cursor3.moveToNext());
                        }

                        selectQuery3 = "SELECT  * FROM " + TABLE4 + " WHERE id = "+cursor2.getString(2)+" ORDER BY id ASC";
                        cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                Muscular_group lista3b = null;

                                String selectQuery4 = "SELECT  * FROM " + TABLE5 + " WHERE id = "+cursor3.getString(1)+" ORDER BY id ASC";
                                Cursor cursor4 = db.rawQuery(selectQuery4, null);
                                if (cursor4.moveToFirst()) {
                                    do {
                                        lista3b = new Muscular_group(cursor4.getInt(0),cursor4.getString(1), cursor4.getString(2), cursor4.getString(3),
                                                cursor4.getString(4), cursor4.getString(5), cursor4.getString(6), cursor4.getString(7), cursor4.getString(8), cursor4.getString(9));
                                    } while (cursor4.moveToNext());
                                }

                                lista3 = new Group_exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), lista3b);


                            } while (cursor3.moveToNext());
                        }

                        selectQuery3 = "SELECT  * FROM " + TABLE6 + " WHERE id = "+cursor2.getString(3)+" ORDER BY id ASC";
                        cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                lista4 = new Exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), cursor3.getString(11), cursor3.getString(12), cursor3.getString(13), cursor3.getString(14), cursor3.getString(15), cursor3.getString(16), cursor3.getString(17), cursor3.getString(18));


                            } while (cursor3.moveToNext());
                        }


                        Exercicio ex1 = new Exercicio(cursor2.getInt(0),cursor2.getInt(1), cursor2.getInt(2), cursor2.getInt(3),
                                cursor2.getInt(4), cursor2.getInt(5), cursor2.getString(6), cursor2.getString(7), cursor2.getInt(8), cursor2.getString(9), cursor2.getString(10), cursor2.getString(11),cursor2.getString(12),cursor2.getString(13),cursor2.getString(14),cursor2.getString(15),lista2,lista3,lista4);
                        lista1.add(ex1);


                    } while (cursor2.moveToNext());
                }


                Treino treino = new Treino(cursor.getInt(0),cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getInt(14), lista1);
                // Adding contact to list
                Ofertalista.add(treino);


            } while (cursor.moveToNext());
        }

        // return oferta list
        return Ofertalista;
    }
    // Getting All Ofertas
    public List<Treino> getAllTreinos() {
        List<Treino> Ofertalista = new ArrayList<Treino>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE1 + " ORDER BY cycle_number desc, stage_number asc, session_number asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                List<Exercicio> lista1 = new ArrayList<Exercicio>();
                String selectQuery2 = "SELECT  * FROM " + TABLE2 + " WHERE routine_id = "+cursor.getString(0)+" ORDER BY id ASC";
                Cursor cursor2 = db.rawQuery(selectQuery2, null);
                if (cursor2.moveToFirst()) {
                    do {

                        List<Workout> lista2 = new ArrayList<Workout>();
                        Group_exercise lista3 = null;
                        Exercise lista4 = null;

                        String selectQuery3 = "SELECT  * FROM " + TABLE3 + " WHERE exercise_routine_id = "+cursor2.getString(0)+" ORDER BY id ASC";
                        Cursor cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                Workout wk1 = new Workout(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getInt(4), cursor3.getFloat(5), cursor3.getInt(6), cursor3.getInt(7), cursor3.getString(8), cursor3.getString(9), cursor3.getInt(10), cursor3.getString(11), cursor3.getString(12));
                                lista2.add(wk1);

                            } while (cursor3.moveToNext());
                        }

                        selectQuery3 = "SELECT  * FROM " + TABLE4 + " WHERE id = "+cursor2.getString(2)+" ORDER BY id ASC";
                        cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                Muscular_group lista3b = null;

                                String selectQuery4 = "SELECT  * FROM " + TABLE5 + " WHERE id = "+cursor3.getString(1)+" ORDER BY id ASC";
                                Cursor cursor4 = db.rawQuery(selectQuery4, null);
                                if (cursor4.moveToFirst()) {
                                    do {
                                        lista3b = new Muscular_group(cursor4.getInt(0),cursor4.getString(1), cursor4.getString(2), cursor4.getString(3),
                                                cursor4.getString(4), cursor4.getString(5), cursor4.getString(6), cursor4.getString(7), cursor4.getString(8), cursor4.getString(9));
                                    } while (cursor4.moveToNext());
                                }

                                lista3 = new Group_exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), lista3b);


                            } while (cursor3.moveToNext());
                        }

                        selectQuery3 = "SELECT  * FROM " + TABLE6 + " WHERE id = "+cursor2.getString(3)+" ORDER BY id ASC";
                        cursor3 = db.rawQuery(selectQuery3, null);
                        if (cursor3.moveToFirst()) {
                            do {

                                lista4 = new Exercise(cursor3.getInt(0),cursor3.getInt(1), cursor3.getString(2), cursor3.getString(3),
                                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), cursor3.getString(11), cursor3.getString(12), cursor3.getString(13), cursor3.getString(14), cursor3.getString(15), cursor3.getString(16), cursor3.getString(17), cursor3.getString(18));


                            } while (cursor3.moveToNext());
                        }


                        Exercicio ex1 = new Exercicio(cursor2.getInt(0),cursor2.getInt(1), cursor2.getInt(2), cursor2.getInt(3),
                                cursor2.getInt(4), cursor2.getInt(5), cursor2.getString(6), cursor2.getString(7), cursor2.getInt(8), cursor2.getString(9), cursor2.getString(10), cursor2.getString(11),cursor2.getString(12),cursor2.getString(13),cursor2.getString(14),cursor2.getString(15),lista2,lista3,lista4);
                        lista1.add(ex1);


                    } while (cursor2.moveToNext());
                }


                Treino treino = new Treino(cursor.getInt(0),cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),cursor.getInt(14),lista1);
                // Adding contact to list
                Ofertalista.add(treino);


            } while (cursor.moveToNext());
        }

        // return oferta list
        return Ofertalista;
    }



    // Deleting single oferta
    public void deleteTreinos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE1, null, null);
        db.close();
    }

    // Adding new oferta
    void addExercicio(Exercicio exercicio) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE2, "id = ?", new String[] {"" + exercicio.id});

        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", exercicio.id);
        values.put("routine_id", exercicio.routine_id);
        values.put("group_exercise_id", exercicio.group_exercise_id);
        values.put("exercise_id", exercicio.exercise_id);
        values.put("rest_seconds", exercicio.rest_seconds);
        values.put("series", exercicio.series);
        values.put("date_ini", exercicio.date_ini);
        values.put("date_final", exercicio.date_final);
        values.put("flag_status", exercicio.flag_status);
        values.put("created_at", exercicio.created_at);
        values.put("updated_at", exercicio.updated_at);
        values.put("train_type", exercicio.train_type);
        values.put("estatreino1", exercicio.estatreino1);
        values.put("estatreino2", exercicio.estatreino2);
        values.put("estatreino3", exercicio.estatreino3);
        values.put("estatreino4", exercicio.estatreino4);

        // Inserting Row
        db.insert(TABLE2, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single oferta
    public void deleteEx() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE2, null, null);
        db.close();
    }

    // Adding new oferta
    void addworkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE3, "id = ?", new String[] {"" + workout.id});

        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", workout.id);
        values.put("exercise_routine_id", workout.exercise_routine_id);
        values.put("reps_estimated", workout.reps_estimated);
        values.put("reps_real", workout.reps_real);
        values.put("velocity", workout.velocity);
        values.put("weight", workout.weight);
        values.put("type_weight", workout.type_weight);
        values.put("status", workout.status);
        values.put("date_ini", workout.date_ini);
        values.put("date_final", workout.date_final);
        values.put("flag_status", workout.flag_status);
        values.put("created_at", workout.created_at);
        values.put("updated_at", workout.updated_at);

        // Inserting Row
        db.insert(TABLE3, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single oferta
    public void deleteWorkout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE3, null, null);
        db.close();
    }


    // Adding new oferta
    void addgroupexercise(Group_exercise ge1) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE4, "id = " + ge1.id, null);
        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", ge1.id);
        values.put("muscular_group_id", ge1.muscular_group_id);
        values.put("name_port", ge1.name_port);
        values.put("name_english", ge1.name_english);
        values.put("name_espanish", ge1.name_espanish);
        values.put("nickname", ge1.nickname);
        values.put("ranking", ge1.ranking);
        values.put("repititions_session", ge1.repititions_session);
        values.put("existsx", ge1.exists);
        values.put("created_at", ge1.created_at);
        values.put("updated_at", ge1.updated_at);


        // Inserting Row
        db.insert(TABLE4, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single oferta
    public void deletGroupExercise() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE4, null, null);
        db.close();
    }



    // Adding new oferta
    void addmusculargroup(Muscular_group mg1) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE5, "id = " + mg1.id, null);
        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", mg1.id);
        values.put("name_port", mg1.name_port);
        values.put("name_english", mg1.name_english);
        values.put("name_espanish", mg1.name_espanish);
        values.put("nickname", mg1.nickname);
        values.put("existsx", mg1.exists);
        values.put("created_at", mg1.created_at);
        values.put("updated_at", mg1.updated_at);
        values.put("genericmuscular", mg1.genericmuscular);
        values.put("urlicon", mg1.urlicon);

        // Inserting Row
        db.insert(TABLE5, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single oferta
    public void deletMuscularGroup() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE5, null, null);
        db.close();
    }


    // Adding new oferta
    void addExercise(Exercise ex1) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE6, "id = " + ex1.id, null);
        db.close();

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", ex1.id);
        values.put("group_exercise_id", ex1.group_exercise_id);
        values.put("name_port", ex1.name_port);
        values.put("name_english", ex1.name_english);
        values.put("name_espanish", ex1.name_espanish);
        values.put("image_mini", ex1.image_mini);
        values.put("image_medium", ex1.image_medium);
        values.put("image_big", ex1.image_big);
        values.put("gif", ex1.gif);
        values.put("nickname", ex1.nickname);
        values.put("existsx", ex1.exists);
        values.put("created_at", ex1.created_at);
        values.put("updated_at", ex1.updated_at);
        values.put("gif2", ex1.gif2);
        values.put("gif3", ex1.gif3);
        values.put("ranking", ex1.ranking);
        values.put("efficiency", ex1.efficiency);
        values.put("difficulty", ex1.difficulty);
        values.put("find_exercise", ex1.find_exercise);

        // Inserting Row
        db.insert(TABLE6, null, values);
        db.close(); // Closing database connection
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> Ofertalista = new ArrayList<Exercise>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE6;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {



                Exercise exercise = new Exercise(cursor.getInt(0),cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12)
                        , cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18));
                // Adding contact to list
                Ofertalista.add(exercise);


            } while (cursor.moveToNext());
        }

        // return oferta list
        return Ofertalista;
    }

    // Deleting single oferta
    public void deletExercise() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE6, null, null);
        db.close();
    }


}