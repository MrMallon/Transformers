package mr.fugugames.com.transformers.extensions

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

infix fun Button.goes(route: NavDirections) {
    setOnClickListener {
        findNavController().navigate(route)
    }
}

infix fun FloatingActionButton.goes(route: NavDirections) {
    setOnClickListener {
        findNavController().navigate(route)
    }
}

fun Fragment.setTitle(title: String) {
    (activity as? AppCompatActivity)?.supportActionBar?.title = title
}