package cn.dyc.myshanxin.client.view;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dyc_DBHelper extends SQLiteOpenHelper{
     private  static final String DATABASE_NAME="dyc.db";
     private static final int DATABASE_VERSION=1;
	public Dyc_DBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		
	}
	 public Dyc_DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("create table if not exists user_info("+
" username varchar primary key,"+
 "passwd  varchar,"+
 "sex int,"+
 "hobby1 varchar,"+
"hobby2 varchar,"+
"hobby3 varchar,"+
"city varchar)");
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		
	}

}
