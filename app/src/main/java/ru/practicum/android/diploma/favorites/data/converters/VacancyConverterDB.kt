package ru.practicum.android.diploma.favorites.data.converters

import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB

class VacancyConverterDB {
    fun mapEntityToModel(entity: VacancyEntity): VacancyDetailsDB =
        VacancyDetailsDB(
            entity.vacancyId,
            entity.name,
            entity.currency,
            entity.salaryFrom,
            entity.salaryTo,
            entity.employerName,
            entity.employerLogo,
            entity.areaName,
            entity.experience,
            entity.employment,
            entity.schedule,
            entity.description,
            entity.keySkills,
            entity.contactName,
            entity.contactEmail,
            entity.contactPhone,
            entity.comment
        )

    fun mapModelToEntity(model: VacancyDetailsDB): VacancyEntity =
        VacancyEntity(
            id = 0,
            model.id,
            model.name,
            model.currency,
            model.salaryFrom,
            model.salaryTo,
            model.employerName,
            model.employerLogo,
            model.areaName,
            model.experience,
            model.employment,
            model.schedule,
            model.description,
            model.keySkills,
            model.contactName,
            model.contactEmail,
            model.contactPhone,
            model.comment
        )
}

