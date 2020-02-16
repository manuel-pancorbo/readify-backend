package com.readify.userprofile.infrastructure.jpa.user

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
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
    basePackages = [ "com.readify.userprofile.infrastructure.jpa.user" ],
    entityManagerFactoryRef = "entityManagerFactory"
)
class UserProfileDatabaseConfiguration {
    @Primary
    @Bean("entityManagerFactory")
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("userDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.readify.userprofile.infrastructure.jpa.user")
            .persistenceUnit("userprofile")
            .build()

    @Bean("userDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun userDataSource(): DataSource = DataSourceBuilder.create().build()

    @Primary
    @Bean("transactionManager")
    fun userTransactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
