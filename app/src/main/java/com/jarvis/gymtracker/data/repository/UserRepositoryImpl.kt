package com.jarvis.gymtracker.data.repository

import com.jarvis.gymtracker.data.local.dao.UserDao
import com.jarvis.gymtracker.data.local.entity.UserEntity
import com.jarvis.gymtracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override fun getUser(): Flow<UserEntity?> = userDao.getUser()

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }
}