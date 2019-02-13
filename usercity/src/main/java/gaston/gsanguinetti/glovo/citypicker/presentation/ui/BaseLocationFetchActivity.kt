package gaston.gsanguinetti.glovo.citypicker.presentation.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class BaseLocationFetchActivity : AppCompatActivity() {

    protected fun deliverUserCityResult(cityCode: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(CITY_CODE_EXTRA, cityCode)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {
        protected const val USER_LOCATION_REQUEST_CODE = 201
        protected const val CITY_CODE_EXTRA = "cityCodeExtra"

        fun isUserLocationRequestResult(requestCode: Int): Boolean {
            return requestCode == USER_LOCATION_REQUEST_CODE
        }

        fun isRequestCanceled(resultCode: Int) :Boolean {
            return resultCode == Activity.RESULT_CANCELED
        }

        fun getCityCodeFromResult(resultCode: Int, data: Intent?): String? =
            if (resultCode == Activity.RESULT_OK) data?.getStringExtra(CITY_CODE_EXTRA)
            else null

        fun startActivityForResult(activity: AppCompatActivity, childClass: Class<out AppCompatActivity>) {
            val intent = Intent(activity, childClass)
            activity.startActivityForResult(intent,
                USER_LOCATION_REQUEST_CODE
            )
        }
    }
}