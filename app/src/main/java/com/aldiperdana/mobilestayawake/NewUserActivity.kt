package com.aldiperdana.mobilestayawake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        val bottom_sheet: TextView = findViewById(R.id.tv_referral)
        bottom_sheet.setOnClickListener{
            val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_referral_code, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()
        }
    }
}