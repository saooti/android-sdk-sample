package com.saooti.sdksample

import android.app.Application
import com.saooti.core.Saooti

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Saooti.init(applicationContext)

        Saooti.setApiUrl("https://api.octopus.saooti.com")

        Saooti.setOrganisationId("<organisation_id>")
    }
}