package mr.fugugames.com.transformers.extensions

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("setString")
fun EditText.setString(data: String){
    this.setText(data)
}

@BindingAdapter("setInt")
fun TextView.setInt(data: Int){
    this.text = data.toString()
}

@BindingAdapter("logo")
fun ImageView.setLogo(urlString: String){
    val picasso = Picasso.get()
    picasso.load(urlString).into(this)
}