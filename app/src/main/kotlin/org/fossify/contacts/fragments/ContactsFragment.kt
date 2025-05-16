package org.fossify.arfoxcontacts.fragments

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import org.fossify.commons.extensions.areSystemAnimationsEnabled
import org.fossify.commons.extensions.hideKeyboard
import org.fossify.commons.models.contacts.Contact
import org.fossify.arfoxcontacts.activities.EditContactActivity
import org.fossify.arfoxcontacts.activities.InsertOrEditContactActivity
import org.fossify.arfoxcontacts.activities.MainActivity
import org.fossify.arfoxcontacts.activities.SimpleActivity
import org.fossify.arfoxcontacts.adapters.ContactsAdapter
import org.fossify.arfoxcontacts.databinding.FragmentContactsBinding
import org.fossify.arfoxcontacts.databinding.FragmentLettersLayoutBinding
import org.fossify.arfoxcontacts.extensions.config
import org.fossify.arfoxcontacts.helpers.LOCATION_CONTACTS_TAB
import org.fossify.arfoxcontacts.interfaces.RefreshContactsListener

class ContactsFragment(context: Context, attributeSet: AttributeSet) : MyViewPagerFragment<MyViewPagerFragment.LetterLayout>(context, attributeSet) {

    private lateinit var binding: FragmentContactsBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = FragmentContactsBinding.bind(this)
        innerBinding = LetterLayout(FragmentLettersLayoutBinding.bind(binding.root))
    }

    override fun fabClicked() {
        activity?.hideKeyboard()
        Intent(context, EditContactActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override fun placeholderClicked() {
        if (activity is MainActivity) {
            (activity as MainActivity).showFilterDialog()
        } else if (activity is InsertOrEditContactActivity) {
            (activity as InsertOrEditContactActivity).showFilterDialog()
        }
    }

    fun setupContactsAdapter(contacts: List<Contact>) {
        setupViewVisibility(contacts.isNotEmpty())
        val currAdapter = innerBinding.fragmentList.adapter

        if (currAdapter == null || forceListRedraw) {
            forceListRedraw = false
            val location = LOCATION_CONTACTS_TAB

            ContactsAdapter(
                activity = activity as SimpleActivity,
                contactItems = contacts.toMutableList(),
                refreshListener = activity as RefreshContactsListener,
                location = location,
                removeListener = null,
                recyclerView = innerBinding.fragmentList,
                enableDrag = false,
            ) {
                (activity as RefreshContactsListener).contactClicked(it as Contact)
            }.apply {
                innerBinding.fragmentList.adapter = this
            }

            if (context.areSystemAnimationsEnabled) {
                innerBinding.fragmentList.scheduleLayoutAnimation()
            }
        } else {
            (currAdapter as ContactsAdapter).apply {
                startNameWithSurname = context.config.startNameWithSurname
                showPhoneNumbers = context.config.showPhoneNumbers
                showContactThumbnails = context.config.showContactThumbnails
                updateItems(contacts)
            }
        }
    }
}
