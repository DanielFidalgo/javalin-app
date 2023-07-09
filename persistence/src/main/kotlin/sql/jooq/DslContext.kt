package sql.jooq

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

class DslContext constructor(private val writerDS: DataSource, private val readerDS: DataSource, private val dialect: SQLDialect) {

    val writer: DSLContext = DSL.using(writerDS, dialect)
    val reader: DSLContext = DSL.using(readerDS, dialect)
}