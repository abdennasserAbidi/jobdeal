package com.example.myjob.domain.entities

import android.view.View
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Serializable
data class User(
    var id: Int? = View.generateViewId(),
    var firstName: String? = "",
    var lastName: String? = "",
    var phoneList: List<String>? = mutableListOf(),
    var addressList: List<String>? = mutableListOf(),
    var country: String? = "",
    var city: String? = "",
    var fcmToken: String? = "",
    var newCountry: String? = "",
    var bio: String? = "",
    var role: String? = "Company",
    var candidate: Boolean? = false,
    var company: Boolean? = true,
    var email: String? = "",
    var preferredWorkType: MutableList<String>? = mutableListOf(),
    var workPreferences: MutableList<String>? = mutableListOf(),
    var fullName: String? = "$firstName $lastName",
    var password: String? = "Aladin@123",
    var preferredEmploymentType: String? = "",
    var situation: String? = "",
    var isVerified: Boolean? = false,
    var firstTime: Boolean? = true,
    var firstTimeUse: Boolean? = true,
    var sexe: String? = "",
    var nationality: String? = "",
    var birthDate: String? = "Choose Date",
    var activitySector: String? = "Choose activity sector",
    var rangeSalary: String? = "",
    var preferredActivitySector: String? = "",

    var validationProfileStatus: ValidationProfileStatus = ValidationProfileStatus(),
    var professionalStatus: ProfessionalStatus? = ProfessionalStatus(),
    var candidateSkills: CandidateSkills? = CandidateSkills(),

    @SerializedName("experiences")
    var experience: MutableList<Experience>? = mutableListOf(),
    var education: MutableList<Educations>? = mutableListOf(),
    var announces: MutableList<AnnouncementModel>? = mutableListOf(),
    //company
    val invitations: MutableList<InvitationModel>? = mutableListOf(),
    val listNum: MutableList<String>? = mutableListOf(),
    var companyName: String? = "",
    var companyActivitySector: String? = "Informatique",
    var numSecuritySocial: String? = "",
    var docs: List<String>? = mutableListOf(),
    var faxCompany: String? = "",
    var linkWebsite: String? = "",
    var linkLinkedIn: String? = "",
    var companyDescription: String? = "",

    //Service
    var service: Boolean? = false,
    var paidUser: Boolean? = false,
    var countTrial: Int = 10,
    var userServiceName: String? = "",
    var category: ServiceCategory = ServiceCategory.IDLE,
    var otherCategory: String = "",
    var description: String = "",
) {

    private fun getMonthNumber(monthName: String): Int {
        return try {
            val date = SimpleDateFormat("MMMM", Locale.FRENCH).parse(monthName)
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.get(Calendar.MONTH) + 1
        } catch (e: Exception) {
            -1
        }
    }

    fun getYearsExp() {
        experience?.apply {
            if (isNotEmpty()) {
                val firstExp = this[0].dateStart
                firstExp?.let {
                    val arr = it.split(", ")
                    val year = arr[2].toInt()
                    val month = arr[1].split(" ")[1]
                    val monthNumber = getMonthNumber(month)

                    val calendar: Calendar = Calendar.getInstance()
                    val currentYear: Int = calendar.get(Calendar.YEAR)
                    val currentMonth: Int = calendar.get(Calendar.MONTH) + 1

                    val diffYear = currentYear - year
                    if (diffYear > 0 && currentMonth >= monthNumber) {
                        val diffMonth = currentMonth
                    } else {

                    }

                    println("aekddekapo  2 $currentMonth   $monthNumber")

                }
            }

        }

    }

    fun getSituation(lang: String): String {
        return if (lang == "French" || lang == "Français") {
            when (situation) {
                "Married" -> "Marrié"
                "Single" -> "Célibataire"
                "Engaged" -> "Engagé"
                else -> situation ?: ""
            }
        } else {
            when (situation) {
                "Marrié" -> "Married"
                "Célibataire" -> "Single"
                "Engagé" -> "Engaged"
                else -> situation ?: ""
            }
        }
    }

    fun changeSituation(name: String, lang: String): String {
        val traduction = if (lang == "French" || lang == "Français") {
            when (name) {
                "Married" -> "Marrié"
                "Single" -> "Célibataire"
                "Engaged" -> "Engagé"
                else -> name
            }
        } else {
            when (name) {
                "Marrié" -> "Married"
                "Célibataire" -> "Single"
                "Engagé" -> "Engaged"
                else -> name
            }
        }

        situation = traduction
        return traduction
    }

    fun getEmploymentType(lang: String): String {
        return if (lang == "French" || lang == "Français") {
            when (preferredEmploymentType) {
                "Contract" -> "Contrat"
                "Freelance" -> "Freelance"
                "Both" -> "Les deux"
                else -> preferredEmploymentType ?: ""
            }
        } else {
            when (preferredEmploymentType) {
                "Contrat" -> "Contract"
                "Freelance" -> "Freelance"
                "Les deux" -> "Both"
                else -> preferredEmploymentType ?: ""
            }
        }
    }

    fun changeEmploymentType(name: String, lang: String): String {
        val traduction = if (lang == "French" || lang == "Français") {
            when (name) {
                "Contract" -> "Contrat"
                "Freelance" -> "Freelance"
                "Both" -> "Les deux"
                else -> name
            }
        } else {
            when (name) {
                "Contrat" -> "Contract"
                "Freelance" -> "Freelance"
                "Les deux" -> "Both"
                else -> name
            }
        }

        preferredEmploymentType = traduction
        return traduction
    }

    fun getSex(lang: String): String {
        return if (lang == "French" || lang == "Français") {
            when (sexe) {
                "Male" -> "Homme"
                "Female" -> "Femme"
                else -> sexe ?: ""
            }
        } else {
            when (sexe) {
                "Homme" -> "Male"
                "Femme" -> "Female"
                else -> sexe ?: ""
            }
        }
    }

    fun changeSex(name: String, lang: String): String {
        val traduction = if (lang == "French" || lang == "Français") {
            when (name) {
                "Male" -> "Homme"
                "Female" -> "Femme"
                else -> name
            }
        } else {
            when (name) {
                "Homme" -> "Male"
                "Femme" -> "Female"
                else -> name
            }
        }

        sexe = traduction
        return traduction
    }

    fun changeAvailability(name: String, lang: String): String {
        professionalStatus?.apply {
            availability = if (lang == "French" || lang == "Français") {
                when (name) {
                    "Now" -> "Maintenant"
                    "Less than 3 months" -> "Avant 3 mois"
                    "In 3 months" -> "Dans 3 mois"
                    "More than 3 months" -> "Après 3 mois"
                    else -> name
                }
            } else {
                when (name) {
                    "Maintenant" -> "Now"
                    "Avant 3 mois" -> "Less than 3 months"
                    "Dans 3 mois" -> "In 3 months"
                    "Après 3 mois" -> "More than 3 months"
                    else -> name
                }
            }

            availability ?: ""
        }

        return ""
    }

    fun showUser(lang: String): Map<String, String> {
        val mapUser = hashMapOf<String, String>()

        val nameValid = fullName?.isNotEmpty() == true
        val addressValid = !addressList.isNullOrEmpty()
        val newCountryValid = !newCountry.isNullOrEmpty()
        val phoneValid = !phoneList.isNullOrEmpty()
        val countryValid = !country.isNullOrEmpty()
        val companyNameValid = !companyName.isNullOrEmpty()
        val nationalityValid = !nationality.isNullOrEmpty()
        val activitySectorValid =
            activitySector != null && activitySector != "Choose activity sector"
        val birthDateValid = birthDate != null && birthDate != "Choose Date"
        val availabilityValid = true
        val rangeSalaryValid = !rangeSalary.isNullOrEmpty()
        val sexValid = !sexe.isNullOrEmpty()
        val situationValid = !situation.isNullOrEmpty()
        val preferredActivitySectorValid = !preferredActivitySector.isNullOrEmpty()
        val preferredEmploymentTypeValid = !preferredEmploymentType.isNullOrEmpty()

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
                "Votre typle d'emploi",
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
                "Your employement type",
                "Phone",
                "Country",
                "New Country"
            )
        }

        if (nameValid) mapUser[listTag[0]] = fullName ?: ""
        if (addressValid) mapUser[listTag[1]] = addressList?.joinToString { "\n" } ?: ""
        if (activitySectorValid) mapUser[listTag[2]] = activitySector ?: ""
        if (companyNameValid) mapUser[listTag[3]] = companyName ?: ""
        if (birthDateValid) mapUser[listTag[4]] = birthDate ?: ""
        if (nationalityValid) mapUser[listTag[5]] = nationality ?: ""

        if (sexValid) mapUser[listTag[6]] = sexe ?: ""
        if (situationValid) mapUser[listTag[7]] = situation ?: ""

        if (availabilityValid) mapUser[listTag[8]] = professionalStatus?.availability ?: ""
        if (rangeSalaryValid) mapUser[listTag[9]] = rangeSalary ?: ""
        if (preferredActivitySectorValid) mapUser[listTag[10]] = preferredActivitySector ?: ""
        if (preferredEmploymentTypeValid) mapUser[listTag[11]] = preferredEmploymentType ?: ""
        if (phoneValid) mapUser[listTag[12]] = phoneList?.joinToString { "\n" } ?: ""
        if (countryValid) mapUser[listTag[13]] = country ?: ""
        if (newCountryValid) mapUser[listTag[14]] = newCountry ?: ""

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