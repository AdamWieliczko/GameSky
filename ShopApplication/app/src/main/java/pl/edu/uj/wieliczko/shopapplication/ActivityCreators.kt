package pl.edu.uj.wieliczko.shopapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import pl.edu.uj.wieliczko.shopapplication.services.getShoppingCartService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityCreators : AppCompatActivity() {
    lateinit var secret: String
    lateinit var googlePayButton: Button
    lateinit var googlePayLauncher: GooglePayLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nickname = intent.getStringExtra("Nickname")!!

        setContentView(R.layout.activity_creators)

        runBlocking(Dispatchers.IO) {
            getPaymentIntent(nickname)
        }

        PaymentConfiguration.init(applicationContext,
            pk)
        googlePayButton = findViewById<Button>(R.id.donateButton)
        googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "PL",
                merchantName = "ShopApplication"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )
    }

    fun onGooglePayReady(isReady: Boolean) {
        googlePayButton!!.isEnabled = isReady
    }

    fun onGooglePayResult(result: GooglePayLauncher.Result) {
        when (result) {
            GooglePayLauncher.Result.Completed -> {
                Toast.makeText(this, "Płatność zakończona powodzeniem, dziękujemy!", Toast.LENGTH_SHORT).show()
            }
            GooglePayLauncher.Result.Canceled -> {
                Toast.makeText(this, "Niestety płatność zostałą anulowana, proszę powtórzyć", Toast.LENGTH_SHORT).show()
            }
            is GooglePayLauncher.Result.Failed -> {
                Toast.makeText(this, "Płatność zakończona niepowodzeniem, proszę powtórzyć", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goBack(view: View) {
        this.finish()
    }

    fun donate(view: View) {
        googlePayLauncher.presentForPaymentIntent(secret)
    }

    fun getPaymentIntent(nickname: String) {
        getShoppingCartService()
            .getPaymentIntentService(nickname)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == 200) {
                        Log.d("Payment Intent", response.body().toString())
                        secret = response.body().toString()
                    }
                    else {
                        wrongCode("Payment Intent")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("Payment Intent got following problem ", t.message.toString())
                }
            })
    }
}