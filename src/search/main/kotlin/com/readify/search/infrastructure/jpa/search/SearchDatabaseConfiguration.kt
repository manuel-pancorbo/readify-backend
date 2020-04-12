package com.readify.search.infrastructure.jpa.search

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
    basePackages = ["com.readify.search.infrastructure.jpa.search"],
    entityManagerFactoryRef = "searchEntityManagerFactory",
    transactionManagerRef = "searchTransactionManager"
)
class SearchDatabaseConfiguration {
    @Bean("searchEntityManagerFactory")
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("searchDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.readify.search.infrastructure.jpa.search")
            .persistenceUnit("search")
            .build()

    @Bean("searchDataSource")
    @ConfigurationProperties(prefix = "spring.search")
    fun userDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("searchTransactionManager")
    fun userTransactionManager(
        @Qualifier("searchEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
