package tech.movies.app.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.movies.app.data.local.LocalDataSource
import tech.movies.app.data.remote.RemoteDataSource
import tech.movies.app.data.respositoryImpl.RepositoryImpl
import tech.movies.app.domain.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): Repository = RepositoryImpl(remoteDataSource, localDataSource)
}