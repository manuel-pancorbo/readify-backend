package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

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
    basePackages = ["com.readify.bookpublishing.infrastructure.jpa.bookpublishing"],
    entityManagerFactoryRef = "bookPublishingEntityManagerFactory",
    transactionManagerRef = "bookPublishingTransactionManager"
)
class BookPublishingDatabaseConfiguration {
    @Bean("bookPublishingEntityManagerFactory")
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("bookPublishingDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.readify.bookpublishing.infrastructure.jpa.bookpublishing")
            .persistenceUnit("bookpublishing")
            .build()

    @Bean("bookPublishingDataSource")
    @ConfigurationProperties(prefix = "spring.bookpublishing")
    fun userDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("bookPublishingTransactionManager")
    fun userTransactionManager(
        @Qualifier("bookPublishingEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
