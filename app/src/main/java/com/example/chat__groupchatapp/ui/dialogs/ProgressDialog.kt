package com.example.chat__groupchatapp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.chat__groupchatapp.databinding.ProgressDialogBinding
import java.lang.reflect.Executable

class ProgressDialog : DialogFragment() {

    lateinit var binding : ProgressDialogBinding

    companion object{
        const val TAG = "Progress_Dialog"

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProgressDialogBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val lp = dialog?.window?.decorView?.layoutParams
        lp?.height = LayoutParams.MATCH_PARENT
        lp?.width = LayoutParams.MATCH_PARENT

        dialog?.window?.decorView?.layoutParams = lp
    }

    fun showDialog(fragmentManager: FragmentManager){
        try {
            show(fragmentManager, TAG)
        }catch (e:Exception){

        }
    }

    fun dismissDialog(){
        try {
            dismiss()

        }catch (e:Exception){

        }
    }
    
}