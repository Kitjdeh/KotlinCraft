package com.ncc.kotlincraft.data.mapper

import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.domain.model.DomainRecord

fun mapperDomainToData(records: DomainRecord): Record {
    return Record(
        records.id,
        records.expression
    )

}

fun mapperDataToDomain(records: List<Record>): List<DomainRecord> {
    return records.toList().map {
        DomainRecord(
            it.id,
            it.expression
        )
    }
}