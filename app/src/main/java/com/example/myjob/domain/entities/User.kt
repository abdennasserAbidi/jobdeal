package com.example.myjob.domain.entities

import android.util.Log
import android.view.View
import com.google.gson.annotations.SerializedName
import java.util.Calendar

data class User(
    var id: Int? = View.generateViewId(),
    var firstName: String? = "",
    var lastName: String? = "",
    var phone: String? = "",
    var country: String? = "",
    var newCountry: String? = "",
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
    var userExperience: String? = "",
    @SerializedName("experiences")
    var experience: MutableList<Experience>? = mutableListOf(),
    var education: MutableList<Educations>? = mutableListOf(),
    //company
    val listNum: MutableList<String>? = mutableListOf(),
    var companyName: String? = "",
    var phoneCompany: String? = "",
    var secondPhoneCompany: String? = "",
    var faxCompany: String? = "",
    var linkWebsite: String? = "",
    var linkLinkedIn: String? = "",
    var companyActivitySector: String? = "",
    var companyDescription: String? = "",
    var companyAddress: String? = "",
    var companySecondAddress: String? = "",
) {

    fun changeUserExperience() {
        experience?.apply {
            userExperience = when(size) {
                0-2 -> ""
                2-5 -> ""
                6-10 -> ""
                else -> ""
            }
        }

    }

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

    fun changeSituation(name: String, lang: String): String {
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
        return traduction
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

    fun changeSex(name: String, lang: String): String {
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
        return traduction
    }

    fun showUser(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String> ()

        val nameValid = fullName?.isNotEmpty() == true
        val addressValid = !address.isNullOrEmpty()
        val newCountryValid = !newCountry.isNullOrEmpty()
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
                "Téléphone",
                "Pays",
                "New Country"
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
                "Phone",
                "Country",
                "New Country"
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
        if (newCountryValid) mapUser[listTag[13]] = newCountry ?: ""

        return mapUser
    }

    fun showUserList(lang: String): List<Pair<String, String>> {
        val mapUser = mutableListOf<Pair<String, String>> ()

        val nameValid = fullName?.isNotEmpty() == true
        val addressValid = !address.isNullOrEmpty()
        val newCountryValid = !newCountry.isNullOrEmpty()
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
                "Téléphone",
                "Pays",
                "New Country"
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
                "Phone",
                "Country",
                "New Country"
            )
        }

        if (nameValid) mapUser.add(Pair(listTag[0], fullName ?: ""))
        if (addressValid) mapUser.add(Pair(listTag[1], address ?: ""))
        if (activitySectorValid) mapUser.add(Pair(listTag[2], activitySector ?: ""))
        if (companyNameValid) mapUser.add(Pair(listTag[3], companyName ?: ""))
        if (birthDateValid) mapUser.add(Pair(listTag[4], birthDate ?: ""))
        if (nationalityValid) mapUser.add(Pair(listTag[5], nationality ?: ""))
        if (sexValid) mapUser.add(Pair(listTag[6], sexe ?: ""))
        if (situationValid) mapUser.add(Pair(listTag[7], situation ?: ""))
        if (availabilityValid) mapUser.add(Pair(listTag[8], availability ?: ""))
        if (rangeSalaryValid) mapUser.add(Pair(listTag[9], rangeSalary ?: ""))
        if (preferredActivitySectorValid) mapUser.add(Pair(listTag[10], preferredActivitySector ?: ""))
        if (phoneValid) mapUser.add(Pair(listTag[11], phone ?: ""))
        if (countryValid) mapUser.add(Pair(listTag[12], country ?: ""))
        if (newCountryValid) mapUser.add(Pair(listTag[13], newCountry ?: ""))

        return mapUser
    }
    fun resumeUser(): String {
        var resume = ""

        val addressValid = !address.isNullOrEmpty()
        val phoneValid = !phone.isNullOrEmpty()
        val countryValid = !country.isNullOrEmpty()
        val nationalityValid = !nationality.isNullOrEmpty()
        val birthDateValid = birthDate != null && birthDate != "Choose Date"
        val availabilityValid = !availability.isNullOrEmpty()
        val rangeSalaryValid = !rangeSalary.isNullOrEmpty()
        val sexValid = !sexe.isNullOrEmpty()
        val situationValid = !situation.isNullOrEmpty()
        val preferredActivitySectorValid = !preferredActivitySector.isNullOrEmpty()

        val t = if (sexe == "Male") "he" else "she"
        val t1 = if (sexe == "Male") "him" else "her"
        val t2 = if (sexe == "Male") "his" else "her"

        if (birthDateValid) {
            val year = birthDate?.split(",")?.get(2)?.trimStart()?.trimEnd()?.toInt() ?: 0
            val age = Calendar.getInstance().get(Calendar.YEAR) - year
            resume += "$t is $age years old "
        }
        if (nationalityValid) resume += "${nationality}n"
        if (situationValid) resume += "${situation}, "
        if (addressValid) resume += "$t situated in $address "
        if (countryValid) resume += "${country}, "
        if (preferredActivitySectorValid) resume += "$t preferred working in $preferredActivitySector, "
        if (rangeSalaryValid) resume += "$t wants a salary range between $rangeSalary, "
        if (availabilityValid) resume += "$t is available ${availability}, "
        resume += "you can contact $t1 via $t2 email : $email "
        if (phoneValid) resume += "or on $t2 phone $phone"

        Log.i("resume", "resumeUser: $resume")

        return resume
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