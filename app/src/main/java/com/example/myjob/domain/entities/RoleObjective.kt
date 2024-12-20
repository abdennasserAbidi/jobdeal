package com.example.myjob.domain.entities

import com.example.myjob.R

data class RoleObjective(
    var id: Int = 0,
    var title: Int = R.string.availability_text,
    var response: Int = R.string.not_specified_text
)

val DEFAULT_ROLE = listOf(
    RoleObjective(1, R.string.availability_text, R.string.response_role_text),
    RoleObjective(2, R.string.work_type_text, R.string.not_specified_text),
    RoleObjective(3, R.string.location_role_text, R.string.not_specified_text),
    RoleObjective(4, R.string.salary_role_text, R.string.not_specified_text),
    RoleObjective(5, R.string.interest_role_text, R.string.not_specified_text),
    RoleObjective(5, R.string.right_role_text, R.string.not_specified_text),
)