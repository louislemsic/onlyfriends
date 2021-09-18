package ph.com.onlyfriends.models.notifs

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitClient{
    companion object{
        private lateinit var retrofit: Retrofit
        fun getClient(url:String):Retrofit{
            retrofit=Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit
        }
    }
}