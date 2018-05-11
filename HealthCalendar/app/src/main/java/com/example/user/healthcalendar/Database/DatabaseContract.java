package com.example.user.healthcalendar.Database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    static final String DATABASE_NAME = "HealthApp.db"; // ".db" may be removed
    static final int DATABASE_VERSION = 1;

    public static final class DoctorsColumns implements BaseColumns{

        public final static String TABLE_NAME = "Doctors";

        public static final String _ID = BaseColumns._ID;
        public static final String SPECIALITY = "speciality";
        public static final String NAME = "name";
        public static final String SURNAME = "surname";
        public static final String FATHERSNAME = "fathersname";
        public static final String ADDRESS = "address";
        public static final String CONTACTS = "contacts";
        public static final String COMMENT = "comment";

    }

    public static  final class EventsColumns implements BaseColumns{

        public final static String TABLE_NAME = "Events";

        public static final String _ID = BaseColumns._ID;
        public static final String DOCTOR_ID = "doctors_id";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String COMMENT = "comment";

    }
}
