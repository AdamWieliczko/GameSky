package pl.edu.uj.wieliczko.shopapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations.ProductDatabaseOperations
import pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations.ShoppingCartDatabaseOperations
import pl.edu.uj.wieliczko.shopapplication.realmModels.ProductRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getProducts
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getShoppingCart
import java.util.*

class ActivityProducts : AppCompatActivity() {

    val customerCart = LinkedList<ProductRealm>()
    lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        getProducts()
        nickname = intent.getStringExtra("UserName")!!
        findViewById<TextView>(R.id.NicknameProductsContainer).text = ("Witamy w sklepie " + nickname)
        getShoppingCart(nickname)
        val productID = ShoppingCartDatabaseOperations.getRealmShoppingCart(nickname)?.product
        if(productID != null) {
            val product = ProductDatabaseOperations.getRealmProduct(productID)
            if (product != null){
                customerCart.add(product)
            }
        }
    }

    fun goBack(view: View) {
        this.finish()
    }

    fun finalize(view: View) {
        val final = Intent(this, ActivityFinalizeShoppingCart::class.java)

        startActivity(final)
    }

    fun creator(view: View) {
        val creators = Intent(this, ActivityCreators::class.java).putExtra("Nickname", nickname)

        startActivity(creators)
    }
}