package cheap.thrills.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import cheap.thrills.R;

import cheap.thrills.models.Order;

import java.util.List;

public class TicketViewAdapter3 extends RecyclerView.Adapter<TicketViewAdapter3.ticketViewHolder2> {

    private List<Order> orderList;
    private Context context;
    private OnItemClickListener mListener;
    private String themePark;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TicketViewAdapter3(List<Order> orderList, Context context, String themePark) {
        this.orderList = orderList;
        this.context = context;
        this.themePark = themePark;
    }

    @NonNull
    @Override
    public ticketViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket3,parent,false);
        return new TicketViewAdapter3.ticketViewHolder2(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewAdapter3.ticketViewHolder2 holder, int position) {
        Order order = orderList.get(position);

        holder.tvAttendee.setText(order.getName());
        holder.tvTicketNo.setText(order.getTicketId());

        try {
            grtBarcode(holder.ivBarcode, order.getTicketId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

    private void grtBarcode(ImageView ivBarcode, String ticketId) throws WriterException {
        BitMatrix bitMatrix = multiFormatWriter.encode(ticketId, BarcodeFormat.QR_CODE,500,500,null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        ivBarcode.setImageBitmap(bitmap);
    }

    public class ticketViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView tvLogo;
        private TextView tvThemePark, tvTicketNo, tvAttendee;
        private ImageView ivBarcode;
        private ConstraintLayout bgConstraintLayout;
        private RelativeLayout bgRelativeLayout;

        public ticketViewHolder2(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            tvThemePark = itemView.findViewById(R.id.tvThemePark);
            tvTicketNo = itemView.findViewById(R.id.tvTicketNo);
            tvAttendee = itemView.findViewById(R.id.tvAttendee);
            ivBarcode = itemView.findViewById(R.id.ivBarcode);
            tvLogo = itemView.findViewById(R.id.tvLogo);
            bgConstraintLayout = itemView.findViewById(R.id.bgConstraintLayout);
            bgRelativeLayout = itemView.findViewById(R.id.bgRelativeLayout);

            switch (themePark) {
                case "Kings Island":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_kings_island));
                    break;
                case "Cedar Point":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_cedar_point));
                    break;
                case "Carowinds":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_carowinds_logo));
                    break;
                case "Knott's Berry Farm":
                    tvLogo.getLayoutParams().width = 700;
                    tvLogo.requestLayout();
                    bgRelativeLayout.setBackgroundColor(Color.WHITE);
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_knottsberry_farm));
                    break;
                case "Dorney Park":
                    tvLogo.requestLayout();
                    bgRelativeLayout.setBackgroundColor(Color.WHITE);
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_dorney_park));
                    break;
                case "Worlds Of Fun":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_worlds_of_fun));
                    break;
                case "Kings Dominion":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_lings_dominion));
                    break;
                case "Valleyfair":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_valley_fair));
                    break;
                default:
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_kings_island));
                    break;

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
