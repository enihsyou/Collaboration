package com.enihsyou.collaboration.api

typealias EmailAddress = String
typealias Username = String
typealias Password = String
typealias InviteToken = String
typealias UserIdentifier = Long


interface ICoGroup {
    fun listGroups()
    fun createGroup()
    fun deleteGroup()
    fun listIndividuals()
    fun createIndividual()
}

interface ICoCabinet {
    fun listCabinets()
    fun createCabinet()
    fun deleteCabinet()

    fun listPads()
    fun createCabinetPad()
    fun deletePad()
}
