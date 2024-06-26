package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.api.SettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {
    override fun read(settingsKey: String): Settings {
        return settingsRepository.read(settingsKey)
    }

    override fun write(writeRequest: WriteRequest, settingsKey: String): Boolean {
        return settingsRepository.write(writeRequest, settingsKey)
    }

    override fun clear() {
        settingsRepository.clear()
    }
}
