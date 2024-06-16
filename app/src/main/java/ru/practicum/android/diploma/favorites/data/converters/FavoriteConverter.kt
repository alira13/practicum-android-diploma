package ru.practicum.android.diploma.favorites.data.converters

import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteConverter {
    fun mapEntityToModel(entity: VacancyEntity): VacancyDetails =
        VacancyDetails(
            entity.vacancyId,
            entity.name,
            entity.employerName,
            entity.employerLogo,
            entity.alternateUrl,
            entity.areaName,
            entity.experience,
            entity.salary,
            entity.employment,
            entity.description,
            entity.keySkills,
            entity.contactName,
            entity.contactPhone,
            entity.contactEmail,
            entity.comment,
            entity.isFavorite,
        )

    fun mapModelToEntity(model: VacancyDetails): VacancyEntity =
        VacancyEntity(
            id = 0,
            model.id,
            model.name,
            model.employer,
            model.logoUrls,
            model.alternateUrl,
            model.area,
            model.experience,
            model.salary,
            model.employment,
            model.description,
            model.keySkills,
            model.contactName,
            model.phone,
            model.email,
            model.comment,
            true
        )
}
