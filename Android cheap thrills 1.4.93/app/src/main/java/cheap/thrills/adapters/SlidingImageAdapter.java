package cheap.thrills.adapters;

import android.app.Activity;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cheap.thrills.R;
import java.util.List;
import butterknife.BindView;

public class SlidingImageAdapter extends PagerAdapter {
    @BindView(R.id.image)
    ImageView imageView;
    private List<String> urls;
    private LayoutInflater inflater;
    private Activity context;

    public SlidingImageAdapter(List<String> urls, Activity context) {
        this.urls = urls;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
    @Override
    public int getCount() {
        return urls.size();
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidinglayout, view, false);
        String url = urls.get(position);
        url = url.replace("http", "https");

        if (imageLayout != null) {
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder))
                    .into(imageView);
        }
        view.addView(imageLayout, 0);
        return imageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
