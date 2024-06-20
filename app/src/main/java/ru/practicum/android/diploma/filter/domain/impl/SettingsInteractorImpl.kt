package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.api.SettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {
    override fun read(): Settings {
        return settingsRepository.read()
    }

    override fun write(writeRequest: WriteRequest): Boolean {
        return settingsRepository.write(writeRequest)
    }

    override fun clear() {
        settingsRepository.clear()
    }
}
