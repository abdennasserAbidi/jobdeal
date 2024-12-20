package com.example.myjob.domain.entities

import android.view.View

data class User(
    var id: Int? = View.generateViewId(),
    var firstName: String? = "",
    var lastName: String? = "",
    var companyName: String? = "",
    var phone: String? = "",
    var country: String? = "",
    var role: String? = "Choose type of user",
    var email: String? = "abidi.baha@gmail.com",
    var fullName: String? = "$firstName $lastName",
    var password: String? = "Aladin@123",
    var address: String? = "",
    var situation: String? = "",
    var sexe: String? = "",
    var nationality: String? = "",
    var birthDate: String? = "Choose Date",
    var activitySector: String? = "Choose activity sector",
    var availability: String? = "",
    var rangeSalary: String? = "",
    var preferredActivitySector: String? = "",
    var experience: MutableList<Experience>? = mutableListOf(),
    var education: MutableList<Educations>? = mutableListOf()
) {

    fun getSituation(lang: String): String {
        return if (lang == "French" || lang == "Français") {
            when(situation) {
                "Married" -> "Marrié"
                "Single" -> "Célibataire"
                "Engaged" -> "Engagé"
                else -> situation?: ""
            }
        } else {
            when(situation) {
                "Marrié" -> "Married"
                "Célibataire" -> "Single"
                "Engagé" -> "Engaged"
                else -> situation?: ""
            }
        }
    }

    fun changeSituation(name: String, lang: String) {
        val traduction = if (lang == "French" || lang == "Français") {
            when(name) {
                "Married" -> "Marrié"
                "Single" -> "Célibataire"
                "Engaged" -> "Engagé"
                else -> name
            }
        } else {
            when(name) {
                "Marrié" -> "Married"
                "Célibataire" -> "Single"
                "Engagé" -> "Engaged"
                else -> name
            }
        }

        situation = traduction
    }

    fun getSex(lang: String): String {
        return if (lang == "French" || lang == "Français") {
            when(sexe) {
                "Male" -> "Homme"
                "Female" -> "Femme"
                else -> sexe ?: ""
            }
        } else {
            when(sexe) {
                "Homme" -> "Male"
                "Femme" -> "Female"
                else -> sexe ?: ""
            }
        }
    }

    fun changeSex(name: String, lang: String) {
        val traduction = if (lang == "French" || lang == "Français") {
            when(name) {
                "Male" -> "Homme"
                "Female" -> "Femme"
                else -> name
            }
        } else {
            when(name) {
                "Homme" -> "Male"
                "Femme" -> "Female"
                else -> name
            }
        }

        sexe = traduction
    }

    fun showUser(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String> ()

        val nameValid = fullName?.isNotEmpty() == true
        val addressValid = !address.isNullOrEmpty()
        val phoneValid = !phone.isNullOrEmpty()
        val countryValid = !country.isNullOrEmpty()
        val companyNameValid = !companyName.isNullOrEmpty()
        val nationalityValid = !nationality.isNullOrEmpty()
        val activitySectorValid = activitySector != null && activitySector != "Choose activity sector"
        val birthDateValid = birthDate != null && birthDate != "Choose Date"
        val availabilityValid = !availability.isNullOrEmpty()
        val rangeSalaryValid = !rangeSalary.isNullOrEmpty()
        val sexValid = !sexe.isNullOrEmpty()
        val situationValid = !situation.isNullOrEmpty()
        val preferredActivitySectorValid = !preferredActivitySector.isNullOrEmpty()

        val listTag = if (lang == "French" || lang == "Français") {
            listOf(
                "Nom et prénom",
                "Adresse",
                "Sécteur d'activity",
                "Nom de la societé",
                "Date de naissance",
                "Nationalité",
                "Sexe",
                "Situation",
                "Disponibilité",
                "Marge salariale",
                "Votre sécteur d'activité",
            )
        } else {
            listOf(
                "Full name",
                "Address",
                "Activity sector",
                "Company name",
                "Birth date",
                "Nationality",
                "Sex",
                "Situation",
                "Availability",
                "Salary range",
                "Your activity sector",
            )
        }

        if (nameValid) mapUser[listTag[0]] = fullName ?: ""
        if (addressValid) mapUser[listTag[1]] = address ?: ""
        if (activitySectorValid) mapUser[listTag[2]] = activitySector ?: ""
        if (companyNameValid) mapUser[listTag[3]] = companyName ?: ""
        if (birthDateValid) mapUser[listTag[4]] = birthDate ?: ""
        if (nationalityValid) mapUser[listTag[5]] = nationality ?: ""

        if (sexValid) mapUser[listTag[6]] = sexe ?: ""
        if (situationValid) mapUser[listTag[7]] = situation ?: ""

        if (availabilityValid) mapUser[listTag[8]] = availability ?: ""
        if (rangeSalaryValid) mapUser[listTag[9]] = rangeSalary ?: ""
        if (preferredActivitySectorValid) mapUser[listTag[10]] = preferredActivitySector ?: ""
        if (phoneValid) mapUser[listTag[11]] = phone ?: ""
        if (countryValid) mapUser[listTag[12]] = country ?: ""

        return mapUser
    }

}

val DEFAULT_USER = listOf(
    User(firstName = "Ala", lastName = "abidi"),
    User(firstName = "grzgrzgzr", lastName = "fsfrezfe"),
    User(firstName = "vsrazrrz", lastName = "grezggtr"),
    User(firstName = "Ala", lastName = "yyyyyyy"),
    User(firstName = "Ala", lastName = "ggggggggg"),
    User(firstName = "Ala", lastName = "cccccc"),
    User(firstName = "Ala", lastName = "eeeeeee"),
    User(firstName = "Ala", lastName = "ttttttt"),
    User(firstName = "Ala", lastName = "gghhyytt"),
    User(firstName = "Ala", lastName = "hhtrrreeee"),
    User(firstName = "Ala", lastName = "huyttrtttrt"),
    User(firstName = "Ala", lastName = "qqqqqqq"),
    User(firstName = "Ala", lastName = "qqqqdsvgergr"),
    User(firstName = "Ala", lastName = "yyyrreee"),
    User(firstName = "Ala", lastName = "bbbbbb"),
    User(firstName = "Ala", lastName = "xxxxxxxx"),
    User(firstName = "Ala", lastName = "wwwwwww"),
    User(firstName = "Ala", lastName = "kkkkkkk"),
    User(firstName = "Ala", lastName = "mmmmmmmmm"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp"),
    User(firstName = "Ala", lastName = "pppppppp")
)