package cheap.thrills.adapters;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import cheap.thrills.R;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.interfaces.AdapterCallback;
import cheap.thrills.models.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserAdapter extends RecyclerView.Adapter {
    AdapterCallback mAdapterCallback;
    ViewPager viewpager;
    List<Order> mlist;
    Context mContext;
    LayoutInflater mLayout;
    @BindView(R.id.radio_pirates)
    RadioButton radioPirates;
    @BindView(R.id.tvTextCheckBoxName)
    TextViewArialRegular tvTextCheckBoxName;


    public UserAdapter(List<Order> mlist, Context mContext, AdapterCallback a, ViewPager vp) {
        this.mlist = mlist;
        this.mContext = mContext;
        mLayout = LayoutInflater.from(mContext);
        this.mAdapterCallback = a;
        this.viewpager = vp;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyView(LayoutInflater.from(mContext).inflate(R.layout.recyclerviewuserlist, viewGroup, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyView myView = (MyView) viewHolder;
        Order order = mlist.get(i);
        myView.tvTextCheckBoxName.setText(order.getName());
        if (order.isiSclicked()) {
            myView.radio_pirates.setChecked(true);
        } else {
            myView.radio_pirates.setChecked(false);
        }
        myView.llFinalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int index = 0; index < mlist.size(); index++) {
                    if (index != i) {
                        mlist.get(index).setiSclicked(false);
                    }
                    mlist.get(i).setiSclicked(true);
                    viewpager.setCurrentItem(i, true);
                }
                mAdapterCallback.onMethodCallback(mlist.get(i));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class MyView extends RecyclerView.ViewHolder {
        @BindView(R.id.radio_pirates)
        RadioButton radio_pirates;
        @BindView(R.id.tvTextCheckBoxName)
        TextViewArialBold tvTextCheckBoxName;
        @BindView(R.id.llFinalLayout)
        LinearLayout llFinalLayout;

        public MyView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
