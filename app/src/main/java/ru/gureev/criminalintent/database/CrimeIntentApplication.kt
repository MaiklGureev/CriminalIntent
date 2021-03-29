package ru.gureev.criminalintent.database

import android.app.Application

class CrimeIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(context = applicationContext)

    }
}