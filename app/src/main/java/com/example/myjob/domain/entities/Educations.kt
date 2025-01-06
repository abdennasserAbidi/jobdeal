package com.example.myjob.domain.entities

import android.view.View

data class Educations(
    var id: Int = View.generateViewId(),
    val title: String? = "",
    val schoolName: String? = "",
    val degree: String? = "",
    val fieldStudy: String? = "",
    val dateStart: String? = "",
    val dateEnd: String? = "",
    val place: String? = "",
    val grade: String? = "",
    val description: String? = "",
    val stillStudying: Boolean? = true,
    var idUser: Int? = 0
) {

    fun showEducation(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String> ()

        val titleValid = title?.isNotEmpty() == true
        val schoolNameValid = !schoolName.isNullOrEmpty()
        val countryValid = !place.isNullOrEmpty()
        val dateStartValid = dateStart != null && dateStart != "Choose Date"
        val dateEndValid = dateEnd != null && dateEnd != "Choose Date" && dateEnd.isNotEmpty()
        val degreeValid = !degree.isNullOrEmpty()
        val fieldStudyValid = !fieldStudy.isNullOrEmpty()
        val gradeValid = !grade.isNullOrEmpty()
        val descriptionValid = !description.isNullOrEmpty()


        val listTag = if (lang == "French" || lang == "Français") {
            listOf(
                "Titre",
                "Nom de l'école",
                "Date de début",
                "Date de fin",
                "Emplacement",
                "Field of study",
                "Degree",
                "Grade",
                "Description"
            )
        } else {
            listOf(
                "Title",
                "School name",
                "Start date",
                "End date",
                "Place",
                "Field of study",
                "Degree",
                "Grade",
                "Description"
            )
        }



        if (titleValid) mapUser[listTag[0]] = title ?: ""
        if (schoolNameValid) mapUser[listTag[1]] = schoolName ?: ""
        if (dateStartValid) mapUser[listTag[2]] = dateStart ?: ""
        if (dateEndValid) mapUser[listTag[3]] = dateEnd ?: ""
        if (countryValid) mapUser[listTag[4]] = place ?: ""
        if (degreeValid) mapUser[listTag[5]] = degree ?: ""
        if (fieldStudyValid) mapUser[listTag[6]] = fieldStudy ?: ""
        if (gradeValid) mapUser[listTag[7]] = grade ?: ""
        if (descriptionValid) mapUser[listTag[8]] = description ?: ""

        return mapUser
    }
}