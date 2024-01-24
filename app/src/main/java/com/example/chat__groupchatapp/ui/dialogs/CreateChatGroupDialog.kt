package com.example.chat__groupchatapp.ui.dialogs

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.DialogFragment
import com.example.chat__groupchatapp.R
import com.example.chat__groupchatapp.Utils.Widgets.BounceButton
import com.example.chat__groupchatapp.Utils.showToast
import com.example.chat__groupchatapp.data.remote.model.user.response.UserEntity
import com.example.chat__groupchatapp.databinding.CreateGroupDialogBinding
import com.example.chat__groupchatapp.databinding.ItemsLayoutsBinding
import com.example.chat__groupchatapp.domain.model.UserSpinnerItem
import com.example.chat__groupchatapp.ui.adapter.UserSpinnerAdapter

class CreateChatGroupDialog(var ownerId : String, val membersList: MutableList<UserEntity>, val onCreateGroup: (String?, String?, String?, ArrayList<String>?,) -> Unit) : DialogFragment() {

    lateinit var binding : CreateGroupDialogBinding

    var selectedMembersList = arrayListOf<String>()

    val userSpinnerAdapter : UserSpinnerAdapter? by lazy {
        UserSpinnerAdapter(onCheckedItem = { item, isChecked ->
            val mList = userSpinnerAdapter?.currentList?.toMutableList()
            val nList= mList?.map {
                if(it.userId == item.userId){
                    it.isSelected = isChecked
                    it
                }else{
                    it
                }
            }
            userSpinnerAdapter?.submitList(nList)

            selectedMembersList.clear()
            nList?.forEach {
                if(it.isSelected)
                selectedMembersList.add(it.userId.toString())
            }
        })
    }

    companion object{
        val TAG = "CreateChatGroupDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateGroupDialogBinding.inflate(layoutInflater)
        binding.doneBtn.setContent {
            BounceButton(buttonText = "Create") {
                val grpName = binding.groupNameEdttxt.text?.toString()
                val grpOwner = binding.groupOwnerEdttxt.text?.toString()
                val grpDesc = binding.descriptionEdttxt.text?.toString()

                if(grpName == null || grpOwner == null || grpDesc == null || selectedMembersList.isEmpty()){
                    requireContext().showToast(message = "Invalid data..")
                }else{
                    onCreateGroup(grpName, grpOwner, grpDesc , selectedMembersList)
                    requireDialog()?.dismiss()

                }
            }
        }

        binding.allMembersPopUpedttxt.setOnClickListener {

            val bindingspinner = ItemsLayoutsBinding.inflate(layoutInflater)
            val popupWindow = PopupWindow(bindingspinner.root,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true)
            popupWindow.contentView = bindingspinner.root
            popupWindow.animationStyle = android.R.style.Animation_Dialog
            popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.dialog_curved_background,null))



            bindingspinner.userIDsRecyclerView.adapter = userSpinnerAdapter
            val userSpinnerItemList = membersList.map { UserSpinnerItem(userId = it.username) }

            Log.d("fvfkmfkvf",userSpinnerAdapter?.currentList?.size.toString())
            userSpinnerAdapter?.currentList?.forEach {
                Log.d("fvknfkvnf",it.toString())
            }
            if(userSpinnerAdapter?.currentList?.isEmpty() == true) {
                userSpinnerAdapter?.submitList(userSpinnerItemList)
            }else{
                userSpinnerAdapter?.submitList(userSpinnerAdapter?.currentList)
            }
            popupWindow.width = binding.allMembersPopUpedttxt.width
            popupWindow.showAsDropDown(binding.allMembersPopUpedttxt)


        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupOwnerEdttxt.setText(ownerId)

    }



    }