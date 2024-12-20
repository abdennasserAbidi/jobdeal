package com.example.myjob.domain.entities

class City {
    var name = ""
    var delegations = mutableListOf<Delegations>()
}

class Delegations {
    var name = ""
    var cities = mutableListOf<String>()
}