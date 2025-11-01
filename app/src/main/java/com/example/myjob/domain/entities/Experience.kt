package com.example.myjob.domain.entities

import android.view.View
import kotlinx.serialization.Serializable

val USER_EXP = listOf(
    "Stagiare/Alternant",
    "Premier emploi",
    "Confirmé",
    "Cadre",
    "Directeur",
    "Cadre supérieur",
)

@Serializable
data class Experience(
    var id: Int = View.generateViewId(),
    var title: String? = "",
    var description: String? = "",
    var listSkills: MutableList<String>? = mutableListOf(),
    var companyName: String? = "",
    var dateStart: String? = "",
    var dateEnd: String? = "",
    var place: String? = "",
    var type: String? = "",
    var typeContract: String? = "",
    var anotherActivitySector: String? = "",
    var freelanceFee: String? = "",
    var nbHours: Int? = 0,
    var nbDays: Int? = 0,
    var salary: Int? = 1000,
    var hourlyRate: Int? = 0,
    var current: Boolean = false,
    var isFreelance: Boolean = false,
    var isContract: Boolean = false,
    var idUser: Int? = 0
) {

    fun showExperience(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String>()

        val titleValid = title?.isNotEmpty() == true
        val companyNameValid = !companyName.isNullOrEmpty()
        val countryValid = !place.isNullOrEmpty()
        val dateStartValid = dateStart != null && dateStart != "Choose Date"
        val dateEndValid = dateEnd != null && dateEnd != "Choose Date" && dateEnd?.isNotEmpty() == true
        val typeValid = !type.isNullOrEmpty()
        val typeContractValid = !typeContract.isNullOrEmpty()
        val salaryValid = salary != null && salary != 1000


        val listTag = if (lang == "French" || lang == "Français") {
            listOf(
                "Titre",
                "Nom de l'entreprise",
                "Date de début",
                "Date de fin",
                "Emplacement",
                "Type",
                "Type du contrat",
                "Salaire"
            )
        } else {
            listOf(
                "Title",
                "Company name",
                "Start date",
                "End date",
                "Place",
                "Type",
                "Contract type",
                "Salary"
            )
        }



        if (titleValid) mapUser[listTag[0]] = title ?: ""
        if (companyNameValid) mapUser[listTag[1]] = companyName ?: ""
        if (dateStartValid) mapUser[listTag[2]] = dateStart ?: ""
        if (dateEndValid) mapUser[listTag[3]] = dateEnd ?: ""
        if (countryValid) mapUser[listTag[4]] = place ?: ""
        if (typeValid) mapUser[listTag[5]] = type ?: ""
        if (typeContractValid) mapUser[listTag[6]] = typeContract ?: ""
        if (salaryValid) mapUser[listTag[7]] = "${salary ?: 1000}"

        return mapUser
    }

    fun showUser1(lang: String): List<Pair<String, String>> {
        val mapUser = mutableListOf<Pair<String, String>>()

        val titleValid = title?.isNotEmpty() == true
        val companyNameValid = !companyName.isNullOrEmpty()
        val countryValid = !place.isNullOrEmpty()
        val dateStartValid = dateStart != null && dateStart != "Choose Date"
        val dateEndValid = dateEnd != null && dateEnd != "Choose Date" && dateEnd?.isNotEmpty() == true
        val typeValid = !type.isNullOrEmpty()
        val typeContractValid = !typeContract.isNullOrEmpty()
        val salaryValid = salary != null && salary != 1000


        val listTag = if (lang == "French" || lang == "Français") {
            listOf(
                "Titre",
                "Nom de l'entreprise",
                "Date de début",
                "Date de fin",
                "Emplacement",
                "Type",
                "Type du contrat",
                "Salaire"
            )
        } else {
            listOf(
                "Title",
                "Company name",
                "Start date",
                "End date",
                "Place",
                "Type",
                "Contract type",
                "Salary"
            )
        }

        if (titleValid) mapUser.add(Pair(listTag[0], title ?: ""))
        if (companyNameValid) mapUser.add(Pair(listTag[1], companyName ?: ""))
        if (dateStartValid) mapUser.add(Pair(listTag[2], dateStart ?: ""))
        if (dateEndValid) mapUser.add(Pair(listTag[3], dateEnd ?: ""))
        if (countryValid) mapUser.add(Pair(listTag[4], place ?: ""))
        if (typeValid) mapUser.add(Pair(listTag[5], type ?: ""))
        if (typeContractValid) mapUser.add(Pair(listTag[6], typeContract ?: ""))
        if (salaryValid) mapUser.add(Pair(listTag[7], "${salary ?: 1000}"))

        return mapUser
    }

}