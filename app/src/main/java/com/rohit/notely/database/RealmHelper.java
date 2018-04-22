package com.rohit.notely.database;

import android.content.Context;

import com.rohit.notely.models.NoteData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelper {

    public static void setDefaultConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("notely.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
//
    public static void addNoteData(final NoteData noteData) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(noteData);
            }
        });
        realm.close();
    }

    public static ArrayList<NoteData> getAllNotes() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NoteData> results = realm.where(NoteData.class).equalTo("isDelete", false).findAll();
        results = results.sort("timeStamp", Sort.DESCENDING);
        if (results.size() > 0) {
            ArrayList<NoteData> noteDataArrayList=new ArrayList<>();
            for(NoteData noteData:results){
                noteDataArrayList.add(noteData);
            }
            return noteDataArrayList;
        } else
            return null;
    }
    public static NoteData getNoteById( String id) {
        //ta
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<NoteData> query = realm.where(NoteData.class).equalTo("id", id);
        RealmResults<NoteData> results = query.findAll();
        if (results.size() > 0) {
            return results.first();
        } else
            return null;
    }
    public static void setHeart(String id, boolean isHearted){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<NoteData> results=realm.where(NoteData.class).equalTo("id",id).findAll();
        if(results!=null && results.size()>0){
            realm.beginTransaction();
            results.first().setHearted(isHearted);
            realm.commitTransaction();
        }
        realm.close();
    }
    public static void updateNote(String id,String type,String content ,String header,String timeStamp){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<NoteData> results=realm.where(NoteData.class).equalTo("id",id).findAll();
        if(results!=null && results.size()>0){
            realm.beginTransaction();
            NoteData noteData=results.first();
            noteData.setTitle(header);
            noteData.setContent(content);
            noteData.setType(type);
            noteData.setTimeStamp(timeStamp);
            realm.commitTransaction();
        }
        realm.close();
    }
    public static void setFavorite(String id, boolean isFav){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<NoteData> results=realm.where(NoteData.class).equalTo("id",id).findAll();
        if(results!=null && results.size()>0){
            realm.beginTransaction();
            results.first().setFavourite(isFav);
            realm.commitTransaction();
        }
        realm.close();
    }
    public static ArrayList<NoteData> getAllFilteredNotes(String noteType,boolean isHearted,boolean isFav) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NoteData> results;
        if(noteType.equalsIgnoreCase("all")){
            if(!isFav && !isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).findAll();
            }else if(isFav && !isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("isFavourite", isFav).findAll();
            }else if(!isFav && isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("isHearted", isHearted).findAll();
            }else{
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("isFavourite", isFav).equalTo("isHearted", isHearted).findAll();
            }
        }else{
            if(!isFav && !isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("type", noteType).findAll();
            }else if(isFav && !isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("type", noteType).findAll().where().equalTo("isFavourite", isFav).findAll();
            }else if(!isFav && isHearted){
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("type", noteType).findAll().where().equalTo("isHearted", isHearted).findAll();
            }else{
                results = realm.where(NoteData.class).equalTo("isDelete", false).equalTo("type", noteType).equalTo("isFavourite", isFav).equalTo("isHearted", isHearted).findAll();
            }
        }

        results = results.sort("timeStamp", Sort.DESCENDING);
        if (results.size() > 0) {
            ArrayList<NoteData> noteDataArrayList=new ArrayList<>();
            for(NoteData noteData:results){
                noteDataArrayList.add(noteData);
            }
            return noteDataArrayList;
        } else
            return null;
    }
    public static void deleNoteData(String id){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<NoteData> results=realm.where(NoteData.class).equalTo("id",id).findAll();
        if(results!=null && results.size()>0){
            realm.beginTransaction();
            results.first().setDelete(true);
            realm.commitTransaction();
        }
        realm.close();
    }
}