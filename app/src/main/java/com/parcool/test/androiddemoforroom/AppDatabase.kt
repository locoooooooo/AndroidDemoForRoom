package com.parcool.test.androiddemoforroom

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase

//@Database(entities = [User::class], version = 1, exportSchema = true)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//}

//迁移
@Database(
    entities = [User::class], version = 2, exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2, spec = AppDatabase.MyAutoMigration::class)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    class MyAutoMigration : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
//            db.execSQL("ALTER TABLE user ADD COLUMN nick_name Text NOT NULL")
        }
    }
}