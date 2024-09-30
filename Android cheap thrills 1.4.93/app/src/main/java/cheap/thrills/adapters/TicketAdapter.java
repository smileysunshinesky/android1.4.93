package cheap.thrills.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cheap.thrills.R;
import cheap.thrills.activities.WalletDetailsActivity;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewArialDoubleBold;
import cheap.thrills.fonts.TextViewArialMedium;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.models.Order;
import cheap.thrills.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;

public class TicketAdapter extends PagerAdapter {
//    @BindView(R.id.tvTickerOrder)
//    TextViewArialMedium tvTicketOrder;
    private ArrayList<Order> urls;
    private LayoutInflater inflater;
    private Activity context;
    private Boolean kid;

    public TicketAdapter(ArrayList<Order> urls, Activity context, Boolean kidcount) {
        this.urls = urls;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.kid = kidcount;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.ticketviewpager, view, false);
        if (imageLayout != null) {
            final TextViewArialRegular tvTicketOrder = (TextViewArialRegular) imageLayout.findViewById(R.id.tvTickerOrder);
            final TextViewArialDoubleBold tvCustomer = (TextViewArialDoubleBold) imageLayout.findViewById(R.id.tvCustomer);
            final ImageView ivBar = (ImageView) imageLayout.findViewById(R.id.ivBarcode);
            final LinearLayout llticketDetail = (LinearLayout) imageLayout.findViewById(R.id.llticketDetail);
            final TextViewArialMedium tvBarcode = (TextViewArialMedium) imageLayout.findViewById(R.id.tvQrCode);
            final ImageView ivIcon = (ImageView) imageLayout.findViewById(R.id.ivIcon);

            final String strTicketOrder = urls.get(position).getTicketOrder();
            tvTicketOrder.setText(strTicketOrder);
            String regex = context.getString(R.string.base64prefix);

            if(strTicketOrder.toUpperCase().contains("2PK") || strTicketOrder.toUpperCase().contains("2 PARK") || strTicketOrder.toUpperCase().contains("2-PARK")){
                ivIcon.setImageResource(R.drawable.two_park_logo_center);
            }else{
                ivIcon.setImageResource(R.drawable.three_park_logo);
            }

            llticketDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, WalletDetailsActivity.class);
                    i.putExtra(Constants.TICKET_ORDER, urls.get(position).getTicketOrder());
                    i.putExtra(Constants.TICKET_DATE, urls.get(position).getTdate());
                    i.putExtra(Constants.TICKET_ID,urls.get(position).getTicketId());
                    i.putExtra(Constants.CUSTOMER_NAME, urls.get(position).getName());
                    i.putExtra(Constants.TYPE, urls.get(position).getType());
                    context.startActivity(i);
                }
            });
            String baseUrl = urls.get(position).getQrCode();
            if (!baseUrl.equals(""))
                baseUrl = baseUrl.replaceAll(regex, "");
            String finalString = baseUrl.toString().trim();
            byte[] decodedString = Base64.decode(finalString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivBar.setImageBitmap(decodedByte);
            tvCustomer.setText(urls.get(position).getName());
            tvBarcode.setText(urls.get(position).getTicketId());



        }
        view.addView(imageLayout, 0);

        return imageLayout;

    }

    public String shuffle(String s) {

        String shuffledString = "";

        while (s.length() != 0) {
            int index = (int) Math.floor(Math.random() * s.length());
            char c = s.charAt(index);
            s = s.substring(0, index) + s.substring(index + 1);
            shuffledString += c;
        }

        return shuffledString;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
