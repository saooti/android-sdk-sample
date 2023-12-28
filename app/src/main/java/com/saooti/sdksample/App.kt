package com.saooti.sdksample

import android.app.Application
import com.saooti.core.Saooti
import com.saooti.core.session.Platform

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Saooti.init(applicationContext)

        Saooti.setOrganisationId("<organization_id>>")
    }
}