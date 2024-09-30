package cheap.thrills.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cheap.thrills.R;
import cheap.thrills.models.Order;

public class TicketViewAdapter extends RecyclerView.Adapter<TicketViewAdapter.ticketViewViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnItemClickListener mListener;
    private String themePark;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TicketViewAdapter(List<Order> orderList, Context context, String themePark) {
        this.orderList = orderList;
        this.context = context;
        this.themePark = themePark;
    }

    @NonNull
    @Override
    public ticketViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket,parent,false);
        return new TicketViewAdapter.ticketViewViewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ticketViewViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvThemePark.setText(order.getTicketOrder());
        holder.tvAttendee.setText(order.getName());
        StringBuilder stepText = new StringBuilder("");
        StringBuilder legalText = new StringBuilder("");
        // Apply the styled label on the TextView
        if(order.getSteps() != null)
        {
            for (Order.Steps_Legal steps_legal:order.getSteps()){
                if(String.valueOf(stepText).isEmpty())
                    stepText.append("<b><b>"+steps_legal.getStepID()+"</b></b> "+steps_legal.getStepName());
                else
                    stepText.append("<br><b><b>"+steps_legal.getStepID()+"</b></b> "+steps_legal.getStepName());
            }
            holder.tvStep1.setText(Html.fromHtml(String.valueOf(stepText)));
        }
        if(order.getLegal() != null)
        {
            for (Order.Steps_Legal steps_legal:order.getLegal()){
                    legalText.append(steps_legal.getLegalTerms());
            }
            holder.tvLT.setText(Html.fromHtml(String.valueOf(legalText)));
        }
        if(order.getTicketId().length() == 19){
            // A995 08470 07497 30915
            holder.tvTicketNo.setText(order.getTicketId().substring(0,4)+" "+
                    order.getTicketId().substring(4,9)+" "+order.getTicketId().substring(9,14)+" "+
                    order.getTicketId().substring(14,19));
        }else if (order.getTicketId().length() == 18){
            //        7052 64102 93581 9971

            holder.tvTicketNo.setText(order.getTicketId().substring(0,4)+" "+
                    order.getTicketId().substring(4,9)+" "+order.getTicketId().substring(9,14)+" "+
                    order.getTicketId().substring(14,18));
        }else {
            holder.tvTicketNo.setText(order.getTicketId());
        }

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
        BitMatrix bitMatrix = multiFormatWriter.encode(ticketId, BarcodeFormat.CODE_128,550,100,null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        ivBarcode.setImageBitmap(bitmap);
    }

    public class ticketViewViewHolder extends RecyclerView.ViewHolder {

        private TextView tvThemePark, tvTicketNo, tvAttendee,tvStep1,tvLT;
        private ImageView ivBarcode;

        public ticketViewViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvThemePark = itemView.findViewById(R.id.tvThemePark);
            tvTicketNo = itemView.findViewById(R.id.tvTicketNo);
            tvAttendee = itemView.findViewById(R.id.tvAttendee);
            tvStep1 = itemView.findViewById(R.id.tvStep1);
            tvLT = itemView.findViewById(R.id.tvLT);
            ivBarcode = itemView.findViewById(R.id.ivBarcode);
            tvThemePark = itemView.findViewById(R.id.tvThemePark);

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
