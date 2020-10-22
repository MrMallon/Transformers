package mr.fugugames.com.transformers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import mr.fugugames.com.transformers.extensions.IBackPressed

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onBackPressed() {
        if(nav_host_fragment.childFragmentManager.fragments.size > 0){
            (nav_host_fragment.childFragmentManager.fragments[0] as? IBackPressed).let {
                if(it == null)
                        super.onBackPressed()
                else
                    it.onBackPress()
            }
        }
        else
            super.onBackPressed()
    }
}
