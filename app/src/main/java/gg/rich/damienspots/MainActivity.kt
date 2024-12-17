package gg.rich.damienspots

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import processing.android.PFragment
import processing.core.PApplet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize your Processing sketch
        val sketch: PApplet = DamienDots_Main()

        // Attach the Processing sketch to this activity
        val fragment = PFragment(sketch)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}
