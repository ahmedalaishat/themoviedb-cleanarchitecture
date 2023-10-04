package com.alaishat.ahmed.themoviedb.datasource.impl.connection.datasource

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.alaishat.ahmed.themoviedb.datasource.source.connection.datasource.ConnectionDataSource
import com.alaishat.ahmed.themoviedb.datasource.source.connection.model.ConnectionStateDataModel
import com.alaishat.ahmed.themoviedb.datasource.source.connection.model.ConnectionStateDataModel.Connected
import com.alaishat.ahmed.themoviedb.datasource.source.connection.model.ConnectionStateDataModel.Disconnected
import com.alaishat.ahmed.themoviedb.datasource.source.connection.model.ConnectionStateDataModel.Unset
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Ahmed Al-Aishat on Sep/16/2023.
 * The Movie DB Project.
 */
class ConnectionDataSourceImpl(
    private val connectivityManager: ConnectivityManager,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
) : ConnectionDataSource {
    private val stateFlow = MutableStateFlow<ConnectionStateDataModel>(Unset)

    private val networkRequestProvider: () -> NetworkRequest = {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            emitConnectionUpdate(Connected(backOnline = stateFlow.value == Disconnected))
        }

        override fun onLost(network: Network) {
            emitConnectionUpdate(
                if (connectivityManager.isConnected()) {
                    Connected(backOnline = stateFlow.value == Disconnected)
                } else {
                    Disconnected
                }
            )
        }
    }

    init {
        emitConnectionUpdate(
            connectionState = if (connectivityManager.isConnected())
                Connected(backOnline = false)
            else Disconnected
        )
    }

    override fun observeIsConnected(): Flow<ConnectionStateDataModel> {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (ignore: IllegalArgumentException) {
        }
        connectivityManager.registerNetworkCallback(networkRequestProvider(), networkCallback)

        return stateFlow
    }

    override fun getConnectionState(): ConnectionStateDataModel = stateFlow.value

    private fun emitConnectionUpdate(connectionState: ConnectionStateDataModel) {
        Timber.i("ConnectivityStatus: $connectionState")
        coroutineScope.launch(ioDispatcher) {
            stateFlow.emit(connectionState)
        }
    }

    private fun ConnectivityManager.isConnected() = activeNetwork
        ?.let(::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false
}
