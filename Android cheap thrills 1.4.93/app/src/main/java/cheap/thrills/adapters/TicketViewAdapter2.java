package cheap.thrills.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class TicketViewAdapter2 extends RecyclerView.Adapter<TicketViewAdapter2.ticketViewHolder2> {

    private List<Order> orderList;
    private Context context;
    private OnItemClickListener mListener;
    private String themePark;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TicketViewAdapter2(List<Order> orderList, Context context, String themePark) {
        this.orderList = orderList;
        this.context = context;
        this.themePark = themePark;
    }

    @NonNull
    @Override
    public ticketViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket2,parent,false);
        return new TicketViewAdapter2.ticketViewHolder2(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewAdapter2.ticketViewHolder2 holder, int position) {
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
        BitMatrix bitMatrix = multiFormatWriter.encode(ticketId, BarcodeFormat.CODE_128,600,200,null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        ivBarcode.setImageBitmap(bitmap);
    }

    public class ticketViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView tvLogo;
        private TextView tvThemePark, tvTicketNo, tvAttendee;
        private ImageView ivBarcode;

        public ticketViewHolder2(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            tvThemePark = itemView.findViewById(R.id.tvThemePark);
            tvTicketNo = itemView.findViewById(R.id.tvTicketNo);
            tvAttendee = itemView.findViewById(R.id.tvAttendee);
            ivBarcode = itemView.findViewById(R.id.ivBarcode);
            tvLogo = itemView.findViewById(R.id.tvLogo);

            switch (themePark) {
                case "Six Flags Magic Mountain":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_magic_mountain));
                    break;
                case "Six Flags Great Adventure":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_great_adventure));
                    break;
                case "Six Flags Over Georgia":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_over_georgia));
                    break;
                case "Six Flags Great America":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_great_america));
                    break;
                case "Six Flags Fiesta Texas":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_fiesta_texas));
                    break;
                case "Six Flags Discovery Kingdom":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_discovery_kingdom));
                    break;
                case "Six Flags America":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_america));
                    break;
                case "Six Flags New England":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_new_england));
                    break;
                case "Six Flags Over Texas":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_over_texas));
                    break;
                case "Six Flags White Water":
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tvLogo .getLayoutParams();
                    params.verticalBias = 0.1f;
                    tvLogo.setLayoutParams(params);
                    tvLogo.getLayoutParams().width = 700;
                    tvLogo.requestLayout();
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_white_water));
                    break;
                case "Six Flags Darien Lake":
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_darien_lake));
                    break;
                case "Six Flags Hurricane Harbor":
                    ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) tvLogo .getLayoutParams();
                    params3.verticalBias = 0.1f;
                    tvLogo.setLayoutParams(params3);
                    tvLogo.getLayoutParams().width = 700;
                    tvLogo.requestLayout();
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_hurricane_harbor));
                    break;
                case "Frontier City":
                    ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) tvLogo .getLayoutParams();
                    params2.verticalBias = 0.1f;
                    tvLogo.setLayoutParams(params2);
                    tvLogo.getLayoutParams().width = 700;
                    tvLogo.requestLayout();
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_frontier_city));
                    break;
                default:
                    tvLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_six_flags_only));
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
