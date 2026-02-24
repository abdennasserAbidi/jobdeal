package com.example.myjob.domain.entities

import FreelanceSector
import FreelanceService
import com.example.myjob.feature.demands.ServiceCategory

data class CategoryModel(
    var listCategories: MutableList<ServiceCategory> = mutableListOf(),
    var listService: MutableList<FreelanceService> = mutableListOf(),
    var listSector: MutableList<FreelanceSector> = mutableListOf()
)