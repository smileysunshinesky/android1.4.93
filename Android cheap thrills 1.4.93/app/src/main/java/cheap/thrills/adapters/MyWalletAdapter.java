package cheap.thrills.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cheap.thrills.R;
import cheap.thrills.activities.WalletActivity;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.models.Order;
import cheap.thrills.utils.Constants;

public class MyWalletAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<Order> mlist;
    ArrayList<Order> mActualList;
    LayoutInflater mLayout;
    String strKidCount;
    String strAdultCount;
    @BindView(R.id.tvTicketSizes)
    TextViewArialBold tvTicketSizes;
    @BindView(R.id.tvTicketId)
    TextViewArialRegular tvTicketId;
    @BindView(R.id.llItem)
    LinearLayout llItem;
    @BindView(R.id.ticketType)
    TextViewArialBold ticketType;
    @BindView(R.id.img_ticketType)
    ImageView img_ticketType;
    @BindView(R.id.img_ticketType_logo)
    ImageView img_ticketType_logo;


    public MyWalletAdapter(Context mContext, ArrayList<Order> mActualList, ArrayList<Order> mlist, String strAdultCount, String strKidCount) {
        this.mContext = mContext;
        this.mActualList = new ArrayList<>();
        ArrayList<Order> childlist = new ArrayList<>();
        for (Order order:mActualList){
            //order.getTicket_type().equalsIgnoreCase("1") ||
            if(order.getType().equalsIgnoreCase("kid") )
                childlist.add(order);
            else
                this.mActualList.add(order);
        }
        this.mActualList.addAll(childlist);
        this.mlist = mlist;
        this.strAdultCount = strAdultCount;
        this.strKidCount = strKidCount;
        mLayout = LayoutInflater.from(mContext);

        Log.e("Adapter", "===Adult Count=="+strAdultCount);
        Log.e("Adapter", "===Kid Count=="+strKidCount);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyView(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_mywallet, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyView myView = (MyView) viewHolder;
/*

        if (mActualList.get(i).getType().equals(mContext.getString(R.string.typeadult))) {
            myView.tvTicketId.setText(mActualList.get(i).getTicketOrder());
            if (!mActualList.get(i).getCount().equals("")) {
                myView.tvTicketSizes.setText(mContext.getString(R.string.x).concat(mActualList.get(i).getCount()));
                myView.tvTicketSizes.setVisibility(View.VISIBLE);
            } else {
                myView.tvTicketSizes.setVisibility(View.GONE);
            }
        } else {
            myView.tvTicketId.setText(mActualList.get(i).getTicketOrder());
            if (!mActualList.get(i).getCount().equals("")) {
                myView.tvTicketSizes.setText(mContext.getString(R.string.x).concat(mActualList.get(i).getCount()));
                myView.tvTicketSizes.setVisibility(View.VISIBLE);
            } else {
                myView.tvTicketSizes.setVisibility(View.GONE);
            }
        }
*/


        String ticketID = mActualList.get(i).getTicketOrder().toUpperCase();
        myView.tvTicketId.setText(ticketID);

        String ticketType = mActualList.get(i).getTicket_type();

        if((ticketType!= null && ticketType.equalsIgnoreCase("2"))){
            myView.ticketType.setText("ANNUAL PASS");
            myView.img_ticketType_logo.setImageResource(R.drawable.uoap_icon);
        }else{
            myView.ticketType.setText("PARK TICKET");
            myView.img_ticketType_logo.setImageResource(R.drawable.icon_ticket);
        }

        if(ticketID.contains("2PK") || ticketID.contains("2 PARK") || ticketID.contains("2-PARK")){
            myView.img_ticketType.setImageResource(R.drawable.two_park_logo);
        }else{
            myView.img_ticketType.setImageResource(R.drawable.three_park_logo);
        }

        String setLink = mActualList.get(i).getSet_link();
        if (setLink != null && setLink.contains("HHN")){
            myView.img_ticketType.setVisibility(View.GONE);
            myView.img_horrornights.setVisibility(View.VISIBLE);
        }



        if (mActualList.get(i).getType().equals(mContext.getString(R.string.typeadult))){
            if (Integer.parseInt(strAdultCount) > 0) {
                myView.tvTicketSizes.setText(mContext.getString(R.string.x).concat(strAdultCount));
                myView.tvTicketSizes.setVisibility(View.VISIBLE);
            } else {
                myView.tvTicketSizes.setVisibility(View.GONE);
            }
        }else if (mActualList.get(i).getType().equals(mContext.getString(R.string.typekid))){
            if (Integer.parseInt(strKidCount) > 0) {
                myView.tvTicketSizes.setText(mContext.getString(R.string.x).concat(strKidCount));
                myView.tvTicketSizes.setVisibility(View.VISIBLE);
            }else {
                myView.tvTicketSizes.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mActualList.size();
    }



    public class MyView extends RecyclerView.ViewHolder {
        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.tvTicketId)
        TextView tvTicketId;
        @BindView(R.id.tvTicketSizes)
        TextViewArialBold tvTicketSizes;
        @BindView(R.id.ticketType)
        TextViewArialBold ticketType;
        @BindView(R.id.img_ticketType)
        ImageView img_ticketType;
        @BindView(R.id.img_horrornights)
        ImageView img_horrornights;
        @BindView(R.id.img_ticketType_logo)
        ImageView img_ticketType_logo;


        public MyView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.llItem)
        public void onClick(View view) {
            Intent i = new Intent(mContext, WalletActivity.class);
            if (mActualList.get(getAdapterPosition()).getTicketOrder() != null && !mActualList.get(getAdapterPosition()).getTicketOrder().isEmpty()){

                if (mlist.get(getAdapterPosition()).getType().equals(mContext.getString(R.string.typeadult))) {
                    i.putExtra(Constants.TICKET_ORDER, mActualList.get(getAdapterPosition()).getTicketOrder());
                } else if (mlist.get(getAdapterPosition()).getType().equals(mContext.getString(R.string.typecustomer))) {
                    i.putExtra(Constants.TICKET_ORDER, mActualList.get(getAdapterPosition()).getTicketOrder());
                } else {
                    i.putExtra(Constants.TICKET_ORDER, mActualList.get(getAdapterPosition()).getTicketOrder());
                }
                if (tvTicketSizes.getVisibility() == View.VISIBLE) {
                    i.putExtra(Constants.VISIBILITYCHECK, true);
                }
                if (tvTicketSizes.getVisibility() == View.VISIBLE) {
                    String s = tvTicketSizes.getText().toString().trim().replaceAll(mContext.getString(R.string.x), "");
                    i.putExtra(Constants.KID_COUNT, strKidCount);
                } else {
                    i.putExtra(Constants.KID_COUNT, String.valueOf(1));
                }
                i.putExtra(Constants.TICKET_DATE, mActualList.get(getAdapterPosition()).getTdate());
                i.putExtra(Constants.TICKET_ID, mActualList.get(getAdapterPosition()).getTicketId());
                i.putExtra(Constants.GID, mActualList.get(getAdapterPosition()).getGid());
                i.putExtra(Constants.TICKET_URL, mActualList.get(getAdapterPosition()).getQrCode());
                i.putExtra(Constants.CUSTOMER_NAME, mActualList.get(getAdapterPosition()).getName());
                i.putExtra(Constants.TYPE, mActualList.get(getAdapterPosition()).getType());
                i.putExtra(Constants.MOBILE, mActualList.get(getAdapterPosition()).getMobile());
                i.putExtra(Constants.LIST, mlist);

                Constants.orderList = mlist;
                mContext.startActivity(i);
            }else{
                new AlertDialog.Builder(mContext)
                        .setTitle("Alert!")
                        .setMessage("TICKETS NOT ASSIGNED\n" +
                                "Please ask the associate to assign your tickets.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
}
