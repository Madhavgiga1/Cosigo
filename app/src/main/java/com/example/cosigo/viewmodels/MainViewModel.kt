package com.example.cosigo.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cosigo.data.Repository
import com.example.cosigo.models.Basic
import com.example.cosigo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,application:Application
):AndroidViewModel(application)
{

    var profileResponse:MutableLiveData<NetworkResult<Basic>> = MutableLiveData()

    fun getProfileData()=viewModelScope.launch {
        getProfileDataSafely()
    }

    private suspend fun getProfileDataSafely() {
        profileResponse.value=NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response=repository.remote.getData()
                profileResponse.value=handleProfileResponse(response)
            }catch (e:Exception) {
                Log.d("error1", e.toString())
                profileResponse.value = NetworkResult.Error("Error getting profile")
            }
        }
        else{
            Log.d("internetERROR", "No Internet connection")
            profileResponse.value=NetworkResult.Error("No internet Connection")
        }
    }
    private fun handleProfileResponse(response: Response<Basic>): NetworkResult<Basic>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }



    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork=connectivityManager.activeNetwork?:return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}