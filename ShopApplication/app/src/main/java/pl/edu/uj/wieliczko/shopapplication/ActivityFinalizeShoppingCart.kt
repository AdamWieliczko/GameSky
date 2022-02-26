package pl.edu.uj.wieliczko.shopapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ActivityFinalizeShoppingCart : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_shopping_cart)
    }

    fun goBack(view: View) {
        this.finish()
    }
}