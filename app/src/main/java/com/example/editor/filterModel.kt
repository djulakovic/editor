import android.net.Uri
import com.zomato.photofilters.imageprocessors.Filter

data class FilterModel(
    var uri: Uri,
    var title: Int,
    var filter: Filter,
) {
}
