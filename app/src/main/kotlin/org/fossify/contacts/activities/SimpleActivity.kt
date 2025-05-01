package org.fossify.arfoxcontacts.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import org.fossify.commons.activities.BaseSimpleActivity
import org.fossify.commons.extensions.getColoredDrawableWithColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.helpers.KEY_PHONE
import org.fossify.arfoxcontacts.R
import org.fossify.arfoxcontacts.helpers.KEY_MAILTO
import org.fossify.arfoxcontacts.helpers.LOCATION_CONTACTS_TAB
import org.fossify.arfoxcontacts.helpers.LOCATION_FAVORITES_TAB

open class SimpleActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher,
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)

    override fun getRepositoryName() = "Contacts"

    protected fun getPhoneNumberFromIntent(intent: Intent): String? {
        if (intent.extras?.containsKey(KEY_PHONE) == true) {
            return intent.getStringExtra(KEY_PHONE)
        } else if (intent.extras?.containsKey("data") == true) {
            // sample contact number from Google Contacts:
            // data: [data1=+123 456 789 mimetype=vnd.android.cursor.item/phone_v2 _id=-1 data2=0]
            val data = intent.extras!!.get("data")
            if (data != null) {
                val contentValues = (data as? ArrayList<Any>)?.firstOrNull() as? ContentValues
                if (contentValues != null && contentValues.containsKey("data1")) {
                    return contentValues.getAsString("data1")
                }
            }
        }
        return null
    }

    protected fun getEmailFromIntent(intent: Intent): String? {
        return if (intent.dataString?.startsWith("$KEY_MAILTO:") == true) {
            Uri.decode(intent.dataString!!.substringAfter("$KEY_MAILTO:").trim())
        } else {
            null
        }
    }

    protected fun getTabIcon(position: Int): Drawable {
        val drawableId = when (position) {
            LOCATION_CONTACTS_TAB -> org.fossify.commons.R.drawable.ic_person_vector
            LOCATION_FAVORITES_TAB -> org.fossify.commons.R.drawable.ic_star_vector
            else -> org.fossify.commons.R.drawable.ic_people_vector
        }

        return resources.getColoredDrawableWithColor(drawableId, getProperTextColor())
    }

    protected fun getTabLabel(position: Int): String {
        val stringId = when (position) {
            LOCATION_CONTACTS_TAB -> org.fossify.commons.R.string.contacts_tab
            LOCATION_FAVORITES_TAB -> org.fossify.commons.R.string.favorites_tab
            else -> org.fossify.commons.R.string.groups_tab
        }

        return resources.getString(stringId)
    }
}
