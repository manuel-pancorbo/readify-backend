package com.readify.authentication.infrastructure.jpa.usercredentials

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
    basePackages = [ "com.readify.authentication.infrastructure.jpa.usercredentials" ],
    entityManagerFactoryRef = "authenticationEntityManagerFactory",
    transactionManagerRef = "authenticationTransactionManager"
)
class UserCredentialsDatabaseConfiguration {
    @Bean("authenticationEntityManagerFactory")
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("authenticationDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.readify.authentication.infrastructure.jpa.usercredentials")
            .persistenceUnit("authentication")
            .build()

    @Bean("authenticationDataSource")
    @ConfigurationProperties(prefix = "spring.authentication")
    fun userDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("authenticationTransactionManager")
    fun userTransactionManager(
        @Qualifier("authenticationEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
