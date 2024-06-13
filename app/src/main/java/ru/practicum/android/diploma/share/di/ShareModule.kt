package ru.practicum.android.diploma.share.di

import org.koin.dsl.module
import ru.practicum.android.diploma.share.data.ExternalNavigator
import ru.practicum.android.diploma.share.domain.api.SharingInteractor
import ru.practicum.android.diploma.share.domain.impl.SharingInteractorImpl

val shareModule = module {
    single {
        ExternalNavigator(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}
