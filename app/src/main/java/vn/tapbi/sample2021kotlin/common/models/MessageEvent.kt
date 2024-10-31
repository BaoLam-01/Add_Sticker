package vn.tapbi.sample2021kotlin.common.models


class MessageEvent {
    var typeEvent = 0
    var stringValue = ""

    constructor(typeEvent: Int, stringValue: String) {
        this.typeEvent = typeEvent
        this.stringValue = stringValue
    }

    constructor(typeEvent: Int) {
        this.typeEvent = typeEvent
    }
}