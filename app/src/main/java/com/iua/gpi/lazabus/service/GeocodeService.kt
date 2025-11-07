package com.iua.gpi.lazabus.service
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.iua.gpi.lazabus.service.interf.GeocodeServiceI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Singleton

@Singleton
class GeocodeService (
    private val context: Context
) : GeocodeServiceI {

    override suspend fun getCoordinatesForLocation(locationName: String): Result<Address> {
        return withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses: List<Address>? = geocoder.getFromLocationName(locationName, 1)

                if (addresses.isNullOrEmpty()) {
                    Result.failure(NoSuchElementException("No se encontraron coordenadas para '$locationName'."))
                } else {
                    Result.success(addresses[0])
                }
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}