package org.fossify.arfoxcontacts.fragments

import android.content.Context
import android.util.AttributeSet
import org.fossify.commons.helpers.TAB_GROUPS
import org.fossify.arfoxcontacts.activities.MainActivity
import org.fossify.arfoxcontacts.activities.SimpleActivity
import org.fossify.arfoxcontacts.databinding.FragmentGroupsBinding
import org.fossify.arfoxcontacts.databinding.FragmentLayoutBinding
import org.fossify.arfoxcontacts.dialogs.CreateNewGroupDialog

class GroupsFragment(context: Context, attributeSet: AttributeSet) : MyViewPagerFragment<MyViewPagerFragment.FragmentLayout>(context, attributeSet) {

    private lateinit var binding: FragmentGroupsBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = FragmentGroupsBinding.bind(this)
        innerBinding = FragmentLayout(FragmentLayoutBinding.bind(binding.root))
    }

    override fun fabClicked() {
        finishActMode()
        showNewGroupsDialog()
    }

    override fun placeholderClicked() {
        showNewGroupsDialog()
    }

    private fun showNewGroupsDialog() {
        CreateNewGroupDialog(activity as SimpleActivity) {
            (activity as? MainActivity)?.refreshContacts(TAB_GROUPS)
        }
    }
}
