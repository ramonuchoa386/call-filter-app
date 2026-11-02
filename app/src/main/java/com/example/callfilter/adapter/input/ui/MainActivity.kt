package com.example.callfilter.adapter.input.ui

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.callfilter.R
import com.example.callfilter.adapter.output.SharedPreferencesSettingsRepository
import com.example.callfilter.domain.port.input.ToggleBlockUnknownUseCase
import com.example.callfilter.domain.port.input.ToggleContactExclusionUseCase

class MainActivity : AppCompatActivity() {

    private lateinit var repository: SharedPreferencesSettingsRepository
    private lateinit var toggleBlockUnknownUseCase: ToggleBlockUnknownUseCase
    private lateinit var toggleContactExclusionUseCase: ToggleContactExclusionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = SharedPreferencesSettingsRepository(applicationContext)
        toggleBlockUnknownUseCase = ToggleBlockUnknownUseCase(repository)
        toggleContactExclusionUseCase = ToggleContactExclusionUseCase(repository)

        setupPermissions()
        setupUI()
    }

    private fun setupUI() {
        val switchBlockUnknown = findViewById<Switch>(R.id.switchBlockUnknown)
        val edtPhoneNumber = findViewById<EditText>(R.id.edtPhoneNumber)
        val btnBlockNumber = findViewById<Button>(R.id.btnBlockNumber)
        val btnUnblockNumber = findViewById<Button>(R.id.btnUnblockNumber)

        switchBlockUnknown.isChecked = repository.getSettings().blockUnknown

        switchBlockUnknown.setOnCheckedChangeListener { _, isChecked ->
            toggleBlockUnknownUseCase.execute(isChecked)
        }

        btnBlockNumber.setOnClickListener {
            val phone = edtPhoneNumber.text.toString().trim()
            if (phone.isNotEmpty()) {
                toggleContactExclusionUseCase.execute(phone, block = true)
                Toast.makeText(this, "Número bloqueado", Toast.LENGTH_SHORT).show()
                edtPhoneNumber.text.clear()
            }
        }

        btnUnblockNumber.setOnClickListener {
            val phone = edtPhoneNumber.text.toString().trim()
            if (phone.isNotEmpty()) {
                toggleContactExclusionUseCase.execute(phone, block = false)
                Toast.makeText(this, "Número desbloqueado", Toast.LENGTH_SHORT).show()
                edtPhoneNumber.text.clear()
            }
        }
    }

    private fun setupPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) && !roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                startActivityForResult(intent, 101)
            }
        }
    }
}