package ru.practicum.android.diploma.favorites.data.converters

import com.google.gson.Gson
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB

class VacancyConverterDB(private val gson: Gson) {
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
            gson.fromJson(entity.keySkills, Array<String>::class.java).toList(),
            entity.contactName,
            entity.contactEmail,
            gson.fromJson(entity.contactPhone, Array<String>::class.java).toList(),
            gson.fromJson(entity.comment, Array<String>::class.java).toList()
        )

    fun mapModelToEntity(model: VacancyDetailsDB): VacancyEntity =
        VacancyEntity(
            id = 0,
            vacancyId = model.id,
            name = model.name,
            currency = model.currency,
            salaryFrom = model.salaryFrom,
            salaryTo = model.salaryTo,
            employerName = model.employerName,
            employerLogo = model.employerLogo,
            areaName = model.areaName,
            experience = model.experience,
            employment = model.employment,
            schedule = model.schedule,
            description = model.description,
            keySkills = gson.toJson(model.keySkills),
            contactName = model.contactName,
            contactEmail = model.contactEmail,
            contactPhone = gson.toJson(model.contactPhone),
            comment = gson.toJson(model.comment)
        )
}

