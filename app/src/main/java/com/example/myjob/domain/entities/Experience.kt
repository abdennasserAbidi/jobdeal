package com.example.myjob.domain.entities

import android.view.View

data class Experience(
    var id: Int = View.generateViewId(),
    val title: String? = "",
    val companyName: String? = "",
    val dateStart: String? = "",
    val dateEnd: String? = "",
    val place: String? = "",
    val type: String? = "",
    val typeContract: String? = "",
    val salary: Int? = 1000,
    var idUser: Int? = 0
) {

    fun showExperience(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String> ()

        val titleValid = title?.isNotEmpty() == true
        val companyNameValid = !companyName.isNullOrEmpty()
        val countryValid = !place.isNullOrEmpty()
        val dateStartValid = dateStart != null && dateStart != "Choose Date"
        val dateEndValid = dateEnd != null && dateEnd != "Choose Date"
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
}