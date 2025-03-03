package com.example.hanyarunrun.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hanyarunrun.data.AppDatabase
import com.example.hanyarunrun.data.ProfileDao
import com.example.hanyarunrun.data.ProfileEntity
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileDao: ProfileDao =
        AppDatabase.getDatabase(application).profileDao()

    fun getProfile(): LiveData<ProfileEntity?> {
        return profileDao.getProfile()
    }

    fun saveProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            if (profileDao.getProfile().value != null) {
                profileDao.update(profile)
            } else {
                profileDao.insert(profile)
            }
        }
    }


}
