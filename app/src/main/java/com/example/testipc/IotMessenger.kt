package com.example.testipc

import android.os.Build
import android.util.Log
import com.amazonaws.mobileconnectors.iot.*
import com.eroad.security.awscert.EroadAWSAuthenticationFileRepository
import com.eroad.security.awscert.model.AWSIoTProvisioningResult
import java.util.*
import java.util.Queue

class IotMessenger : AWSIotMqttClientStatusCallback {
    private lateinit var mqttManager: AWSIotMqttManager
    private lateinit var authenticationRepository: EroadAWSAuthenticationFileRepository
    private var provisionResult: AWSIoTProvisioningResult? = null
    private var queue: Queue<String> = LinkedList()
    private var mqttConnected = false
    private var topic = ""
    private var isSendingMessage = false
    private var hasInit = false

    override fun onStatusChanged(
        status: AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus?,
        throwable: Throwable?
    ) {
        if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
            mqttConnected = true
            mqttManager.subscribeToTopic(topic, AWSIotMqttQos.QOS1, object: AWSIotMqttSubscriptionStatusCallback {
                override fun onSuccess() {
                    sendMessage()
                }

                override fun onFailure(exception: Throwable?) {
                    Log.e("IotMessenger", "Error occurred when subscribe the message exception: ${exception.toString()}")
                }

            }, object: AWSIotMqttNewMessageCallback {
                override fun onMessageArrived(topic: String?, data: ByteArray?) {
                    Log.d("IotMessenger", "message received: ${String(data!!)}")
                    sendMessage()
                }
            })
        }
    }

    fun addMessage(message: String) {
        Log.d("IotMessenger", "Adding message: $message")
        initInstances()
        queue.add(message)
        sendMessage()
    }

    private fun initInstances() {
        if (hasInit) return

        hasInit = true
        authenticationRepository = EroadAWSAuthenticationFileRepository.create()
        mqttManager = AWSIotMqttManager(Build.getSerial(), "a3un33h1bw83m5-ats.iot.ap-southeast-2.amazonaws.com")
        provisionResult = authenticationRepository.getDeviceProvisioningResult()
        if (provisionResult != null) {
            Log.d("IotMessenger", "Connecting MQTT")

            mqttManager.connect(provisionResult!!.keyStoreContainer.keyStore, this)
            topic = "%s/device/%s/diagnostic/test2".format(
                provisionResult!!.connectionInfo.environmentName,
                provisionResult!!.connectionInfo.thingName)
        }
    }

    private fun sendMessage() {
        if (mqttConnected == false || queue.isEmpty()) {
            isSendingMessage = false
            return
        }

        isSendingMessage = true
        val message = queue.poll()!!
        Log.d("IotMessenger", "Sending message: $message")
        mqttManager.publishString(message, topic, AWSIotMqttQos.QOS1)
    }

}