package com.ardritkrasniqi.prenotimi.model.data

interface IEvent : ITimeDuration {
    val clientName: String
    val commenti: String?
    val nrTel: String?

}
