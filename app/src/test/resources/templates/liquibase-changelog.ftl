<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd">
    <changeSet author="${author}" id="${project}-${tableName}">
        <createTable tableName="${tableName}">
            <column autoIncrement="true" name="id" type="BIGINT">
                <constraints nullable="false" primaryKey="true"/>
            </column>
        </createTable>
    </changeSet>
</databaseChangeLog>