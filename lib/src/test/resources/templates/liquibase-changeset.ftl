    <changeSet author="${author}" id="${project}-${tableName}">
        <createTable tableName="${tableName}">
            <column autoIncrement="true" name="id" type="BIGINT">
                <constraints nullable="false" primaryKey="true"/>
            </column>
        </createTable>
    </changeSet>
