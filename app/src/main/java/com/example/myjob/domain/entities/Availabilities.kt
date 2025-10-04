package com.example.myjob.domain.entities

import android.view.View
import com.example.myjob.R

open class Choices

data class ParentChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class InvitationChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class Availabilities(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class ExperienceChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class CategoryChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.type1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class ContractTypeChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class SituationChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()

data class SexChoices(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var titleString: String = "",
    var isSelected: Boolean = false
): Choices()