package com.readify.readerlibrary.infrastructure.jpa.readerlibrary

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.readify.readerlibrary.infrastructure.jpa.readerlibrary"],
    entityManagerFactoryRef = "readerLibraryEntityManagerFactory",
    transactionManagerRef = "readerLibraryTransactionManager"
)
class ReaderLibraryDatabaseConfiguration {
    @Bean("readerLibraryEntityManagerFactory")
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("readerLibraryDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.readify.readerlibrary.infrastructure.jpa.readerlibrary")
            .persistenceUnit("readerlibrary")
            .build()

    @Bean("readerLibraryDataSource")
    @ConfigurationProperties(prefix = "spring.readerlibrary")
    fun userDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("readerLibraryTransactionManager")
    fun userTransactionManager(
        @Qualifier("readerLibraryEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
